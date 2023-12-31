package org.self.context;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.bson.BsonValue;
import org.bson.Document;
import org.self.SyncConfig;
import org.self.enums.SynchronizeModeEnum;
import org.self.strategy.AsyncDelayedSyncStrategy;
import org.self.strategy.ImmediateSyncStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author march
 * @since 2023/6/26 下午4:04
 */
@Service
@Log4j2
public class SyncStrategyContext {
    private final ImmediateSyncStrategy is;
    private final AsyncDelayedSyncStrategy ads;
    private final SyncConfig syncConfig;
    private final ArrayBlockingQueue<ChangeStreamDocument<Document>> queue;

    public SyncStrategyContext(ImmediateSyncStrategy is, AsyncDelayedSyncStrategy ads, SyncConfig syncConfig) {
        this.is = is;
        this.ads = ads;
        this.syncConfig = syncConfig;
        this.queue = new ArrayBlockingQueue<>((int) (syncConfig.getCapacity() * 1.1));
    }

    public void submit(ChangeStreamDocument<Document> raw) throws InterruptedException {
        if (SynchronizeModeEnum.getStrategy(syncConfig.getMode()).equals(SynchronizeModeEnum.ADS)) { // 定量
            queue.put(raw);

            if (queue.size() >= syncConfig.getCapacity()) {
                ArrayList<ChangeStreamDocument<Document>> arrayList = new ArrayList<>(queue.size() + 10);
                queue.drainTo(arrayList);
                ads.run(arrayList);
            }
        } else if (SynchronizeModeEnum.getStrategy(syncConfig.getMode()).equals(SynchronizeModeEnum.SS)) { // 定时
            queue.put(raw);
        } else { // 立即
            OperationType operationType = raw.getOperationType();
            if (operationType.equals(OperationType.DELETE)) {
                BsonValue bsonValue = raw.getDocumentKey().get("_id");
                String id;
                if (bsonValue.isObjectId()) id = bsonValue.asObjectId().toString();
                else id = bsonValue.asString().getValue();

                is.delete(id, raw.getNamespace().getCollectionName());
            } else if (operationType.equals(OperationType.INSERT)
                    || operationType.equals(OperationType.REPLACE)
                    || operationType.equals(OperationType.UPDATE)
            ) {
                is.save(raw.getFullDocument(), raw.getNamespace().getCollectionName());
            }
        }
    }

    @PostConstruct
    private void init() {
        if (SynchronizeModeEnum.getStrategy(syncConfig.getMode()).equals(SynchronizeModeEnum.SS)) { // 定时
            // 开启另一个线程定时任务
            Thread thread = new Thread(() -> {
                while (true) {
                    ArrayList<ChangeStreamDocument<Document>> arrayList = new ArrayList<>(queue.size() + 10);
                    queue.drainTo(arrayList);
                    ads.run(arrayList);

                    try {
                        Thread.sleep(syncConfig.getTimeout());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        }

        log.info("Preparing for data synchronization...");
        log.info("Sync type: " + syncConfig.getMode());
        if (SynchronizeModeEnum.getStrategy(syncConfig.getMode()).equals(SynchronizeModeEnum.SS)) {
            log.info("Sync interval: " + syncConfig.getTimeout() + "ms");
        }
        if (SynchronizeModeEnum.getStrategy(syncConfig.getMode()).equals(SynchronizeModeEnum.ADS)) {
            log.info("Sync number:" + syncConfig.getTimeout());
        }
    }
}
