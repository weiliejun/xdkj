package com.xdkj.admin.system.service;

import com.xdkj.common.components.exception.MessageBusiTypeTmplInexistenceException;
import com.xdkj.common.components.exception.MessageTypeTmplInexistenceException;
import com.xdkj.common.components.exception.ParameterNullPointerException;
import com.xdkj.common.components.exception.TemplateInexistenceException;
import com.xdkj.common.model.sysMessageTmpl.bean.SysMessageTmpl;
import com.xdkj.common.model.sysMessageTmpl.dao.SysMessageTmplDao;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("sysMessageTmplService")
public class SysMessageTmplServiceImpl implements SysMessageTmplService {
    @Autowired
    private SysMessageTmplDao sysMessageTmplDao;

    private static String[] getTmplVariable(String context) {

        String[] variables = StringUtils.substringsBetween(context, "${", "}");

        return variables;
    }

    public SysMessageTmpl addSysMessageTmpl(SysMessageTmpl sysMessageTmpl) {
        return sysMessageTmplDao.addSysMessageTmpl(sysMessageTmpl);
    }

    public SysMessageTmpl getSysMessageTmplById(Integer id) {
        return sysMessageTmplDao.getSysMessageTmplById(id);
    }

    public void updateSysMessageTmpl(SysMessageTmpl sysMessageTmpl) {
        sysMessageTmplDao.updateSysMessageTmpl(sysMessageTmpl);
    }

    public List<SysMessageTmpl> listSysMessageTmplsByParam(Map<String, Object> params) {
        return sysMessageTmplDao.listMessageTmplsByParam(params);
    }

    /**
     * 数据库查询内容解析为模板
     *
     * @param busiType
     * @param type
     * @param values
     * @return
     * @throws ParameterNullPointerException
     * @throws TemplateInexistenceException
     */
    public Map<String, String> tmplAssignment(String busiType, String type, Map<String, String> values) throws ParameterNullPointerException, TemplateInexistenceException {

        Map<String, String> result = new HashMap<String, String>();

        // 从数据库读取消息模板数据
        Map<String, String> tmplResult = getSysMessageTmpl(busiType, type);

        Set<String> keys = tmplResult.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            result.put(key, tmplResult.get(key));
        }

        if (MapUtils.isNotEmpty(result)) {
            if (type.equalsIgnoreCase("email") || type.equalsIgnoreCase("website")) {
                String title = result.get("title");
                if (StringUtils.isBlank(title)) {
                    throw new TemplateInexistenceException("未定义title模板内容！");
                }
                String[] variablesTitle = getTmplVariable(title);
                if (!ArrayUtils.isEmpty(variablesTitle)) {
                    for (String variable : variablesTitle) {
                        String val = values.get(variable);
                        if (StringUtils.isEmpty(val)) {
                            throw new ParameterNullPointerException(variable);
                        }
                        title = title.replace("${" + variable + "}", val);
                    }
                    result.put("title", title);
                }
            }

            String content = result.get("content");
            if (StringUtils.isBlank(content)) {
                throw new TemplateInexistenceException("未定义content模板内容！");
            }
            String[] variables = getTmplVariable(content);
            if (!ArrayUtils.isEmpty(variables)) {
                for (String variable : variables) {

                    String val = values.get(variable);
                    if (StringUtils.isEmpty(val)) {
                        throw new ParameterNullPointerException(variable);
                    }
                    content = content.replace("${" + variable + "}", val);
                }
                result.put("content", content);
            }
        }

        return result;
    }

    /**
     * cyp
     *
     * @param busiType
     * @param type
     * @return
     * @throws ParameterNullPointerException
     * @throws MessageBusiTypeTmplInexistenceException
     */
    private Map<String, String> getSysMessageTmpl(String busiType, String type) throws ParameterNullPointerException, MessageBusiTypeTmplInexistenceException {
        Map<String, String> result = null;
        if (StringUtils.isBlank(busiType)) {
            throw new ParameterNullPointerException("busiType");
        }

        if (StringUtils.isBlank(type)) {
            throw new ParameterNullPointerException("type");
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("busiType", busiType);
        params.put("type", type);
        List<SysMessageTmpl> messageTmplList = sysMessageTmplDao.listMessageTmplsByParam(params);
        SysMessageTmpl messageTmpl = null;
        if (messageTmplList != null && messageTmplList.size() > 0) {
            messageTmpl = messageTmplList.get(0);
        }
        //1表示禁用，后期改为字典获取值
        if (messageTmpl == null || "0".equals(messageTmpl.getStatus())) {
            throw new MessageTypeTmplInexistenceException(type);
        }

        result = new HashMap<String, String>();
        result.put("title", messageTmpl.getTitle());
        result.put("content", messageTmpl.getContent());
        return result;
    }
}