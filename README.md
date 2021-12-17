#H1
######H6
`高亮`
* 无需列表1
* 无需列表2
  * 无需列表2.1
    * 无需列表2.1
>缩进1
>>缩进2

# cloud20210820
##springcloud学习
`gongliang`
>1
>>2
```static```
**加重**
> 应用

#2021-11-10
###OpenFeign
* 与Ribbon+RestTemplate作用类似
* 但是统一封装 这样每次都写Ribbon+RestTemplate,这样优雅简单
```xml
 <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
 </dependency>
```
> openFeign使用步骤
>> 接口+注解
* `微服务调用接口+@FeignClient`
>>新建cloud-consumer-feign-order80
* `Feign在消费端使用`
>> pom
```xml
<dependencies>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>com.atguigu.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>

        <!--热部署-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <!--            <scope>runtime</scope>-->
            <optional>true</optional>
        </dependency>
    </dependencies>
```
* `能看到`
![Image text](image/16365266621.png?raw=true)

>> yml
```yaml
## 端口号
server:
  port: 80
##此处只当它是客户端,不当微服务(这个无所谓相加自己可以加)
eureka:
  client:
    #false 表示不向注册中心注册
    register-with-eureka: true
    #false 表示自己端就是注册中心，我的职责就是维护，并不需要检查
    fetch-registry: true
    service-url:
      #设置与eureka server交互的地址查询服务和注册服务需要依赖的这个地址
      #defaultZone: http://localhost:7001/eureka/
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/ #集群
```
>> 主启动
```java
@SpringBootApplication
@EnableFeignClients//激活feign
public class OrderFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain80.class,args);
    }
}
```
>> 业务逻辑
>>> 业务逻辑接口+`@FeignClient配置调用provider服务`
>>> `新建PaymentFeignService接口`并新增注解@FeignClient
```java
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")//知道eruaka服务方的ip
public interface PaymentFeignService {
    @GetMapping(value = "/payment/get/{id}")//知道eureka服务方的上下文地址
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);
}
```
>>> 控制层Controller
```java
@RestController
@Slf4j
public class OrderFeignController {
    @Resource
    private PaymentFeignService paymentFeignService;
    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment>getPaymentById(@PathVariable("id") Long id){
        return paymentFeignService.getPaymentById(id);
    }
}
```

#2021-11-22
> OpenFeign的超时控制
* 超时设置,故意设置超时演示情况出错
>> 服务提供方8001故意写暂停程序
>>> 在8001的`PaymentController` 加入方法
```java_holder_method_tree
    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            //直接阻塞3分钟
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }
```
>> 服务消费方80添加超时方法`PaymentFeignService`
```java_holder_method_tree
    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout();
```
>> 服务消费方80添加超时方法`OrderFeignController`
```java_holder_method_tree
    @GetMapping(value = "/consumer/payment/feign/timeout")
    public String paymentFeignTimeout(){
        //openfein-ribbon 客户端一般默认等待1秒钟 就是说三秒是会出问题的
        return paymentFeignService.paymentFeignTimeout();
    }
```
>> 测试
>>> 测试之前 只开8001 因为8002没有这个方法
![Image text](image/1637571277.png?raw=true)

>>> http://localhost/consumer/payment/feign/timeout

>>> 错误页面
* `openfeign`默认是1秒返回,但是逻辑执行走了3秒,超时报错
![Image text](image/1637571957.png?raw=true)
>>> `解决方法:` `YML`文件需要开启`OpenFeign`客户端超时控制
```yaml
##设置feign客户端超时时间(OpenFeign默认支持ribbon)
ribbon:
  #指的建立连接所用的时间,适用于网络状况正常的情况下,两端连接所用时间
  ##这个注释掉 报错
  ReadTimeout: 5000
  #指的建立连接后,从服务器读取到可用资源所用的时间  
  ##这个注释掉 不报错
  ConnectTimeout: 5000
  ##这两个的用法不是很清楚
  ##重要的是他们居然不能默认打出 我真的很无语
```
>> OpenFeign日志打印功能
>>> 日志打印功能
* `Feign`提供了日志打印功能,我们可以通过配置调整日志级别,从而了解`Feign`中`http`请求的细节
* `对Feign接口的调用情况进行监控输出`
>>> 日志级别
* `NONE:` 默认的,不显示任何日志
* `BASIC:` 仅记录请求方法 URL 响应状态码及执行时间
* `HEADERS:` 除了`BASIC`中定义的信息之外,还有请求响应的头信息
* `FULL:` 除了`HEADERS`中定义的信息之外,还有请求和响应的正文及元数据

>>> 配置日志bean
* `feign80`中加配置`bean`
```java
package com.atguigu.springCloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author HJ
 * @version 1.0
 * @date 2021/11/22 18:52
 */
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

}
```
>>> YML文件里需要开启日志的Fegin客户端
* `feign80`中的`yml`文件加
```yaml
logging:
  level:
    com.atguigu.springCloud.service.PaymentFeignService: debug
```
>>> 后台日志查看

#2021-11-23
> Hystrix(断路器)
* 出道即巅峰
* `resilience4j` 与 `sentinel`都有抄过它的的思想
* 现在已经停更
* `resilience4j`是`Hystrix`停更后,官网推荐,国外很多使用
* `sentinel`国内一般用这个替换
* `Hystrix`用于处理分布式系统`延迟`和`容错`的开源库,在一个依赖出问题的时候,`不会导致整体服务失败,避免级联故障,以提高分布式系统的弹性.`
* `断路器`顾名思义就是熔断保险丝,故障时`向调用方返回一个符合预期的,可处理的备选响应(FallBack),而不是长时间的等待或者抛出调用方无法处理的异常`,这样线程不会被长时间,不必要的占用,避免了故障在分布式系统中蔓延,直至雪崩.
* `功能` 服务熔断,服务降级,接近实时的监控等等(限流,隔离)
>> Hystrix停更进维
* 官网资料 `https://github.com/Netflix/Hystrix/wiki/How-To-Use`

`停更了`
![Image text](image/1637634946.png?raw=true)
>> Hystrix概念
* `fallback:` 降级(就是不可用,我们的处理方案,如程序运行异常,超时,服务熔断触发服务降级,线程池/信号量打满也会导致服务降级,友好一些)
* `break:` 熔断(比如保险是达到最大服务访问后,直接拒绝访问,拉闸限电,然后调用服务降级得方法并返回友好提示,功能类似保险丝,强硬一些)
* `flowlimit:` 限流(秒杀高并发等操作,严禁一窝蜂得过来挤,大家排队,一秒多少个,有序进行)
>> Hystrix案例
>>> 构建
* 新建`cloud-provider-hystrix-payment8001`
* POM 
```xml
 <dependencies>
        <!--hystrix-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <!--eureka client-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>com.atguigu.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>


        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!--热部署-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <!--            <scope>runtime</scope>-->
            <optional>true</optional>
        </dependency>
</dependencies>
```
* YML
```yaml
server:
  port: 8001
spring:
  application:
    name: cloud-provider-hystrix-payment
eureka:
  client:
    #false 表示不向注册中心注册
    register-with-eureka: true
    #false 表示自己端就是注册中心，我的职责就是维护，并不需要检查
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/

```
* 主启动
```java
@SpringBootApplication
@EnableEurekaClient
public class PaymentHystrixMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrixMain8001.class,args);
    }
}
```
* 业务类 
```java
package com.atguigu.springCloud.controller;

import com.atguigu.springCloud.service.PaymentService;

import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/12/16 14:53
 */
@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result =paymentService.paymentInfo_OK(id);
        log.info("****result: "+result);
        return result;
    }

    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id){
        String result =paymentService.paymentInfo_TimeOut(id);
        log.info("****result: "+result);
        return result;
    }
}
```

```java
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
```
* 正常测试
  * http://localhost:8001/payment/hystrix/timeout/1
  * http://localhost:8001/payment/hystrix/ok/1
>>> 高并发测试
* 使用软件 jmeter
![Image text](image/1639725608.png?raw=true)
![Image text](image/1639735599.png?raw=true)

>>> 故障现象和导致原因

>>> 上诉结论

>>> 如何解决?解决要求

>>> 服务降级

>>> 服务熔断

>>> 服务限流
