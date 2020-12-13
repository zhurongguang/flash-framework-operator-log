package com.flash.framework.operator.log.core.processor;

import com.flash.framework.operator.log.common.exception.OperationLogException;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

/**
 * OperationLogProcessor 注册器
 *
 * @author zhurg
 * @date 2020/4/30 - 上午11:19
 */
@Component
public class OperationLogProcessorRegistry {

    private Map<String, OperationLogProcessor> operationLogProcessors = Maps.newConcurrentMap();

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        Map<String, OperationLogProcessor> beans = applicationContext.getBeansOfType(OperationLogProcessor.class);
        if (MapUtils.isNotEmpty(beans)) {
            beans.values().forEach(bean -> {
                Set<String> operations = bean.forOperations();
                if (CollectionUtils.isEmpty(operations)) {
                    throw new OperationLogException("[OperationLog] OperationLogProcessor " + bean + " forOperations can not be empty");
                }
                operations.forEach(operation -> {
                    if (operationLogProcessors.containsKey(operation)) {
                        throw new OperationLogException(String.format("[OperationLog] OperationLogProcessor for operation %s already exists,fund two OperationLogProcessor %s and %s", operation, operationLogProcessors.get(operation), bean));
                    }
                    operationLogProcessors.put(operation, bean);
                });
            });
        }
    }

    public OperationLogProcessor get(String operation) {
        return operationLogProcessors.get(operation);
    }

    public OperationLogProcessor getOrDefault(String operation) {
        if (operationLogProcessors.containsKey(operation)) {
            return operationLogProcessors.get(operation);
        }
        return operationLogProcessors.get(DefaultOperationLogProcessor.DEFAULT_OPERATION_LOG_PROCESSOR);
    }
}