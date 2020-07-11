package com.xdkj.common.model.dlsInfo.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.dlsInfo.bean.DlsInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DlsInfoMapper extends AbstractBaseDao {
    /**
     * @Description 动态参数添加数据
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public DlsInfo addDlsInfo(DlsInfo record) {
        insert("DlsInfoMapper.insertSelective", record);
        return record;
    }

    /**
     * @Description 根据Id查找人员信息
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public DlsInfo selectDlsInfoById(String id) {
        return (DlsInfo) queryForObject("DlsInfoMapper.selectByPrimaryKey", id);
    }

    /**
     * @Description 根据name查找人员信息
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public DlsInfo selectDlsInfoByName(String name) {
        return (DlsInfo) queryForObject("DlsInfoMapper.selectDlsInfoByName", name);
    }

    /**
     * @Description 获取人员信息列表
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public List<DlsInfo> selectDlsInfoList(DlsInfo record) {
        return queryForList("DlsInfoMapper.selectDlsInfoList", record);
    }

    /**
     * @return
     * @Author 魏列军
     * @Description //TODO 根据动态参数获取人员列表
     * @Date 2020/5/15 15:04
     * @Param
     **/
    public List<DlsInfo> listDlsInfoByParams(Map<String, Object> params) {
        return queryForList("DlsInfoMapper.listDlsInfoByParams", params);
    }

    /**
     * @Description 根据id修改人员信息
     * @Author 魏列军
     * @UpdateDate 2020/5/15 10:12
     */
    public int updateDlsInfoById(DlsInfo record) {
        return update("DlsInfoMapper.updateByPrimaryKeySelective", record);
    }

    /**
     * @return
     * @Author 魏列军
     * @Description //TODO 根据动态参数获取人员列表
     * @Date 2020/5/15 15:04
     * @Param
     **/
    /*public List<DlsInfoSelect> selectList(Map<String, Object> params) {
        return queryForList("DlsInfoMapper.selectList", params);
    }*/
}