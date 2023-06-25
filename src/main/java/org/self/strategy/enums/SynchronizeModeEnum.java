package org.self.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author march
 * @since 2023/6/21 上午11:17
 */
@Getter
@AllArgsConstructor
public enum SynchronizeModeEnum {
    /**
     * 同步
     */
    IS("IS", "immediateSyncStrategy"),
    /**
     * 异步定量
     */
    ADS("ADS", "asyncDelayedSyncStrategy"),
    /**
     * 异步定时
     */
    SS("SS", "scheduledSyncStrategy");

    /**
     * 模式
     */
    private final String mode;

    /**
     * 策略
     */
    private final String strategy;

    /**
     * 获取策略
     *
     * @param mode 模式
     * @return {@link String} 搜索策略
     */
    public static String getStrategy(String mode) {
        for (SynchronizeModeEnum value : SynchronizeModeEnum.values()) {
            if (value.getMode().equals(mode)) {
                return value.getStrategy();
            }
        }
        return null;
    }

}
