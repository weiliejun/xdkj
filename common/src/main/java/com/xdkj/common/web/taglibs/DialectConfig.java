package com.xdkj.common.web.taglibs;


import com.xdkj.common.web.taglibs.code.CodeDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DialectConfig {

    @Bean
    public CodeDialect registerCodeDialect() {
        return new CodeDialect();
    }

}