package com.atguigu.springCloud.controller;

import com.atguigu.springCloud.entities.CommonResult;
import com.atguigu.springCloud.entities.Payment;
import com.atguigu.springCloud.lib.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/31 16:20
 */
@RestController
@Slf4j
public class OrderController {
    @Resource
    private LoadBalancer loadBalancer;

    @Resource
    private DiscoveryClient discoveryClient;

    //public static final String PAYMENT_URL="http://localhost:8001";
    public static final String PAYMENT_URL="http://CLOUD-PAYMENT-SERVICE";
   //需要调用RestTemplate
    @Resource
    private RestTemplate restTemplate;

    @PostMapping(value = "/consumer/payment/create")
    public CommonResult create(//@RequestBody //加了就可以用json
                               Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);
    }

    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id")Long id){
        System.out.println("2");
       // System.out.println(PAYMENT_URL+"/payment/get/"+id);
        CommonResult<Payment> commonResult=restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
        System.out.println(
                commonResult.toString()
        );
        return commonResult;
    }
    @GetMapping(value = "/consumer/payment/getForEntity/{id}")
    public CommonResult<Payment>getPaymentById2(@PathVariable("id")Long id){
        ResponseEntity<CommonResult>entity=restTemplate.getForEntity(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
        if (entity.getStatusCode().is2xxSuccessful()){
          return entity.getBody();
        }else {
            return new CommonResult<>(444,"操作时报");
        }
    }

    @GetMapping(value = "/consumer/payment/lb")
    public String  getPaymentByIdLB(){
        List<ServiceInstance>instances= discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if(instances==null||instances.size()<=0){
            return null;
        }

        ServiceInstance serviceInstance=loadBalancer.instances(instances);
        URI uri=serviceInstance.getUri();
        return restTemplate.getForObject(uri+"/payment/lb",String.class);

    }
}
