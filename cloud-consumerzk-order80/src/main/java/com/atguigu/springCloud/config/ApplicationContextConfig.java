package com.atguigu.springCloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/10/14 18:24
 */
//
@Configuration
public class ApplicationContextConfig {
    @Bean
    @LoadBalanced //为了走负载均衡




    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
