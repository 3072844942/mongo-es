## 致力于开发一个SpringBoot3的 数据库同步组件

### 介绍
自动化同步MongoDB到ElasticSearch

### 使用方法
1. 书写配置类， MongoMessageListener, MessageListenerContainer可被重写， 当然也可以使用默认
```java
@Configuration
@ComponentScan("org.self")
public class MongoESConfig implements CommandLineRunner {
    @Value("${spring.data.mongodb.authentication-database}")
    private String database;
    @Autowired
    private MongoMessageListener mongoMessageListener;
    @Autowired
    private MessageListenerContainer messageListenerContainer;

    public void run(String... args) {
        ChangeStreamRequest<Object> request = ChangeStreamRequest.builder(mongoMessageListener).database(database).build();
        messageListenerContainer.register(request, Object.class);
    }
}
```
2. 书写配置文件
```yaml
spring:
  mes:
    collections: example.collection1,example.collection2
    mode: IS
    capacity: 10000
    timeout: 1000
```

### 创建思路
1. 监听MongoDB（副本集模式）的ChangeStream
2. 根据策略完成同步
3. FullDocument 或者 DocumentKey

### 附录
#### 同步策略
| 同步策略 |   描述   |
|:----:|:------:|
|  IS  |  立即同步  |
| ADS  | 异步定量同步 |
|  SS  |  定时同步  |

#### 配置描述
|     配置      |    描述    | 默认值  |
|:-----------:|:--------:|:----:|
|    mode     |    模式    |  IS  |
|  capacity   |   最大容量   | 1000 |
|   timeout   | 时间间隔（ms） | 1000 |
| collections |   同步集合   |  *   |

#### 执行时刻

|         机制	         |                    描述	                    |       适用对象        |            	执行顺序            |
|:-------------------:|:-----------------------------------------:|:-----------------:|:---------------------------:|
|  CommandLineRunner  |         	在应用程序启动时执行特定代码，可访问命令行参数	         | Spring Boot bean	 |     容器控制，通常在所有 bean 之后      |
|  ApplicationRunner  |         	在应用程序启动时执行特定代码，可访问应用程序参数         | 	Spring Boot bean |     	容器控制，通常在所有 bean 之后     |
|  InitializingBean   |         	在 bean 的属性设置完成后执行初始化逻辑	          |    Spring bean    | 	容器控制，通常在 @PostConstruct 之前 |
|   DisposableBean	   |            在 bean 销毁之前执行清理逻辑	             |   Spring bean	    |        容器控制，通常在销毁之前         |
|   @PostConstruct	   |   在 bean 的构造函数执行之后、依赖注入完成之后执行指定方法，用于初始化   |   	Spring bean    |    	不确定，只保证在 bean 初始化完成后    |
|    @PreDestroy	     |         在 bean 销毁之前执行指定方法，用于清理资源	         |    Spring bean    |        	不确定，只保证在销毁之前        |
|   SmartLifecycle	   | 在容器启动和关闭过程中执行特定逻辑，可以控制启动和停止的顺序，支持懒加载和自动启动 |   	Spring bean    |    	容器控制，可根据 phase 控制顺序     |
| ApplicationListener |             	监听应用程序事件并执行相应的逻辑             |   	Spring bean    |       	容器控制，根据事件触发顺序        |
