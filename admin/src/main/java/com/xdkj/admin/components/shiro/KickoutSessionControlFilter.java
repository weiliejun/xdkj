package com.xdkj.admin.components.shiro;

import com.alibaba.fastjson.JSON;
import com.xdkj.common.model.sysManager.bean.SysManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

public class KickoutSessionControlFilter extends AccessControlFilter {
    private static final Log logger = LogFactory.getLog(KickoutSessionControlFilter.class);
    //踢出后到的地址
    private String kickoutUrl;
    //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private boolean kickoutAfter = false;
    //同一个帐号最大会话数 默认1
    private int maxSession = 1;

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("sessionIdCache");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        //如果没有登录，直接进行之后的流程
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }


        Session session = subject.getSession();
        //已经被踢出
        if (session.getAttribute("kickout") != null) {
            return kickLoginOut(request, response);
        }

        /**
         * 缓存sessionId
         */

        //sid已经缓存
        if (session.getAttribute("cachSid") != null) {
            return true;
        }

        SysManager sysManager = (SysManager) subject.getPrincipal();
        String username = sysManager.getCode();
        Serializable sessionId = session.getId();

        //读取缓存   没有就存入
        Deque<Serializable> deque = cache.get(username);

        /**
         * 如果此用户没有session队列，也就是还没有登录过，缓存中没有
         * 就new一个空队列，不然deque对象为空，会报空指针
         */
        if (deque == null) {
            deque = new LinkedList<Serializable>();
        }
        //清除队列中无效的sessionId
        if (deque.size() > 0) {
            Iterator<Serializable> it = deque.iterator();
            while (it.hasNext()) {
                Serializable cacheSid = it.next();
                try {
                    Session cacheSession = sessionManager.getSession(new DefaultSessionKey(cacheSid));
                    if (cacheSession == null) {
                        deque.remove(cacheSid);
                    }
                } catch (UnknownSessionException e) {
                    deque.remove(cacheSid);
                }

            }
        }

        //缓存sessionId
        if (!deque.contains(sessionId)) {
            //将sessionId存入队列
            deque.push(sessionId);
            //将用户的sessionId队列缓存
            cache.put(username, deque);
            session.setAttribute("cachSid", true);
        }


        //如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if (kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
                //踢出后再更新下缓存队列
                cache.put(username, deque);
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
                //踢出后再更新下缓存队列
                cache.put(username, deque);
            }

            try {
                //获取被踢出的sessionId的session对象
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {
                logger.error("获取session对象", e);
            }
        }
        //已经被踢出
        if (session.getAttribute("kickout") != null) {
            return kickLoginOut(request, response);
        }
        return true;
    }

    /**
     * 被踢出退出登录
     */
    private boolean kickLoginOut(ServletRequest request, ServletResponse response) {
        Subject subject = getSubject(request, response);
        Session session = subject.getSession();
        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                //退出登录
                subject.logout();
            } catch (Exception e) {
                logger.error("退出登录异常", e);
            }
            saveRequest(request);

            Map<String, String> resultMap = new HashMap<String, String>();
            //判断是不是Ajax请求
            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "您已经在其他地方登录，请重新登录！");
                //输出json串
                out(response, resultMap);
            } else {
                //重定向
                try {
                    WebUtils.issueRedirect(request, response, kickoutUrl);
                } catch (IOException e) {
                    logger.error("被踢出退出登录，重定向异常", e);
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 输出json
     */
    private void out(ServletResponse hresponse, Map<String, String> resultMap) {
        try {
            hresponse.setCharacterEncoding("UTF-8");
            PrintWriter out = hresponse.getWriter();
            out.println(JSON.toJSONString(resultMap));
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("KickoutSessionFilter.class 输出JSON异常。", e);
        }
    }
}