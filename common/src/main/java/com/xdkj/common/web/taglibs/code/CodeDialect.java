package com.xdkj.common.web.taglibs.code;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashSet;
import java.util.Set;

public class CodeDialect extends AbstractProcessorDialect {

    //定义方言名称
    private static final String DIALECT_NAME = "Dictionary";
    private static final String PREFIX = "itech";

    public CodeDialect() {
        /**
         * 我们将设置此方言与“方言处理器”优先级相同
         * 标准方言, 以便处理器执行交错
         */
        super(DIALECT_NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
    }

    /**
     * 元素处理器
     */
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new CodeTagProcessor(dialectPrefix));//添加我们定义的标签
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
        return processors;
    }
}
