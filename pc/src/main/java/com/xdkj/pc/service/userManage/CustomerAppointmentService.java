package com.xdkj.pc.service.userManage;


import com.xdkj.common.model.customerAppointment.bean.CustomerAppointment;

import java.util.List;
import java.util.Map;

public interface CustomerAppointmentService {
    CustomerAppointment addCustomerAppointment(CustomerAppointment customerAppointment);

    void updateCustomerAppointment(CustomerAppointment customerAppointment);

    CustomerAppointment getCustomerAppointmentById(Integer id);

    int countCustomerAppointmentsByParams(Map<String, Object> params);

    List<Map<String, Object>> listCustomerAppointmentByParams(Map<String, Object> params);
}