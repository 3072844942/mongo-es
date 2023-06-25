package org.self.strategy.context;

import org.self.strategy.SyncStrategy;
import org.self.strategy.enums.SynchronizeModeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author march
 * @since 2023/6/21 上午11:09
 */
@Service
public class SynchronizeStrategyContext implements SyncStrategy {
    /**
     * 搜索模式
     */
    @Value("${spring.mes.mode}")
    private String mode;

    @Autowired
    private Map<String, SyncStrategy> strategyMap;

    @Override
    public void save(Object o) {
        strategyMap.get(SynchronizeModeEnum.getStrategy(mode)).save(o);
    }

    @Override
    public void saveAll(List<Object> list) {
        strategyMap.get(SynchronizeModeEnum.getStrategy(mode)).saveAll(list);
    }

    @Override
    public void delete(Object id) {
        strategyMap.get(SynchronizeModeEnum.getStrategy(mode)).delete(id);
    }

    @Override
    public void deleteAll(List<Object> ids) {
        strategyMap.get(SynchronizeModeEnum.getStrategy(mode)).deleteAll(ids);
    }
}
