package com.flash.framework.operator.log.core.processor;


import com.alibaba.fastjson.JSON;
import com.flash.framework.commons.utils.ReflectUtil;
import com.flash.framework.operator.log.common.OperationLogConstants;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.common.utils.SwaggerUtils;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 日志生成处理器
 *
 * @author zhurg
 * @date 2019/3/19 - 下午4:49
 */
public interface OperationLogProcessor<T> {

    /**
     * 操作执行前日志处理，可用来设置历史记录
     *
     * @param operationLogDTO 日志
     * @param args            入参
     */
    void beforeProcess(OperationLogDTO operationLogDTO, Object... args);

    /**
     * 操作执行后日志处理，可用来记录新纪录
     *
     * @param operationLogDTO 日志
     * @param args            入参
     * @param result          业务执行结果
     */
    void afterProcess(OperationLogDTO operationLogDTO, Object result, Object... args);

    /**
     * 异常处理
     *
     * @param operationLogDTO 日志
     * @param args            入参
     */
    void exceptionProcess(OperationLogDTO operationLogDTO, Object... args);

    /**
     * 对应的模块操作
     *
     * @return
     */
    Set<String> forOperations();

    /**
     * 解析记录
     *
     * @param record
     * @return
     */
    default Map<String, Object> analysisRecord(String record) throws Exception {
        Class<T> clazz = getCurrentModel();
        if (Objects.nonNull(clazz)) {
            T obj = JSON.parseObject(record, clazz);
            if (Objects.nonNull(obj)) {
                Map<String, Object> results = Maps.newHashMap();
                //基础类型直接设置参数
                if (clazz.isPrimitive() || OperationLogConstants.PRIMITIVES.contains(clazz)) {
                    results.put(OperationLogConstants.ARGS_PREFIX + results.size(), obj);
                }
                //对象进行swagger解析
                else {
                    SwaggerUtils.resoverSwagger(obj, results);
                }
                return results;
            }
        }
        return null;
    }

    /**
     * 解析档期接口泛型
     *
     * @return
     */
    default Class<T> getCurrentModel() {
        return (Class<T>) ReflectUtil.getInterfaceT(this, 0);
    }
}