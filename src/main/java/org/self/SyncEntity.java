package org.self;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记实体类
 * 指定需要同步的类
 *
 * @author march
 * @since 2023/6/21 上午9:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SyncEntity {
    String value();
}
