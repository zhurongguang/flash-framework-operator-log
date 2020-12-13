package com.flash.framework.operator.log.common.annotation;

import com.flash.framework.operator.log.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author zhurg
 * @date 2019/2/2 - 上午11:53
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作模块
     *
     * @return
     */
    String module() default "";

    /**
     * 操作
     *
     * @return
     */
    String operator() default "";

    /**
     * 描述
     *
     * @return
     */
    String desc() default "";

    /**
     * 操作类型
     *
     * @return
     */
    OperatorType operatorType() default OperatorType.UNKNOWN;
}