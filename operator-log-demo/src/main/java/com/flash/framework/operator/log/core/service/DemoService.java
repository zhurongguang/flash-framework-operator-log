package com.flash.framework.operator.log.core.service;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.annotation.OperationLog;
import com.flash.framework.operator.log.common.enums.OperatorType;
import com.flash.framework.operator.log.core.model.Demo;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Service;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午5:29
 */
@Service
public class DemoService {

    @OperationLog(module = "测试", operator = "demo", desc = "demo", operatorType = OperatorType.INSERT)
    public Demo demo(Demo demo) {
        System.out.println(JSON.toJSONString(demo));
        return demo;
    }

    @OperationLog(module = "测试", operator = "demo2", desc = "demo2", operatorType = OperatorType.UPDATE)
    public Demo demo2(Demo demo) {
        return demo;
    }

    @OperationLog(module = "测试", operator = "demo3", desc = "demo3", operatorType = OperatorType.UPDATE)
    public Demo demo3(Demo demo) {
        return demo;
    }

    @OperationLog(module = "测试", operator = "demo4", desc = "demo4", operatorType = OperatorType.DELETE)
    public Demo demo4(Demo demo) {
        return demo;
    }

    @OperationLog(module = "测试", operator = "demo5", desc = "demo5", operatorType = OperatorType.UPDATE)
    public Demo demo5(Demo demo) {
        int r = 1 / 0;
        return demo;
    }

    @OperationLog(module = "测试", operator = "demo6", desc = "demo6", operatorType = OperatorType.DELETE)
    public void demo6(Long id) {
        System.out.println("delete " + id);
    }

    @OperationLog(module = "测试", operator = "demo7", desc = "demo7", operatorType = OperatorType.DELETE)
    public void demo7(Long id, @ApiParam(name = "name") String name) {
        System.out.println("delete " + id);
    }
}