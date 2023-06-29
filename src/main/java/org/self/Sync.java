package org.self;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要同步的类集合
 *
 * @author march
 * @since 2023/6/29 上午8:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Sync {
    /**
     * 所在集合
     */
    String value();
}
