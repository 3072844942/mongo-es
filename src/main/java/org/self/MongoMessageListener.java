package org.self;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import org.bson.Document;
import org.springframework.data.mongodb.core.messaging.Message;
import org.springframework.data.mongodb.core.messaging.MessageListener;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@Component
public class MongoMessageListener implements MessageListener<ChangeStreamDocument<Document>, Object> {
    @Override
    public void onMessage(Message<ChangeStreamDocument<Document>, Object> message) {
        Class<?> aClass = message.getBody().getClass();
        SyncEntity annotation = aClass.getAnnotation(SyncEntity.class);
        if (annotation == null) { // 如果没有被标记
            return;
        }
        // todo 解析oplog
        //{ operationType=insert, resumeToken={"_data": "826497A651000000012B022C0100296E5A1004A36A1CA6D56B415CB06579A2D96FEBF146645F696400646497A6515E70716EA1124EAC0004"}, namespace=oj.test, destinationNamespace=null, fullDocument=Document{{_id=6497a6515e70716ea1124eac, content=1687660113375, _class=org.self.test.Article}}, fullDocumentBeforeChange=null, documentKey={"_id": {"$oid": "6497a6515e70716ea1124eac"}}, clusterTime=Timestamp{value=7248444992098664449, seconds=1687660113, inc=1}, updateDescription=null, txnNumber=null, lsid=null, wallTime=BsonDateTime{value=1687660113437}}
        System.out.println(message.getRaw());
        OperationType operationType = message.getRaw().getOperationType();
        System.out.println("操作类型为 :" + operationType);
        System.out.println();
    }
}