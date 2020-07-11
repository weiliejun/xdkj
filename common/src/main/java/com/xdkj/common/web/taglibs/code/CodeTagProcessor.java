package com.xdkj.common.web.taglibs.code;

import com.xdkj.common.model.sysdictionary.bean.SysDictionary;
import com.xdkj.common.model.sysdictionary.dao.SysDictionaryDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Iterator;
import java.util.List;

public class CodeTagProcessor extends AbstractElementTagProcessor {

    private static final String TAG_NAME = "code";//标签名
    private static final String ATTR_PARENT_CODE = "parentCode";//属性（码表编码）
    private static final String ATTR_PROPERTY = "property";//属性名称
    private static final String ATTR_TYPE = "type";//类型
    private static final String ATTR_VALUE = "value";//码表编码名称
    private static final String ATTR_DEFAULT_VALUE = "defaultValue";//默认值
    private static final String ATTR_LAY_VERIFY = "lay-verify";//校验

    private static final int PROCESSOR_PRECEDENCE = 10000;


    public CodeTagProcessor(String dialectPrefix) {
        super(
                TemplateMode.HTML,  // 此处理器将仅应用于HTML模式
                dialectPrefix,  // 要应用于名称的匹配前缀
                TAG_NAME,   // 标签名称：匹配此名称的特定标签
                true,   // 将标签前缀应用于标签名称
                null,   // 无属性名称：将通过标签名称匹配
                false,  // 没有要应用于属性名称的前缀
                PROCESSOR_PRECEDENCE);    // 优先(内部方言自己的优先)

    }


    @Override
    protected void doProcess(
            final ITemplateContext context,
            final IProcessableElementTag tag,
            final IElementTagStructureHandler structureHandler) {
        //获取应用程序上下文
        ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
        SysDictionaryDao sysDictionaryDao = (SysDictionaryDao) appCtx.getBean("sysDictionaryDao");

        //从标签读取属性
        final String parentCode = tag.getAttributeValue(ATTR_PARENT_CODE);
        final String property = tag.getAttributeValue(ATTR_PROPERTY);
        final String type = tag.getAttributeValue(ATTR_TYPE);
        final String value = tag.getAttributeValue(ATTR_VALUE);
        final String defaultValue = tag.getAttributeValue(ATTR_DEFAULT_VALUE);

        String showText = "";
        if (type.equals("text")) {
            SysDictionary sysDictionary = sysDictionaryDao.getSysDictionaryByCode(parentCode, value);
            if (sysDictionary != null) {
                showText = sysDictionary.getName();
            }
        } else if (type.equals("radio")) {
            showText = getRadioTypeContent(sysDictionaryDao, tag);
        } else if (type.equals("checkbox")) {
            showText = getCheckboxTypeContent(sysDictionaryDao, tag);
        } else if (type.equals("select")) {
            showText = getSelectTypeContent(sysDictionaryDao, tag);
        } else {

        }
        /*
         *  创建将替换自定义标签的DOM结构。
         * logo将显示在“<div>”标签内, 因此必须首先创建,
         * 然后必须向其中添加一个节点。
         */
        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();
        model.add(modelFactory.createText(showText));
        //指示引擎用指定的模型替换整个元素
        structureHandler.replaceWith(model, false);

    }

    private String getSelectTypeContent(SysDictionaryDao sysDictionaryDao, IProcessableElementTag tag) {
        String content = "";
        //从标签读取属性
        final String parentCode = tag.getAttributeValue(ATTR_PARENT_CODE);
        final String property = tag.getAttributeValue(ATTR_PROPERTY);
        final String type = tag.getAttributeValue(ATTR_TYPE);
        final String value = tag.getAttributeValue(ATTR_VALUE);
        final String defaultValue = tag.getAttributeValue(ATTR_DEFAULT_VALUE);
        final String layVerify = tag.getAttributeValue(ATTR_LAY_VERIFY);
        List<SysDictionary> sysDictionarys = sysDictionaryDao.getSysDictionarysByParentCode(parentCode);
        if (sysDictionarys != null && sysDictionarys.size() > 0) {
            String selVerify = "";
            if (StringUtils.isNotBlank(layVerify)) {
                selVerify = " lay-verify='required'";
            }
            content = content + "<select  id='" + property + "' name='" + property + "' lay-filter='" + property + "' class='form-control'" + selVerify + ">";
            String selected = "";
            if (StringUtils.isBlank(value) && StringUtils.isBlank(defaultValue)) {
                selected = "selected='selected'";
            }
            content = content + "<option value='' " + selected + ">请选择</option>";
            Iterator<SysDictionary> it = sysDictionarys.iterator();
            while (it.hasNext()) {
                SysDictionary sysDictionary = (SysDictionary) it.next();
                String key = sysDictionary.getCode();
                String label = sysDictionary.getName();
                selected = "";
                if (StringUtils.isBlank(value)) {
                    if (defaultValue != null && defaultValue.equals(key)) {
                        selected = "selected='selected'";
                    }
                } else if (StringUtils.isNotBlank(value) && value.equals(key)) {
                    selected = "selected='selected'";
                }
                content = content + "<option value='" + key + "' " + selected + ">" + label + "</option>";
            }
            content = content + "</select>";
        }
        return content;
    }

    private String getRadioTypeContent(SysDictionaryDao sysDictionaryDao, IProcessableElementTag tag) {
        String content = "";
        //从标签读取属性
        final String parentCode = tag.getAttributeValue(ATTR_PARENT_CODE);
        final String property = tag.getAttributeValue(ATTR_PROPERTY);
        final String type = tag.getAttributeValue(ATTR_TYPE);
        final String value = tag.getAttributeValue(ATTR_VALUE);
        final String defaultValue = tag.getAttributeValue(ATTR_DEFAULT_VALUE);
        final String layVerify = tag.getAttributeValue(ATTR_LAY_VERIFY);
        List<SysDictionary> sysDictionarys = sysDictionaryDao.getSysDictionarysByParentCode(parentCode);
        if (sysDictionarys != null && sysDictionarys.size() > 0) {
            String selVerify = "";
            if (StringUtils.isNotBlank(layVerify)) {
                selVerify = " lay-verify='required'";
            }
            String checked = "";
            if (StringUtils.isBlank(value) && StringUtils.isBlank(defaultValue)) {
                checked = "checked='checked'";
            }
            Iterator<SysDictionary> it = sysDictionarys.iterator();
            while (it.hasNext()) {
                SysDictionary sysDictionary = it.next();
                String key = sysDictionary.getCode();
                String label = sysDictionary.getName();
                checked = "";
                if (StringUtils.isBlank(value)) {
                    if (defaultValue != null && defaultValue.equals(key)) {
                        checked = "checked='checked'";
                    }
                } else if (StringUtils.isNotBlank(value) && value.equals(key)) {
                    checked = "checked='checked'";
                }
                content = content + "<label class='radio-inline'><input type='radio' " + checked + " id='" + property + "' name='" + property + "' value='" + key + "' />" + label + "</label>";
            }
        }
        return content;
    }

    private String getCheckboxTypeContent(SysDictionaryDao sysDictionaryDao, IProcessableElementTag tag) {
        String content = "";
        //从标签读取属性
        final String parentCode = tag.getAttributeValue(ATTR_PARENT_CODE);
        final String property = tag.getAttributeValue(ATTR_PROPERTY);
        final String type = tag.getAttributeValue(ATTR_TYPE);
        final String value = tag.getAttributeValue(ATTR_VALUE);
        final String defaultValue = tag.getAttributeValue(ATTR_DEFAULT_VALUE);
        final String layVerify = tag.getAttributeValue(ATTR_LAY_VERIFY);
        List<SysDictionary> sysDictionarys = sysDictionaryDao.getSysDictionarysByParentCode(parentCode);
        if (sysDictionarys != null && sysDictionarys.size() > 0) {
            String selVerify = "";
            if (StringUtils.isNotBlank(layVerify)) {
                selVerify = " lay-verify='required'";
            }
            String checked = "";
            if (StringUtils.isBlank(value) && StringUtils.isBlank(defaultValue)) {
                checked = "checked='checked'";
            }
            Iterator<SysDictionary> it = sysDictionarys.iterator();
            while (it.hasNext()) {
                SysDictionary sysDictionary = it.next();
                String key = sysDictionary.getCode();
                String label = sysDictionary.getName();
                checked = "";
                if (StringUtils.isBlank(value)) {
                    if (defaultValue != null && defaultValue.equals(key)) {
                        checked = "checked='checked'";
                    }
                } else if (StringUtils.isNotBlank(value) && value.equals(key)) {
                    checked = "checked='checked'";
                }
                content = content + "<label class='checkbox-inline'><input type='checkbox' " + checked + " id='" + property + "' name='" + property + "' value='" + key + "' />" + label + "</label>";
            }
        }
        return content;
    }
}
