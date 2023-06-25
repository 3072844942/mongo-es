package org.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;

/**
 * changeStream监听配置
 */
@Configuration
public class ChangeStream implements CommandLineRunner {
    @Value("${spring.data.mongodb.authentication-database}")
    private String database;
    @Autowired
    private MongoMessageListener mongoMessageListener;
    @Autowired
    private MessageListenerContainer messageListenerContainer;

    @Override
    public void run(String... args) {
        ChangeStreamRequest<Object> request = ChangeStreamRequest.builder(mongoMessageListener)
                .database(database)
                .build();
        messageListenerContainer.register(request, Object.class);
    }
}