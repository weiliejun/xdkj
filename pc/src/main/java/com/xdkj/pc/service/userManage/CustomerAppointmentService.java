package com.xdkj.pc.service.userManage;


import com.xdkj.common.model.customerAppointment.bean.CustomerAppointment;

import java.util.Map;

public interface CustomerAppointmentService {
    CustomerAppointment addCustomerAppointment(CustomerAppointment customerAppointment);

    void updateCustomerAppointment(CustomerAppointment customerAppointment);

    CustomerAppointment getCustomerAppointmentById(Integer id);

    int countCustomerAppointmentsByParams(Map<String, Object> params);

}