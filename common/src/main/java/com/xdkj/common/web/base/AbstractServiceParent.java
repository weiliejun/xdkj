package com.xdkj.common.web.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * ===========================================================================
 * Copyright 2009 CHENGANG Corp. All Rights Reserved.
 * @version 2.0, 2009-7-26
 * @author  Jack Chen
 * ===========================================================================
 *
 */
@Service("abstractServiceParent")
public class AbstractServiceParent {

    // scale 表示表示需要精确到小数点以后几位。
    protected static final int scale = 4;
    private static final Log logger = LogFactory.getLog(AbstractServiceParent.class);

    // @Autowired
    // protected HttpSession session;
    //
    // @Autowired
    // protected HttpServletRequest request;

    public String generateOrdId() {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String uuid = format.format(new Date().getTime()) + new Double(Math.random() * 100000).intValue();
        while (uuid.length() < 22) {
            uuid = uuid + "0";
        }
        uuid = uuid.substring(2);
        return uuid;
    }


}
