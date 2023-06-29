package org.self;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.self.context.SyncStrategyContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer;
import org.springframework.data.mongodb.core.messaging.Message;
import org.springframework.data.mongodb.core.messaging.MessageListener;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class MongoMessageListener implements MessageListener<ChangeStreamDocument<Document>, Object> {
    private final SyncStrategyContext strategyContext;

    public MongoMessageListener(SyncStrategyContext strategyContext) {
        this.strategyContext = strategyContext;
    }

    @Bean
    MessageListenerContainer messageListenerContainer(MongoTemplate mongoTemplate) {
        Executor executor = new ThreadPoolExecutor(0, 5, 10L, TimeUnit.MINUTES, new ArrayBlockingQueue(100), new ThreadPoolExecutor.CallerRunsPolicy());
        return new DefaultMessageListenerContainer(mongoTemplate, executor) {
            public boolean isAutoStartup() {
                return true;
            }
        };
    }

    @Override
    public void onMessage(Message<ChangeStreamDocument<Document>, Object> message) {
        ChangeStreamDocument<Document> raw = message.getRaw();

        // 如果需要同步当前表
        if (CollectionScanner.collections.contains(raw.getNamespace().getCollectionName())) {
            try {
                strategyContext.submit(raw);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}