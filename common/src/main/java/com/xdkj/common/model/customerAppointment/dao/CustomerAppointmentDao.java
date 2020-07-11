package com.xdkj.common.model.customerAppointment.dao;

import com.xdkj.common.db.AbstractBaseDao;
import com.xdkj.common.model.customerAppointment.bean.CustomerAppointment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CustomerAppointmentDao extends AbstractBaseDao {

    public CustomerAppointment addCustomerAppointment(CustomerAppointment customerAppointment) {
        return (CustomerAppointment) queryForObject("customerAppointment.addCustomerAppointment", customerAppointment);
    }

    public CustomerAppointment getCustomerAppointmentById(Integer id) {
        return (CustomerAppointment) queryForObject("customerAppointment.getCustomerAppointmentById", id);
    }

    public void updateCustomerAppointment(CustomerAppointment customerAppointment) {
        queryForObject("customerAppointment.updateCustomerAppointment", customerAppointment);
    }

    public List<Map<String, Object>> listCustomerAppointmentByParams(Map<String, Object> params) {
        return (List<Map<String, Object>>) queryForList("customerAppointment.listCustomerAppointmentByParams", params);
    }

    public int countCustomerAppointmentsByParams(Map<String, Object> params) {
        return (int) queryForObject("customerAppointment.countCustomerAppointmentsByParams", params);
    }
}