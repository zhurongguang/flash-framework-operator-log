package com.flash.framework.operator.log.api.processor;

import com.alibaba.fastjson.JSON;
import com.flash.framework.commons.utils.ReflectUtil;
import com.flash.framework.operator.log.api.OperationLogConstants;
import com.flash.framework.operator.log.api.utils.SwaggerUtils;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.common.enums.OperatorType;
import com.flash.framework.operator.log.common.processor.OperationLogProcessor;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * 增强版OperationLogProcessor抽象实现，可以记录操作前、操作后的数据
 *
 * @author zhurg
 * @date 2019/9/12 - 上午9:55
 */
public abstract class AbstractOperationLogProcessor<T> implements OperationLogProcessor {

    @Override
    public void beforeProcess(OperationLogDTO operationLogDTO, Object... args) {
        Map<String, String> extra = operationLogDTO.getExtra();
        if (MapUtils.isEmpty(extra)) {
            extra = Maps.newHashMapWithExpectedSize(1);
        }
        extra.put(OperationLogConstants.OPERATION_LOG_PROCESSOR, this.getClass().getName());
        operationLogDTO.setExtra(extra);
        if (OperatorType.UPDATE.name().equals(operationLogDTO.getOperationType()) || OperatorType.DELETE.name().equals(operationLogDTO.getOperationType())) {
            T record = saveRecordsBeforeOperation(args);
            if (Objects.nonNull(record)) {
                operationLogDTO.setHistryRecord(JSON.toJSONString(record));
            }
        }
    }

    @Override
    public void afterProcess(OperationLogDTO operationLogDTO, Object result, Object... args) {
        if (OperatorType.INSERT.name().equals(operationLogDTO.getOperationType()) || OperatorType.UPDATE.name().equals(operationLogDTO.getOperationType())) {
            T record = saveRecordsAfterOperation(result, args);
            if (Objects.nonNull(record)) {
                operationLogDTO.setNewRecord(JSON.toJSONString(record));
            }
        }
    }

    @Override
    public void exceptionProcess(OperationLogDTO operationLogDTO, Object... args) {

    }

    /**
     * 解析记录
     *
     * @param record
     * @return
     */
    public Map<String, Object> analysisRecord(String record) throws Exception {
        Class<T> clazz = getCurrentModel();
        if (Objects.nonNull(clazz)) {
            T object = JSON.parseObject(record, clazz);
            if (Objects.nonNull(object)) {
                Map<String, Object> result = Maps.newHashMap();
                if (object.getClass().isPrimitive() || object.getClass().equals(String.class)) {
                    result.put("arg" + result.size(), object);
                } else {
                    SwaggerUtils.resoverSwagger(object, result);
                }
                return result;
            }
        }
        return null;
    }

    public Class<T> getCurrentModel() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
        return ReflectUtil.getGenericClass(getClass());
    }

    /**
     * 记录操作前的原始数据
     *
     * @param args
     */
    public abstract T saveRecordsBeforeOperation(Object... args);

    /**
     * 记录操作后的新数据
     *
     * @param result
     * @param args
     */
    public abstract T saveRecordsAfterOperation(Object result, Object... args);
}
