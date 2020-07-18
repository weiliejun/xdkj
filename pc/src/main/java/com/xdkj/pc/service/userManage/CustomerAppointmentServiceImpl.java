package com.xdkj.pc.service.userManage;

import com.xdkj.common.model.customerAppointment.bean.CustomerAppointment;
import com.xdkj.common.model.customerAppointment.dao.CustomerAppointmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("customerAppointmentService")
public class CustomerAppointmentServiceImpl implements CustomerAppointmentService {
    @Autowired
    private CustomerAppointmentDao customerAppointmentDao;

    public CustomerAppointment addCustomerAppointment(CustomerAppointment customerAppointment) {
        return customerAppointmentDao.addCustomerAppointment(customerAppointment);
    }

    public void updateCustomerAppointment(CustomerAppointment customerAppointment) {
        customerAppointmentDao.updateCustomerAppointment(customerAppointment);
    }

    public CustomerAppointment getCustomerAppointmentById(Integer id) {
        return customerAppointmentDao.getCustomerAppointmentById(id);
    }

    public int countCustomerAppointmentsByParams(Map<String, Object> params) {
        return customerAppointmentDao.countCustomerAppointmentsByParams(params);
    }

    public List<Map<String, Object>> listCustomerAppointmentByParams(Map<String, Object> params){
        return customerAppointmentDao.listCustomerAppointmentByParams(params);
    }
}
