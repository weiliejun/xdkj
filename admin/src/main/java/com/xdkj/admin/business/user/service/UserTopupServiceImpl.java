package com.xdkj.admin.business.user.service;

import com.xdkj.admin.system.service.SysMessageService;
import com.xdkj.common.constant.MessageBusiType;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.user.dao.UserInfoDao;
import com.xdkj.common.model.userTopup.bean.UserTopup;
import com.xdkj.common.model.userTopup.dao.UserTopupDao;
import com.xdkj.common.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userTopupService")
public class UserTopupServiceImpl implements UserTopupService {

    @Autowired
    private UserTopupDao userTopupDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private SysMessageService sysMessageService;

    public void addUserTopup(UserTopup userTopup) {
        userTopupDao.addUserTopup(userTopup);
    }

    public void updateUserTopup(UserTopup userTopup) {
        userTopupDao.updateUserTopup(userTopup);
    }

    public UserTopup getUserTopupById(Integer id) {
        return userTopupDao.getUserTopupById(id);
    }

    public List<UserTopup> listUserTopupsByParams(Map<String, Object> params) {
        return userTopupDao.listUserTopupsByParams(params);
    }

    public Map<String, Object> updateTopUpStatus(Integer userTopUpId) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        UserTopup userTopup = userTopupDao.getUserTopupById(userTopUpId);
        if (userTopup == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "更新失败");
            return resultMap;
        }
        UserInfo userInfo = userInfoDao.getUserInfoById(userTopup.getUserId());
        if (userInfo == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "更新失败");
            return resultMap;
        }
        // 1、把充值状态改为“成功”
        UserTopup temp = new UserTopup();
        temp.setId(userTopUpId);
        temp.setStatus("success");
        temp.setUpdateTime(new Date());
        userTopupDao.updateUserTopup(temp);

        //2、更新账户金额
        userInfoDao.updateUserAccountBalance(userInfo.getId(), userInfo.getUserAccountBalance().add(userTopup.getTopupAmount()));

        //3、发送消息通知
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", DateHelper.getYMDFormatDate(new Date()));
        params.put("amount", userTopup.getTopupAmount().toString());
        sysMessageService.sendWebSiteMessage(userInfo.getId(), MessageBusiType.TOPUP, params);

        resultMap.put("flag", "true");
        resultMap.put("msg", "更新成功");
        return resultMap;
    }
}
