package com.xdkj.pc.service.dlsInfo;

import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.dlsInfo.bean.DlsInfo;
import com.xdkj.common.model.dlsInfo.dao.DlsInfoMapper;
import com.xdkj.common.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("DlsInfoService")
public class DlsInfoServiceImpl implements DlsInfoService {

    @Autowired
    private DlsInfoMapper dlsInfoMapper;

    /**
     * 添加人员信息管理
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 16:41
     * @Param
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DlsInfo addDlsInfo(DlsInfo DlsInfo) {
        // Id
        DlsInfo.setId(RandomUtil.getSerialNumber());
        // 数据有效性 0 无效 1 有效
        DlsInfo.setDataStatus(GlobalConstant.DATA_VALID);
        return dlsInfoMapper.addDlsInfo(DlsInfo);
    }

    /**
     * 获取人员信息管理
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 16:42
     * @Param
     **/
    @Override
    public DlsInfo getDlsInfoById(String id) {
        return dlsInfoMapper.selectDlsInfoById(id);
    }

    @Override
    public DlsInfo getDlsInfoByName(String name) {
        return dlsInfoMapper.selectDlsInfoByName(name);
    }

    /**
     * 修改人员信息管理
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 16:43
     * @Param
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDlsInfo(DlsInfo DlsInfo) {
        dlsInfoMapper.updateDlsInfoById(DlsInfo);
    }

    /**
     * 根据动态参数获取人员信息管理列表
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:18
     * @Param
     **/
    @Override
    public List<DlsInfo> listDlsInfoByParams(Map<String, Object> params) {
        return dlsInfoMapper.listDlsInfoByParams(params);
    }

    /**
     * 根据人员信息管理pojo获取人员信息管理列表
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:18
     * @Param
     **/
    @Override
    public List<DlsInfo> listDlsInfoByModel(DlsInfo DlsInfo) {
        // 获取时默认获取有效数据
        DlsInfo.setDataStatus(GlobalConstant.DATA_VALID);
        return dlsInfoMapper.selectDlsInfoList(DlsInfo);
    }

    /*@Override
    public List<DlsInfoSelect> selectList(Map<String, Object> params) {
        return dlsInfoMapper.selectList(params);
    }*/
}
