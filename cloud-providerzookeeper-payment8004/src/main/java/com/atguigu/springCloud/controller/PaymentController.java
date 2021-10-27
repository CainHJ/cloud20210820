package com.atguigu.springCloud.controller;


import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/30 17:16
 */
@RestController//这个的注解能发送json
@Slf4j//这个注解能直接用log日志
public class PaymentController {

    @Resource
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String serverPort;


    @GetMapping(value = "/payment/zk")
    public String paymentZk(){
        System.out.println("11");
        return "springCloud with zookeeper:"+serverPort+"\t"+ UUID.randomUUID().toString();
    }


}
