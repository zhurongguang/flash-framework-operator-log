package com.flash.framework.operator.log.core.processor.builder;

import com.flash.framework.operator.log.common.enums.OperatorType;

/**
 * @author zhurg
 * @date 2020/4/30 - 上午11:50
 */
public class DefaultOperationBuilder implements OperationBuilder {
    @Override
    public String buildForOperation(String module, String operator, OperatorType operationType) {
        return String.format("%s_%s_%s", module, operator, operationType);
    }
}