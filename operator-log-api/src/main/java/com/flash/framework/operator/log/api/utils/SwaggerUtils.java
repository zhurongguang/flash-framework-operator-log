package com.flash.framework.operator.log.api.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author zhurg
 * @date 2019/9/12 - 上午10:48
 */
@Slf4j
public class SwaggerUtils {

    private static final Set<Class<?>> PRIMITIVES = Sets.newHashSet(
            String.class,
            Integer.class,
            Double.class,
            Float.class,
            Long.class,
            Boolean.class,
            BigInteger.class,
            BigDecimal.class
    );

    public static void resoverSwagger(Object target, Map<String, Object> results) throws Exception {
        //解析swagger
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            if (Objects.nonNull(apiModelProperty)) {
                if (!apiModelProperty.hidden()) {
                    if (field.getType().isPrimitive() || PRIMITIVES.contains(field.getType())) {
                        results.put(apiModelProperty.value(), FieldUtils.readField(field, target, true));
                    } else {
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            Collection collection = (Collection) FieldUtils.readField(field, target, true);
                            List list = Lists.newArrayListWithCapacity(collection.size());
                            if (CollectionUtils.isNotEmpty(collection)) {
                                collection.forEach(item -> {
                                    try {
                                        Map<String, Object> subResults = Maps.newHashMap();
                                        resoverSwagger(item, subResults);
                                        list.add(subResults);
                                    } catch (Exception e) {
                                        log.warn("[OperationLog] Field {} analysis fialed,cause:", item, e);
                                    }
                                });
                            }
                            results.put(apiModelProperty.value(), list);
                        } else if (Map.class.isAssignableFrom(field.getType())) {
                            results.put(apiModelProperty.value(), FieldUtils.readField(field, target, true));
                        } else if (field.getType().isArray()) {
                            results.put(apiModelProperty.value(), FieldUtils.readField(field, target, true));
                        } else {
                            Map<String, Object> subResults = Maps.newHashMap();
                            resoverSwagger(FieldUtils.readField(field, target, true), subResults);
                            results.put(apiModelProperty.value(), subResults);
                        }
                    }
                }
            }
        }
    }
}