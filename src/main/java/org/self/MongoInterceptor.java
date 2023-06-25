package org.self;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.self.strategy.context.SynchronizeStrategyContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截mongodb的数据更新
 */
@Aspect
@Component
public class MongoInterceptor {
    @Autowired
    private SynchronizeStrategyContext strategyContext;

    /**
     * 在插入完成后进行数据写入, 此时可以拿到完整数据
     *
     * @param joinPoint
     */
    @After("execution(* org.springframework.data.mongodb.repository.MongoRepository.insert(..))")
    public void afterInsert(JoinPoint joinPoint) {
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object data = args[0];
            if (data.getClass().isArray()) {
                strategyContext.saveAll((ArrayList) data);
            } else {
                strategyContext.save(data);
            }
        }
    }

    /**
     * 在插入完成后进行数据写入, 此时可以拿到完整数据
     *
     * @param joinPoint
     */
    @After("execution(* org.springframework.data.mongodb.repository.MongoRepository.save(..))")
    public void afterSave(JoinPoint joinPoint) {
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            strategyContext.save(args[0]);
        }
    }

    /**
     * 在插入完成后进行数据写入, 此时可以拿到完整数据
     * 这个不再是org.springframework.data.mongodb.repository.MongoRepository.saveAll，
     * 上面那个无法切面？
     *
     * @param joinPoint
     */
    @After("execution(* org.springframework.data.repository.CrudRepository.saveAll(..))")
    public void afterSaveAll(JoinPoint joinPoint) {
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            strategyContext.saveAll((List) args[0]);
        }
    }

    /**
     * 在删除前读取数据
     *
     * @param joinPoint
     */
    @Before("execution(* org.springframework.data.mongodb.repository.MongoRepository.deleteById(..))")
    public void beforeDeleteById(JoinPoint joinPoint) {
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            strategyContext.delete(args[0]);
        }
    }

    /**
     * 在删除前读取数据
     *
     * @param joinPoint
     */
    @After("execution(* org.springframework.data.mongodb.repository.MongoRepository.deleteAllById(..))")
    public void beforeDeleteAllById(JoinPoint joinPoint) {
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            strategyContext.deleteAll((List) args[0]);
        }
    }
}
