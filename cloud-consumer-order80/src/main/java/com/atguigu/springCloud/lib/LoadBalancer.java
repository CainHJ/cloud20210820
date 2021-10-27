package com.atguigu.springCloud.lib;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/10/24 16:13
 */
public interface LoadBalancer {
    ServiceInstance instances(List<ServiceInstance>serviceInstances);
}
