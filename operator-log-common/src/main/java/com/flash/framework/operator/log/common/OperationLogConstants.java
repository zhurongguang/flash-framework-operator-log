package com.flash.framework.operator.log.common;

import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

/**
 * @author zhurg
 * @date 2019/9/12 - 上午10:57
 */
public interface OperationLogConstants {

    /**
     * 操作日志：操作成功
     */
    Integer OPERATOR_LOG_SUCCESS = 0;

    /**
     * 操作日志：操作失败
     */
    Integer OPERATOR_LOG_ERROR = 1;

    /**
     * 基础包装类型
     */
    Set<Class<?>> PRIMITIVES = Sets.newHashSet(
            String.class,
            Integer.class,
            Double.class,
            Float.class,
            Long.class,
            Boolean.class,
            BigInteger.class,
            BigDecimal.class
    );

    String ARGS_PREFIX = "参数";
}