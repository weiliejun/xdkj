package com.xdkj.admin.system.service;

import com.xdkj.common.model.sysBusinessLog.bean.SysBusinessLog;
import com.xdkj.common.model.sysBusinessLog.dao.SysBusinessLogMapper;
import com.xdkj.common.util.DateHelper;
import com.xdkj.common.util.IpHelper;
import com.xdkj.common.web.bean.CurrentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("monitorService")
public class MonitorServiceImp implements MonitorService {

    @Autowired
    public SysBusinessLogMapper monitorRepository;

    @Override
    public List findBusinessLogs(Map params) {
        return monitorRepository.findBusinessLogs(params);
    }

    @Override
    @Transactional
    public void saveBusinessLog(CurrentManager currentManager, String functionModule, String functionDescription, String operationData) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysBusinessLog log = new SysBusinessLog();
        String operationTime = DateHelper.getYMDHMSFormatDate(new Date());
        log.setManagerId(currentManager.getSysManager().getId());
        log.setManagerCode(currentManager.getSysManager().getCode());
        log.setManagerName(currentManager.getSysManager().getName());
        log.setSessionId(currentManager.getSessionId());
        log.setIp(IpHelper.getClientIpAddress(request));
        log.setOperationTime(new Date());
        log.setFunctionModule(functionModule);
        log.setFunctionDescription(functionDescription);
        log.setOperationData(operationData);
        monitorRepository.saveBusinessLog(log);
    }


}
