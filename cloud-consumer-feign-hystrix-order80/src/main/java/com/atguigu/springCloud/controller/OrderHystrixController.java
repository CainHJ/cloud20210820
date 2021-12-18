package com.atguigu.springCloud.controller;

import com.atguigu.springCloud.service.PaymentHysitrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/12/18 15:41
 */
@Slf4j
@RestController
public class OrderHystrixController {
    @Resource
    private PaymentHysitrixService paymentHysitrixService;

    @GetMapping("/consumer/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        return paymentHysitrixService.paymentInfo_OK(id);
    }

    @GetMapping("/consumer/hystrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id){
        return paymentHysitrixService.paymentInfo_TimeOut(id);
    }

}
