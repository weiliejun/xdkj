package com.xdkj.common.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect {
    @Pointcut("execution (* com.xdkj.*.service..*.*(..))")
    public void pointCut() {
    }

    ;

    @Before(value = "pointCut()")
    public void before(JoinPoint point) {
        Object target = point.getTarget();
        String method = point.getSignature().getName();
        Class<?>[] classz = target.getClass().getInterfaces();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
                .getMethod().getParameterTypes();
        try {
            if (classz.length > 0) {
                Method m = classz[0].getMethod(method, parameterTypes);
                //接口上有标注使用哪个数据源
                if (m != null && m.isAnnotationPresent(DataSource.class)) {
                    DataSource data = m.getAnnotation(DataSource.class);
                    CustomerContextHolder.setServiceDataSource(data.value());
                } else {
                    CustomerContextHolder.setServiceDataSource(CustomerContextHolder.DATA_SOURCE_X);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After(value = "pointCut()")
    public void after(JoinPoint point) {
        CustomerContextHolder.removeServiceDataSource();
    }
}