package org.self;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author march
 * @since 2023/6/26 下午3:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@EnableConfigurationProperties(SyncConfig.class)
@ConfigurationProperties(prefix = "spring.mes")
public class SyncConfig {
    /**
     * 搜索模式
     */
    @Value("${spring.mes.mode:IS}")
    private String mode;

    /**
     * ads模式下 容量限制
     */
    @Value("${spring.mes.capacity:1000}")
    private int capacity;

    /**
     * ss模式下 时间间隔
     */
    @Value("${spring.mes.timeout:1000}")
    private long timeout;
}
