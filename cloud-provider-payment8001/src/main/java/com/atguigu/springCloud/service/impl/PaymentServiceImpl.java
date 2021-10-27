package com.atguigu.springCloud.service.impl;

import com.atguigu.springCloud.dao.PaymentDao;
import com.atguigu.springCloud.entities.Payment;
import com.atguigu.springCloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/30 17:06
 */
@Service
public class PaymentServiceImpl implements PaymentService {

   @Resource
   // @Autowired  //用这个报错
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }
}
