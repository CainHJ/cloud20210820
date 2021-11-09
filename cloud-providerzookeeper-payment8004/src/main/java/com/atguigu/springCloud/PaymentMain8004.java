package com.atguigu.springCloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/26 11:06
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentMain8004 {
    public static void main(String[] args) {


        SpringApplication.run(PaymentMain8004.class,args);

    }
}
