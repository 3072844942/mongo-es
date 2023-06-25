package org.self.test;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author march
 * @since 2023/6/21 上午10:37
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
}
