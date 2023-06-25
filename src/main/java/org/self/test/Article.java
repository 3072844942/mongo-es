package org.self.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.self.SyncEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author march
 * @since 2023/6/21 上午10:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("test")
//@SyncEntity("test")
public class Article {
    @Id
    private String id;

    private String content;

    private List<String> ids;
}
