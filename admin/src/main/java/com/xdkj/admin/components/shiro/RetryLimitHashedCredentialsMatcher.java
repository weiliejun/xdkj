package com.xdkj.admin.components.shiro;

import com.xdkj.admin.system.service.SysManagerService;
import com.xdkj.common.model.sysManager.bean.SysManager;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description 密码错误次数限制匹配器
 * @auther: zhangkele
 * @UpadteDate: 2019/3/5 12:33
 */
public class RetryLimitHashedCredentialsMatcher extends SimpleCredentialsMatcher {

    private static final Logger logger = LoggerFactory.getLogger(RetryLimitHashedCredentialsMatcher.class);

    @Autowired
    private SysManagerService sysManagerService;

    private final Cache<String, Integer> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获取用户名
        String userName = (String) token.getPrincipal();
        //获取用户登录次数
        Integer retryCount = passwordRetryCache.get(userName);
        if (retryCount == null) {
            //如果用户没有登陆过,登陆次数加1 并放入缓存
            retryCount = 1;
            passwordRetryCache.put(userName, retryCount);
        } else {
            retryCount++;
            passwordRetryCache.put(userName, retryCount);
        }
        if (retryCount > 3) {
            //如果用户登陆失败次数大于3次 抛出锁定用户异常  并修改数据库字段
            SysManager sysManager = sysManagerService.getSysManagerByCode(userName);
            if (sysManager != null && "0".equals(sysManager.getStatus())) {
                //数据库字段 默认为 0  就是正常状态 所以 要改为1
                //修改数据库的状态字段为锁定
                SysManager sysManagerTemp = new SysManager();
                sysManagerTemp.setStatus("2");
                sysManagerTemp.setId(sysManager.getId());
                sysManagerService.updateSysManager(sysManagerTemp);
            }
            logger.info("锁定用户" + sysManager.getCode());
            throw new ExcessiveAttemptsException();
        }
        //判断用户账号和密码是否正确
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //如果正确,从缓存中将用户登录计数 清除
            passwordRetryCache.remove(userName);
        }
        return matches;
    }

    /**
     * 根据用户名 解锁用户
     *
     * @param userName
     * @return
     */
    public void unlockAccount(String userName) {
        SysManager sysManager = sysManagerService.getSysManagerByCode(userName);
        if (sysManager != null) {
            //修改数据库的状态字段为正常状态
            SysManager sysManagerTemp = new SysManager();
            sysManagerTemp.setStatus("0");
            sysManagerTemp.setId(sysManager.getId());
            sysManagerService.updateSysManager(sysManagerTemp);
            passwordRetryCache.remove(userName);
        }
    }

}