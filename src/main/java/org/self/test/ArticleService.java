package org.self.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author march
 * @since 2023/6/21 上午10:36
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository repository;

    public void insert() {
        Article article = Article.builder()
                .content(String.valueOf(System.currentTimeMillis()))
                .build();

        repository.insert(article);
    }

    public void update() {
        Article article = Article.builder()
                .id("1")
                .content(String.valueOf(System.currentTimeMillis()))
                .build();

        repository.save(article);
    }

    public void delete() {
        List<Article> all = repository.findAll();
        if (all.size() != 0) {
            Article article = all.get(0);
            repository.deleteById(article.getId());
        }
    }

    public void deleteAll() {
        repository.deleteAllById(repository.findAll().stream().map(Article::getId).toList());
    }

    public void updateAll() {
        Article article = Article.builder()
                .id("1")
                .content("2")
                .build();
        Article article1 = Article.builder()
                .id("2")
                .content("2")
                .build();

        repository.saveAll(Arrays.asList(article, article1));
    }
}
