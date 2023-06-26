package org.self.strategy;

import com.mongodb.MongoNamespace;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异步批量策略
 *
 * @author march
 * @since 2023/6/21 上午11:12
 */
@Service
public class AsyncDelayedSyncStrategy {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public void run(List<ChangeStreamDocument<Document>> list) {
        // 分批进行操作
        for (int i = 0, j = 0; i < list.size(); i = j) {
            OperationType op = list.get(i).getOperationType();
            MongoNamespace namespace = list.get(i).getNamespace();
            String fullName = namespace.getFullName();
            // 同一个集合的同一操作
            while (j < list.size()
                    && op.equals(list.get(j).getOperationType())
                    && fullName.equals(list.get(j).getNamespace().getFullName())
            ) {
                j ++;
            }
            List<ChangeStreamDocument<Document>> subList = list.subList(i, j);

            if (op.equals(OperationType.DELETE)) { // 删除必须一个一个做
                subList.forEach(raw -> {
                    BsonValue bsonValue = raw.getDocumentKey().get("_id");
                    String id;
                    if (bsonValue.isObjectId()) id = bsonValue.asObjectId().toString();
                    else id = bsonValue.toString();

                    elasticsearchOperations.delete(id, IndexCoordinates.of(raw.getNamespace().getCollectionName()));
                });
            } else if (op.equals(OperationType.INSERT)) {
                elasticsearchOperations.bulkIndex(
                        subList.stream().map(raw -> {
                            BsonValue bsonValue = raw.getDocumentKey().get("_id");
                            String id;
                            if (bsonValue.isObjectId()) id = bsonValue.asObjectId().toString();
                            else id = bsonValue.toString();

                            // 隐去_class, _id
                            Map<String, Object> m = new HashMap<>();
                            raw.getFullDocument().forEach((key, value) -> {
                                if (key.equals("_id")) {
                                    if (value.getClass().equals(ObjectId.class)) {
                                        m.put("id", ((ObjectId)value).toString());
                                    } else {
                                        m.put("id", value.toString());
                                    }
                                }
                                else if (!key.startsWith("_"))   {
                                    m.put(key, value);
                                }
                            });

                            return new IndexQueryBuilder()
                                    .withId(id)
                                    .withIndex(namespace.getCollectionName())
                                    .withObject(m)
                                    .build();
                        }).toList()
                        , IndexCoordinates.of(namespace.getCollectionName())
                );
            }
        }
    }
}
