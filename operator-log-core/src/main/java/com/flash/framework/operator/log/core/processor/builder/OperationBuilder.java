package com.flash.framework.operator.log.core.processor.builder;

import com.flash.framework.operator.log.common.enums.OperatorType;

/**
 * @author zhurg
 * @date 2020/4/30 - 上午11:42
 */
public interface OperationBuilder {

    /**
     * 构建对应的操作模块名
     *
     * @param module
     * @param operator
     * @param operationType
     * @return
     */
    String buildForOperation(String module, String operator, OperatorType operationType);
}