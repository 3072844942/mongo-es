package org.self.strategy.impl;

import org.self.strategy.SyncStrategy;

import java.util.List;

/**
 * 异步定时策略
 *
 * @author march
 * @since 2023/6/21 上午11:26
 */
public class ScheduledSyncStrategy implements SyncStrategy {
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
