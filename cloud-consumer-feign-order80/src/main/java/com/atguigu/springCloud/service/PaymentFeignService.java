package com.atguigu.springCloud.service;

import com.atguigu.springCloud.entities.CommonResult;
import com.atguigu.springCloud.entities.Payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/11/11 10:11
 */
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")//知道eruaka服务方的ip
public interface PaymentFeignService {
    @GetMapping(value = "/payment/get/{id}")//知道eureka服务方的上下文地址
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);
}
