package com.xdkj.common.model.productOtherAttachFile.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.productOtherAttachFile.bean.ProductOtherAttachFile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductOtherAttachFileMapper extends AbstractBaseDao {
    /**
     * @Description 动态参数添加数据
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    public ProductOtherAttachFile addProductOtherAttachFile(ProductOtherAttachFile record) {
        insert("ProductOtherAttachFileMapper.addProductOtherAttachFile", record);
        return record;
    }

    /**
     * @Description 根据Id查找产品其他附件信息
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    public ProductOtherAttachFile selectProductOtherAttachFileById(String id) {
        return (ProductOtherAttachFile) queryForObject("ProductOtherAttachFileMapper.selectProductOtherAttachFileById", id);
    }

    /**
     * @Description 根据产品Id查找产品其他附件信息
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    public ProductOtherAttachFile selectProductOtherAttachFileByProductId(String productId) {
        return (ProductOtherAttachFile) queryForObject("ProductOtherAttachFileMapper.selectProductOtherAttachFileByProductId", productId);
    }

    /**
     * @Description 获取产品其他附件信息列表
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    public List<ProductOtherAttachFile> selectProductOtherAttachFileList(ProductOtherAttachFile record) {
        return queryForList("ProductOtherAttachFileMapper.selectProductOtherAttachFileList", record);
    }

    /**
     * @Description 根据id修改产品其他附件信息
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    public int updateProductOtherAttachFileById(ProductOtherAttachFile record) {
        return update("ProductOtherAttachFileMapper.updateProductOtherAttachFileById", record);
    }

    /**
     * @Description 修改产品其他附件信息有效性
     * @Author 吕剑
     * @UpdateDate 2019/07/18 10:12
     */
    public int updateDataStatusById(ProductOtherAttachFile record) {
        return update("ProductOtherAttachFileMapper.updateDataStatusById", record);
    }

    public List<ProductOtherAttachFile> listWdmbByParams(Map<String, Object> params) {
        return queryForList("ProductOtherAttachFileMapper.listWdmbByParams", params);
    }
}