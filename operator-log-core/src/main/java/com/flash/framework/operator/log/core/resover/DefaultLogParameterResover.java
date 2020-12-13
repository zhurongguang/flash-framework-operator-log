package com.flash.framework.operator.log.core.resover;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.flash.framework.operator.log.common.OperationLogConstants;
import com.flash.framework.operator.log.common.resover.LogParameter;
import com.flash.framework.operator.log.common.resover.LogParameterResover;
import com.flash.framework.operator.log.common.utils.SwaggerUtils;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 默认日志参数解析器
 *
 * @author zhurg
 * @date 2019/9/6 - 上午11:50
 */
@Slf4j
public class DefaultLogParameterResover implements LogParameterResover {


    @Override
    public Map<String, Object> resover(String params) {
        if (StringUtils.isBlank(params)) {
            return null;
        }
        List<LogParameter> parameters = JSONArray.parseArray(params, LogParameter.class);
        if (CollectionUtils.isEmpty(parameters)) {
            return null;
        }
        Map<String, Object> results = Maps.newHashMap();
        parameters.forEach(parameter -> {
            try {
                Class<?> clazz = parameter.getParameterClass();
                Object obj = JSON.parseObject(parameter.getParams(), clazz);
                //基础类型直接设置参数
                if (clazz.isPrimitive() || OperationLogConstants.PRIMITIVES.contains(clazz)) {
                    if (StringUtils.isNotBlank(parameter.getName())) {
                        results.put(parameter.getName(), obj);
                    } else {
                        results.put(OperationLogConstants.ARGS_PREFIX + results.size(), obj);
                    }
                }
                //对象进行swagger解析
                else {
                    SwaggerUtils.resoverSwagger(obj, results);
                }
            } catch (Exception e) {
                log.error("[OperationLog] LogParameterResover resover params {} failed,cause:{}", params, Throwables.getStackTraceAsString(e));
            }
        });
        return results;
    }


}
