package org.self;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author march
 * @since 2023/6/29 上午8:55
 */
@Log4j2
@Component
public class CollectionScanner implements BeanPostProcessor {
    public static final List<String> collections = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Sync.class)) {
            Sync annotation = bean.getClass().getAnnotation(Sync.class);
            collections.add(annotation.value());
            log.info("Sync Collection: " + annotation.value());
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
