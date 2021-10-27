package com.atguigu.springCloud.dao;

import com.atguigu.springCloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
//import org.springframework.stereotype.Repository;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/30 10:01
 */
@Mapper//这个注解是可以注入 示例的mapper类
//@Repository
public interface PaymentDao {
    public int create(Payment payment);
    public Payment getPaymentById(@Param("id") Long id);
}
