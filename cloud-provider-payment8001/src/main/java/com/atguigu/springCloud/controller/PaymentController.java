package com.atguigu.springCloud.controller;


import com.atguigu.springCloud.entities.CommonResult;
import com.atguigu.springCloud.entities.Payment;
import com.atguigu.springCloud.service.PaymentService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/30 17:16
 */
@RestController//这个的注解能发送json
@Slf4j//这个注解能直接用log日志
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

    //@RequestBody
    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result=paymentService.create(payment);
        log.info("****插入结果"+result);

        if(result>0){
            return new CommonResult<Integer>(200,"插入数据库成功,serverPort:"+serverPort,result);
        }else{
            return new CommonResult(444,"插入数据库失败,serverPort:"+serverPort);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id")Long id){
        Payment result=paymentService.getPaymentById(id);
        System.out.println(id);
        System.out.println("好嗨哦!");
        log.info("****查询结果"+result);
        if(result!=null){
            return new CommonResult<Payment>(200,"查询成功,serverPort:"+serverPort,result);
        }else{
            return new CommonResult<Payment>(444,"查询失败,serverPort:"+serverPort+",没有对应记录:"+id);
        }
    }

    @GetMapping(value = "payment/discovery")
    public Object discovery(){
        List<String> services=discoveryClient.getServices();
        for(String service:services){
            log.info("****service:"+service);
        }
        List<ServiceInstance> services1=discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for(ServiceInstance service1:services1){
            log.info(service1.getServiceId()+"\t"+service1.getHost()+"\t"+service1.getPort()+"\t"+service1.getUri());
        }
        return  this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }
}
