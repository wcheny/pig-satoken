## 系统说明

- 基于 Pig开源版 开发，移除oauth2改为SaToken，删除oauth2相关代码
- 集成积木报表

[配套文档](https://www.yuque.com/wchenyang/ah2b1g/fntnc1) 

## 快速开始

### 核心依赖

| 依赖                   | 版本         |
|----------------------|------------|
| Spring Boot          | 2.7.1      |
| Spring Cloud         | 2021.0.3   |
| Spring Cloud Alibaba | 2021.0.1.0 |
| Sa-Token             | 1.3.0      |
| Mybatis Plus         | 3.5.2      |
| hutool               | 5.8.4      |
| Avue                 | 2.6.18     |

### 模块说明

```lua
project-ui  -- https://gitee.com/log4j/ui

cloud-satoken
├── auth -- 授权服务提供[3000]
└── common -- 系统公共模块
     ├── common-bom -- 全局依赖管理控制
     ├── common-anti_replay -- 自定义防重放防篡改组件
     ├── common-core -- 公共工具类核心包
     ├── common-datasource -- 动态数据源包
     ├── common-job -- xxl-job 封装
     ├── common-log -- 日志服务
     ├── common-redis -- redis服务
     ├── common-mybatis -- mybatis 扩展封装
     ├── common-seata -- 分布式事务
     ├── common-satoken -- satoken扩展封装
     ├── common-security -- 安全工具类
     └── common-feign -- feign 扩展封装
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
1.启动本地nacos 新建命名空间Id: 802d2e82-082a-4fc6-ae84-d318e7796058<br/>
2.导入db/yifan-nacos_config.zip文件到新建的命名空间下<br/>
3.导入数据库文件 db/cloud.sql，需要提前创建好数据库<br/>
4.启动gateway,upms,auth测试

### 接口防重放、防篡改
使用方法详见：[https://www.yuque.com/pig4cloud/pig/qgriht](https://www.yuque.com/pig4cloud/pig/qgriht)
