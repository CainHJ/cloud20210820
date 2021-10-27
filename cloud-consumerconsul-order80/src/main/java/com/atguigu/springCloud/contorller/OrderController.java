package com.atguigu.springCloud.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/10/18 10:26
 */
@RestController
@Slf4j
public class OrderController {
    //com.atguigu.springCloud.contorller.OrderController
    public static final String INVOKE_URL="http://consul-provider-payment";
    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value = "consumer/payment/consul")
    public String payMentInfo(){
        String result=restTemplate.getForObject(INVOKE_URL+"/payment/consul",String.class);
        return result;
    }
}
