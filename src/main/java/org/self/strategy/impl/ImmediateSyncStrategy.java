package org.self.strategy.impl;

import org.self.strategy.SyncStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 即时同步策略
 *
 * @author march
 * @since 2023/6/21 上午11:12
 */
@Service
public class ImmediateSyncStrategy implements SyncStrategy {
    @Override
    public void save(Object o) {
        System.out.println(o.toString());
    }

    @Override
    public void saveAll(List<Object> list) {
        System.out.println(list.toString());
    }

    @Override
    public void delete(Object id) {
        System.out.println(id);
    }

    @Override
    public void deleteAll(List<Object> ids) {
        System.out.println(ids.toString());
    }
}
