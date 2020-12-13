package com.flash.framework.operator.log.common.utils;

import com.flash.framework.operator.log.common.OperationLogConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author zhurg
 * @date 2019/9/12 - 上午10:48
 */
@Slf4j
public class SwaggerUtils {

    /**
     * 解析swagger注解
     *
     * @param target
     * @param results
     * @throws Exception
     */
    public static void resoverSwagger(Object target, Map<String, Object> results) throws Exception {
        //解析swagger
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            if (Objects.nonNull(apiModelProperty) && !apiModelProperty.hidden()) {
                //基础类型处理
                if (field.getType().isPrimitive() || OperationLogConstants.PRIMITIVES.contains(field.getType())) {
                    results.put(apiModelProperty.value(), FieldUtils.readField(field, target, true));
                } else {
                    if (Collection.class.isAssignableFrom(field.getType())) {
                        Collection collection = (Collection) FieldUtils.readField(field, target, true);
                        List list = Lists.newArrayList();
                        if (CollectionUtils.isNotEmpty(collection)) {
                            for (Object item : collection) {
                                dealCollectionAndArray(list, item);
                            }
                        }
                        results.put(apiModelProperty.value(), list);
                    } else if (Map.class.isAssignableFrom(field.getType())) {
                        Map map = (Map) FieldUtils.readField(field, target, true);
                        Map<String, Object> datas = Maps.newHashMap();
                        if (MapUtils.isNotEmpty(map)) {
                            Set<Map.Entry> entrySet = map.entrySet();
                            for (Map.Entry entry : entrySet) {
                                if (entry.getValue().getClass().isPrimitive() || OperationLogConstants.PRIMITIVES.contains(entry.getValue().getClass())) {
                                    datas.put(entry.getKey().toString(), entry.getValue());
                                } else {
                                    Map<String, Object> subResults = Maps.newHashMap();
                                    resoverSwagger(entry.getValue(), subResults);
                                    datas.put(entry.getKey().toString(), subResults);
                                }
                            }
                        }
                        results.put(apiModelProperty.value(), datas);
                    } else if (field.getType().isArray()) {
                        Object[] array = (Object[]) FieldUtils.readField(field, target, true);
                        List<Object> list = Lists.newArrayList();
                        for (Object item : array) {
                            dealCollectionAndArray(list, item);
                        }
                        results.put(apiModelProperty.value(), list);
                    } else {
                        Map<String, Object> subResults = Maps.newHashMap();
                        resoverSwagger(FieldUtils.readField(field, target, true), subResults);
                        results.put(apiModelProperty.value(), subResults);
                    }
                }
            }
        }
    }

    private static void dealCollectionAndArray(List list, Object item) throws Exception {
        if (item.getClass().isPrimitive() || OperationLogConstants.PRIMITIVES.contains(item.getClass())) {
            list.add(item);
        } else {
            Map<String, Object> subResults = Maps.newHashMap();
            resoverSwagger(item, subResults);
            list.add(subResults);
        }
    }
}