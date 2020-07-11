package com.xdkj.pc.service.userManage;

import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.userTopup.bean.UserTopup;
import com.xdkj.common.model.userTopup.dao.UserTopupDao;
import com.xdkj.common.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userTopupService")
public class UserTopupServiceImpl implements UserTopupService {

    @Autowired
    private UserTopupDao userTopupDao;

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

    public BigDecimal countUserTopupTotalAmount(Integer userId) {
        return userTopupDao.countUserTopupTotalAmount(userId);
    }

    public Map<String, String> offLineTransfer(Map<String, Object> params) throws IOException {
        Map<String, String> result = new HashMap<String, String>();
        UserInfo userInfo = (UserInfo) params.get("userInfo");
        String transAmount = (String) params.get("transAmount");
        String topUpType = (String) params.get("topUpType");
        String mobile = (String) params.get("mobile");
        MultipartFile transferFile = (MultipartFile) params.get("transferFile");
        String realPath = ResourceUtils.getURL("classpath:").getPath();
        realPath = realPath.replace("/target/classes/", "/src/main/resources/static");
        //String realPath = "D:\\idea_workspace\\mbiger\\pc\\src\\main\\resources\\static";
        String tmpPath = GlobalConstant.TRANSFER_FILES_TEMP_PATH;
        String filePath = realPath + tmpPath + userInfo.getId() + "/";
        File tmpFilePath = new File(filePath);
        if (!tmpFilePath.exists()) {
            tmpFilePath.mkdirs();
        }
        String filename = transferFile.getOriginalFilename();
        String prefix = filename.substring(filename.lastIndexOf(".") + 1); // 文件后缀
        String tempFileName = RandomUtil.getSerialNumber() + "." + prefix; // 生成文件名字
        // 创建文件
        File tmpFile = new File(filePath + tempFileName);
        transferFile.transferTo(tmpFile);

        UserTopup userTopup = new UserTopup();
        userTopup.setUserId(userInfo.getId());
        userTopup.setUserName(userInfo.getUserName());
        userTopup.setTopupAmount(new BigDecimal(transAmount));
        userTopup.setTopupType(topUpType);
        userTopup.setStatus("submit");
        userTopup.setUserMobile(mobile);
        userTopup.setDataStatus("0");
        userTopup.setCreateTime(new Date());
        userTopup.setUpdateTime(new Date());
        userTopup.setOrdId(RandomUtil.getSerialNumber());
        userTopup.setTransferVoucher(tmpPath + userInfo.getId() + "/" + tempFileName);
        userTopupDao.addUserTopup(userTopup);
        result.put("flag", "true");
        result.put("msg", "转账成功！");
        return result;
    }
}
