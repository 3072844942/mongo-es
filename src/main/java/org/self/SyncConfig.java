package org.self;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * @author march
 * @since 2023/6/26 下午3:43
 */
@Getter
@Log4j2
@EnableConfigurationProperties(SyncConfig.class)
@ConfigurationProperties(prefix = "spring.mes")
public class SyncConfig {
    /**
     * 搜索模式
     */
    @Value("${spring.mes.mode:IS}")
    public static String mode;

    /**
     * 同步的数据库
     */
    @Value("${spring.mes.collections:*}")
    public static List<String> collections;

    /**
     * ads模式下 容量限制
     */
    @Value("${spring.mes.capacity:1000}")
    public static int capacity;

    /**
     * ss模式下 时间间隔
     */
    @Value("${spring.mes.timeout:1000}")
    public static long timeout;

    @PostConstruct
    public void init() {
        log.info("数据开始同步...");
        log.info("同步类型: " + mode);
        log.info("同步集合: " + collections);
    }
}
