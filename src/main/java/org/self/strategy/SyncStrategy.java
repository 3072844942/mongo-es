package org.self.strategy;

import java.util.List;

/**
 * 抽象同步策略
 *
 * @author march
 * @since 2023/6/21 上午11:09
 */
public interface SyncStrategy {
    void save(Object o);

    void saveAll(List<Object> list);

    void delete(Object id);

    void deleteAll(List<Object> ids);
}
