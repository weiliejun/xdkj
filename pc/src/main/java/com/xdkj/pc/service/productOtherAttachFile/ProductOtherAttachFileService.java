package com.xdkj.pc.service.productOtherAttachFile;

import com.xdkj.common.model.productOtherAttachFile.bean.ProductOtherAttachFile;

import java.util.List;
import java.util.Map;

public interface ProductOtherAttachFileService {
    /**
     * @Description 动态参数添加数据
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    ProductOtherAttachFile addProductOtherAttachFile(ProductOtherAttachFile record);


    /**
     * @Description 根据Id查找产品其他附件信息
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    ProductOtherAttachFile selectProductOtherAttachFileById(String id);

    /**
     * @Description 根据产品Id查找产品其他附件信息
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    ProductOtherAttachFile selectProductOtherAttachFileByProductId(String productId);

    /**
     * @Description 获取产品其他附件信息列表
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    List<ProductOtherAttachFile> selectProductOtherAttachFileList(ProductOtherAttachFile record);

    /**
     * @Description 根据id修改产品其他附件信息
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    int updateProductOtherAttachFileById(ProductOtherAttachFile record);


    /**
     * @Description 修改产品其他附件信息有效性
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    int updateDataStatusById(ProductOtherAttachFile record);

    public List<ProductOtherAttachFile> listWdmbByParams(Map<String, Object> params);
}
