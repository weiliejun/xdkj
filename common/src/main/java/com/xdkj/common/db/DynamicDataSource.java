package com.xdkj.common.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // TODO Auto-generated method stub
        String handlerString = CustomerContextHolder.getServiceDataSource();
        return handlerString;
    }


}
