# 项目
本项目主要是学习 spring cloud 过程的记录，简单 demo。

# 项目结构
 - spring-cloud-learn 父目录，进行依赖管理
    - producer_8000 服务提供者
    - consumer_9000 服务消费者
   
   - eureka-client eureka 客户端
   - eureka-server eureka 服务端
   - eureka-security-server 认证模块
    
# 功能实现
1. 实现了两个简单的功能模块，producer 提供服务，consumer 通过 RestTemplate 进行服务调用
2. 实现了在 eureka-server 中注册 eureka-client 服务；实现在 eureka-security-server 中注册 eureka-client 服务

# 流程
1. eureka-security-server 中注册 eureka-client 服务
   在 eureka-security-server 中主要是比普通的 eureka-server 配置了 security 信息；  
   在 eureka-security-server 中增加 WebSecurityConfig 配置；  
   在 eureka-client 添加 application-security.yml，其中配置了要注册中心地址为 eureka-security-server