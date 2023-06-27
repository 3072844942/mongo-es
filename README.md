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