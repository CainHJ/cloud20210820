package com.atguigu.myrule;


import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/10/18 16:42
 */
//
@Configuration
public class MySelRule {
    @Bean
    public IRule myRule(){
        return new RandomRule();
    }
}
