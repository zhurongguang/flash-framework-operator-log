package com.flash.framework.operator.log.api.resover;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.flash.framework.operator.log.api.utils.SwaggerUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
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
                Class<?> clazz = Class.forName(parameter.getParameterClass());
                Object target = JSON.parseObject(parameter.getParams(), clazz);
                if (target.getClass().isPrimitive() || target.getClass().equals(String.class)) {
                    results.put("arg" + results.size(), target);
                } else {
                    SwaggerUtils.resoverSwagger(target, results);
                }
            } catch (Exception e) {
                log.error("[OperationLog] LogParameterResover resover params {} failed,cause:", params, e);
            }
        });
        return results;
    }


}
