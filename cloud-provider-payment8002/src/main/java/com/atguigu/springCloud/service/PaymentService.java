package com.atguigu.springCloud.service;

import com.atguigu.springCloud.entities.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/30 17:05
 */
public interface PaymentService {
    public int create(Payment payment);
    public Payment getPaymentById(@Param("id") Long id);
}
