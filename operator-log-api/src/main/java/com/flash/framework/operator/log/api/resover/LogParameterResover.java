package com.flash.framework.operator.log.api.resover;

import java.util.Map;

/**
 * 日志参数解析器
 *
 * @author zhurg
 * @date 2019/9/6 - 上午11:40
 */
public interface LogParameterResover {

    Map<String, Object> resover(String params);
}