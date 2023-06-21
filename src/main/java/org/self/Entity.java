package org.self;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 标记实体类
 * 指定同步的类信息
 *
 * @author march
 * @since 2023/6/21 上午9:52
 */
@Target({ElementType.TYPE})
public @interface Entity {
}
