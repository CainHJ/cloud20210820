package com.atguigu.springCloud.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/10/14 18:27
 */
@RestController
@Slf4j
public class OrderController {
    //com.atguigu.springCloud.contorller.OrderController
    public static final String INVOKE_URL="http://cloud-provider-payment";
    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value = "consumer/payment/zk")
    public String payMentInfo(){
        String result=restTemplate.getForObject(INVOKE_URL+"/payment/zk",String.class);
        return result;
    }
}
