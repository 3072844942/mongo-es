package org.self.strategy;

import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 即时同步策略
 *
 * @author march
 * @since 2023/6/21 上午11:12
 */
@Log4j2
@Service
public class ImmediateSyncStrategy {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public void save(Document document, String annotation) {
        // 隐去_class, _id
        Map<String, Object> m = new HashMap<>();
        document.forEach((key, value) -> {
            if (key.equals("_id")) {
                if (value.getClass().equals(ObjectId.class)) {
                    m.put("id", ((ObjectId) value).toString());
                } else {
                    m.put("id", value.toString());
                }
            } else if (!key.startsWith("_")) {
                m.put(key, value);
            }
        });

        elasticsearchOperations.save(org.springframework.data.elasticsearch.core.document.Document.from(m),
                IndexCoordinates.of(annotation));
        log.info("sync object: " + m);
    }


    public void delete(String id, String collectionName) {
        elasticsearchOperations.delete(id, IndexCoordinates.of(collectionName));
        log.info("sync delete: " + id);
    }
}
