package org.self.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author march
 * @since 2023/6/21 上午10:39
 */
@RestController
public class ArticleController {
    @Autowired
    private ArticleService service;

    @GetMapping("insert")
    public void insert() {
        service.insert();
    }

    @GetMapping("updateAll")
    public void updateAll() {
        service.updateAll();
    }

    @GetMapping("update")
    public void update() {
        service.update();
    }

    @GetMapping("delete")
    public void delete() {
        service.delete();
    }

    @GetMapping("deleteAll")
    public void deleteAll() {
        service.deleteAll();
    }
}
