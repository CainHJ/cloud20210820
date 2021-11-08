package com.atguigu.springCloud.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/31 16:40
 */
//
@Configuration
public class  ApplicationContextConfig {

//111221

    @Bean
  //  @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
