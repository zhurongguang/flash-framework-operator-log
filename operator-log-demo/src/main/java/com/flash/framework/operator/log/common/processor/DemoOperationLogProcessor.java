package com.flash.framework.operator.log.common.processor;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.api.processor.AbstractOperationLogProcessor;
import com.flash.framework.operator.log.core.model.Demo;
import org.springframework.stereotype.Component;

/**
 * @author zhurg
 * @date 2019/9/12 - 上午11:20
 */
@Component
public class DemoOperationLogProcessor extends AbstractOperationLogProcessor<Demo> {

    @Override
    public Demo saveRecordsBeforeOperation(Object... args) {
        System.out.println("saveRecordsBeforeOperation : " + JSON.toJSONString(args));
        return (Demo) args[0];
    }

    @Override
    public Demo saveRecordsAfterOperation(Object result, Object... args) {
        System.out.println("saveRecordsAfterOperation : " + JSON.toJSONString(result) + " - " + JSON.toJSONString(args));
        return (Demo) args[0];
    }
}
