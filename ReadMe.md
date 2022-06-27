# 项目
本项目主要是学习 spring cloud 过程的记录，简单 demo。

# 项目结构
 - spring-cloud-learn 父目录，进行依赖管理
    - producer_8000 服务提供者
    - consumer_9000 服务消费者
      
> 之后的模块不再依赖 spring-cloud-learn
- Spring cloud Eureka 服务注册和发现需要的模块：
   - eureka-client eureka 客户端
   - eureka-server eureka 服务端
   - eureka-security-server 认证模块  
- Spring cloud Ribbon 负载均衡的服务调用
   - eureka-server eureka 注册中心
   - user-service 提供具体服务
   - ribbon-service 支持负载均衡，调用 user-service 服务
- Spring cloud Hystrix && Hystrix Dashboard
    - hystrix-service 提供服务降级、请求缓存、请求合并
    - hystrix-dashboard 提供对单个实例的监控
- Open Feign 模块
    - feign-service 使用 Feign 实现负载均衡的服务调用
    
# 功能实现
1. 实现了两个简单的功能模块，producer 提供服务，consumer 通过 RestTemplate 进行服务调用
2. 实现了在 eureka-server 中注册 eureka-client 服务；实现在 eureka-security-server 中注册 eureka-client 服务
3. 实现了负载均衡功能，主要是通过 RestTemplate
4. 实现 Hystrix 的服务降级功能，请求缓存，合并请求
5. 实现 Hystrix Dashboard 监控 Hystrix-service 单个服务
6. 实现基于 Open Feign 的负载均衡服务调用，实现服务降级
7. 开启 Open Feign 的日志功能
8. 实现 Zuul 路由功能，使用 actuator 监控
9. 实现 Zuul 过滤器功能

# 流程
1. eureka-security-server 中注册 eureka-client 服务
   在 eureka-security-server 中主要是比普通的 eureka-server 配置了 security 信息；  
   在 eureka-security-server 中增加 WebSecurityConfig 配置；  
   在 eureka-client 添加 application-security.yml，其中配置了要注册中心地址为 eureka-security-server
   
2. ribbon-service 如何提供负载均衡
   1. 在 RibbonConfig 中配置 RestTemplate Bean，并添加 LoadBalanced 注解，提供负载均衡
   2. 在 Controller 中通过 RestTemplate 远程调用 user-service 中的具体服务
    
3. Hystrix 服务容错保护的实现
    1. 服务降级：当被调用的服务出现故障时，指定一个服务降级处理方法（当 user-service 出现故障时，执行了 fallbackMethod 指定的方法）
    2. 使用 ignoreException 忽略某些异常，不发生服务降级
    3. Hystrix 请求缓存
        1. 自定义 Filter，在其中初始化 HystrixRequestContext 对象
        2. 使用 @CacheResult 开启请求缓存
        3. Controller 多个相同请求，除了第一次调用了 service，后边直接使用的缓存
        4. 使用 @CacheRemove 去除请求缓存
    4. Hystrix 请求合并 使用 @HystrixCollapser
    
4. Hystrix DashBoard 使用
    1. 使用 @EnableHystrixDashboard 开启 HystrixDashboard
    2. yml 中配置 proxy-stream-allow-list
    3. 在要监控的服务（hystrix-service） 中配置 management.endpoints.web.exposure.include
    4. 调用 hystrix-service 中的服务，即可在 hystrix dashboard 中出现监控

5. Open Feign 实现负载均衡的服务调用步骤
    1. 主启动类配置 @EnableFeignClients 启用 Feign 客户端
    2. Service 接口层通过 @FeignClient(value="service-name") 绑定具体调用的服务实现
    3. 启动多个 service-name 服务，并调用 Feign-service 中的接口，即可观察到负载均衡的情况
    
6. Open Feign 实现服务降级
    1. 实现 UserFallbackService 并通过 @Component 注入
    2. UserService 接口 @Feign-Client 中配置 fallback
    3. yml 中配置 feign.circuitbreaker.enabled=true; 引入 circuitvreaker-resilience4j 依赖
    
7. Open Feign 开启日志
    1. 实现 FeignConfig
    2. 开启 logging.level.xxx.UserService: debug
    
8. Zuul 网关服务：路由
    1. 添加 zuul 依赖，  
    2. 配置文件中配置路由信息（主要是这里的配置内容）
    3. @EnableZuulProxy 开启服务

9. Zuul 过滤器功能
    自定义 filter 类继承 ZuulFilter
# 问题
1. 使用 Hystrix 合并请求时，第三次请求会触发错误，具体如下
> Failed to map all collapsed requests to response. The expected contract has not been respected. Collapser key: 'getUserFuture', requests size: '2', response size: '1'  
> https://blog.csdn.net/xiao_jun_0820/article/details/78423985

有一篇博文中提到了这个错误，但是还没太理解怎么解决

2. Open Feign 实现服务降级的问题
   
> stack overflow 原帖：https://stackoverflow.com/questions/69524571/spring-cloud-openfeign-3-0-1-fallback-not-being-triggered
    
由于 FeignService 模块使用的 Spring cloud 的版本是 2021.0.3，这个版本并不支持 hystrix，为了开启 fallback 功能，需要以下两点：
   1. Enable property: feign.circuitbreaker.enabled=true   
   2. Add dependency to Spring Cloud CircuitBreaker, which will handle configured fallbacks.
    
```yml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```

# 组件介绍
1. OpenFeign 
   OpenFeign 是一种声明式、模块化的 Http 客户端。声明式调用：就像调用本地方法一样调用远程方法（类似 RPC）。 
   使用 OpenFeign 之后，可以不使用 RestTemplate 进行调用。

2. Zuul
    Zuul 作为微服务的 API 网关（为服务提供统一的访问入口）使用，支持动态路由和过滤功能。  
   Zuul 自动集成了 Ribbon 和 Hystrix，所以 Zuul 具有负载均衡和服务容错能力
# todo list
1. 项目模块中 spring cloud 版本不统一
