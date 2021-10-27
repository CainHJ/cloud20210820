package com.atguigu.springCloud.lib;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/10/24 16:19
 */
@Component
public class MyLB implements LoadBalancer {

    private AtomicInteger atomicInteger=new AtomicInteger(0);


    public final int getAndIncrement(){
        int current;
        int next;
        do{
            current=this.atomicInteger.get();
            System.out.println("current1-->"+current);
            next=current>=Integer.MAX_VALUE?0:current+1;
            System.out.println("current2-->"+current);
            System.out.println("next-->"+next);
        }while(!this.atomicInteger.compareAndSet(current,next));
        System.out.println("****第几次访问***next:"+next);
        return next;
    }


    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        int index=getAndIncrement() %serviceInstances.size();
        return serviceInstances.get(index);
    }
}
