package org.self;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author march
 * @since 2023/6/25 上午10:20
 */
@Configuration
public class MongoConfig {

    @Bean
    MessageListenerContainer messageListenerContainer(MongoTemplate mongoTemplate) {
        Executor executor = Executors.newFixedThreadPool(5);
        return new DefaultMessageListenerContainer(mongoTemplate, executor) {
            @Override
            public boolean isAutoStartup() {
                return true;
            }
        };
    }
}
