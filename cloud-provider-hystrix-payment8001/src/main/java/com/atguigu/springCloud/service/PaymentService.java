package com.atguigu.springCloud.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/12/16 11:28
 */
@Service
public class PaymentService {
    /**
     * 正常访问 OK
     * @param id
     * @return
     */
    public String paymentInfo_OK(Integer id){
        return "线程池:"+Thread.currentThread().getName()+"paymentInfo_OK,id"+id+"\t"+"o(^_^)哈哈";
    }

    public String paymentInfo_TimeOut(Integer id){
        int timeNumber=3;
        try{
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池:"+Thread.currentThread().getName()+"paymentInfo_TimeOut,id"+id+"\t"+"o(^_^)哈哈"+"耗时3秒";
    }
}
