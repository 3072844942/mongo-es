package org.self.strategy.impl;

import org.self.strategy.SyncStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 异步批量策略
 *
 * @author march
 * @since 2023/6/21 上午11:12
 */
@Service
public class AsyncDelayedSyncStrategy implements SyncStrategy {
    @Override
    public void save(Object o) {

    }

    @Override
    public void saveAll(List<Object> list) {

    }

    @Override
    public void delete(Object id) {

    }

    @Override
    public void deleteAll(List<Object> ids) {

    }
}
