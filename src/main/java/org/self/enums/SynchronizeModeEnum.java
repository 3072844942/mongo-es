package org.self.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.self.SyncConfig;

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
     * @return {@link String} 搜索策略
     */
    public static SynchronizeModeEnum getStrategy() {
        for (SynchronizeModeEnum value : SynchronizeModeEnum.values()) {
            if (value.getMode().equals(SyncConfig.mode)) {
                return value;
            }
        }
        return IS;
    }

}
