package com.xdkj.pc.service.productOtherAttachFile;

import com.xdkj.common.model.productOtherAttachFile.bean.ProductOtherAttachFile;
import com.xdkj.common.model.productOtherAttachFile.dao.ProductOtherAttachFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description 产品附件信息
 * @Author 吕剑
 * @UpdateDate 2019/7/31 10:19
 */
@Service
public class ProductOtherAttachFileServiceImpl implements ProductOtherAttachFileService {

    @Autowired
    private ProductOtherAttachFileMapper productOtherAttachFileMapper;


    @Transactional
    @Override
    public ProductOtherAttachFile addProductOtherAttachFile(ProductOtherAttachFile record) {
        return productOtherAttachFileMapper.addProductOtherAttachFile(record);
    }

    @Override
    public ProductOtherAttachFile selectProductOtherAttachFileById(String id) {
        return productOtherAttachFileMapper.selectProductOtherAttachFileById(id);
    }

    @Override
    public ProductOtherAttachFile selectProductOtherAttachFileByProductId(String productId) {
        return productOtherAttachFileMapper.selectProductOtherAttachFileByProductId(productId);
    }

    @Override
    public List<ProductOtherAttachFile> selectProductOtherAttachFileList(ProductOtherAttachFile record) {
        return productOtherAttachFileMapper.selectProductOtherAttachFileList(record);
    }

    @Transactional
    @Override
    public int updateProductOtherAttachFileById(ProductOtherAttachFile record) {
        return productOtherAttachFileMapper.updateProductOtherAttachFileById(record);
    }

    @Transactional
    @Override
    public int updateDataStatusById(ProductOtherAttachFile record) {
        return productOtherAttachFileMapper.updateDataStatusById(record);
    }

    @Override
    public List<ProductOtherAttachFile> listWdmbByParams(Map<String, Object> params) {
        return productOtherAttachFileMapper.listWdmbByParams(params);
    }
}
