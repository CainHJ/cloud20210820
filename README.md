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
###OpenFeign的超时控制
* 超时设置,故意设置超时演示情况出错
> 服务提供方8001故意写暂停程序
>> 在8001的`PaymentController` 加入方法
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
> 服务消费方80添加超时方法`PaymentFeignService`
```java_holder_method_tree
    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout();
```
> 服务消费方80添加超时方法`OrderFeignController`
```java_holder_method_tree
    @GetMapping(value = "/consumer/payment/feign/timeout")
    public String paymentFeignTimeout(){
        //openfein-ribbon 客户端一般默认等待1秒钟 就是说三秒是会出问题的
        return paymentFeignService.paymentFeignTimeout();
    }
```
> 测试
>>测试之前 只开8001 因为8002没有这个方法
![Image text](image/1637571277.png?raw=true)

>> http://localhost/consumer/payment/feign/timeout

>> 错误页面
* `openfeign`默认是1秒返回,但是逻辑执行走了3秒,超时报错
![Image text](image/1637571957.png?raw=true)
>>`解决方法:` `YML`文件需要开启`OpenFeign`客户端超时控制
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
