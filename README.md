## 系统说明

- 基于 Spring Cloud 2021 、Spring Boot 2.7、 OAuth2 的 RBAC **权限管理系统**
- 基于数据驱动视图的理念封装 element-ui，即使没有 vue 的使用经验也能快速上手
- 提供对常见容器化支持 Docker、Kubernetes、Rancher2 支持
- 提供 lambda 、stream api 、webflux 的生产实践

## 快速开始

### 核心依赖

| 依赖                   | 版本         |
| ---------------------- |------------|
| Spring Boot            | 2.7.1      |
| Spring Cloud           | 2021.0.3   |
| Spring Cloud Alibaba   | 2021.0.1.0 |
| Spring Authorization Server | 0.3.1      |
| Mybatis Plus           | 3.5.2      |
| hutool                 | 5.8.4      |
| Avue                   | 2.6.18     |

### 模块说明

```lua
project-ui  -- https://gitee.com/log4j/ui

project
├── auth -- 授权服务提供[3000]
└── common -- 系统公共模块
     ├── common-bom -- 全局依赖管理控制
     ├── common-core -- 公共工具类核心包
     ├── common-datasource -- 动态数据源包
     ├── common-job -- xxl-job 封装
     ├── common-log -- 日志服务
     ├── common-mybatis -- mybatis 扩展封装
     ├── common-seata -- 分布式事务
     ├── common-security -- 安全工具类
     ├── common-swagger -- 接口文档
     └── common-feign -- feign 扩展封装
├── register -- Nacos Server[8848]
├── gateway -- Spring Cloud Gateway网关[9999]
└── upms -- 通用用户权限管理模块
     └── upms-api -- 通用用户权限管理系统公共api模块
     └── upms-biz -- 通用用户权限管理系统业务处理模块[4000]
└── visual
     └── monitor -- 服务监控 [5001]
     ├── codegen -- 图形化代码生成 [5002]
     ├── sentinel-dashboard -- 流量高可用 [5003]
     └── xxl-job-admin -- 分布式定时任务管理台 [5004]
```

### 本地开发 运行

导入本地nacos数据
启动nacos,mysql,redis等软件
启动gateway,upms,auth测试

### 接口防重放、防篡改
```lua
1.业务模块添加依赖
<dependency>
    <groupId>com.pig4cloud.plugin</groupId>
    <artifactId>anti-replay-spring-boot-starter</artifactId>
    <version>0.0.2</version>
</dependency>
2.配置参数
anti:
  replay:
    signature-algorithm:
      salt: 67236618ad696de2a91700b1afda43d0 #盐值 加密生成signature时用到
    request:
      expire-time: 10   #请求过期时间
    cache:
      cache-key-prefix: NARUTO:SECURITY:ANTI-REPLAY:REQUEST_ID_
      lock-hold-time: 30  #锁释放时间
    header-key:
      signature: mySignature # 与请求头参数名对应
      nonce: mtNonce # 与请求头参数名对应
      timestamp: myTimestamp # 与请求头参数名对应
      url: myUrl # 与请求头参数名对应
      token: myToken # 与请求头参数名对应
3.方法添加@NarutoAntiReplay注解，启动服务使用postman进行测试。注意请求头需要携带的参数。其中signature的值,为请求配置文件中配置的salt值+nonce+url+timestamp+token进行md5加密后的值。注意，如果方法带有参数，对参数也应该加密。具体逻辑可以查看MdFiveUtils的digest方法。md5加密,pig-ui可以使用CryptoJS.MD5进行加密。只有通过验证，才可访问方法。
```
