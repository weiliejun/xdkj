package com.xdkj.pc.service.dlsInfo;


import com.xdkj.common.model.dlsInfo.bean.DlsInfo;

import java.util.List;
import java.util.Map;

public interface DlsInfoService {
    DlsInfo addDlsInfo(DlsInfo DlsInfo);

    DlsInfo getDlsInfoById(String id);

    DlsInfo getDlsInfoByName(String name);

    void updateDlsInfo(DlsInfo DlsInfo);

    List<DlsInfo> listDlsInfoByParams(Map<String, Object> params);

    List<DlsInfo> listDlsInfoByModel(DlsInfo DlsInfo);

//    List<DlsInfoSelect> selectList(Map<String, Object> params);

}