package com.flash.framework.operator.log.processor;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.common.enums.OperatorType;
import com.flash.framework.operator.log.core.model.Demo;
import com.flash.framework.operator.log.core.processor.OperationLogProcessor;
import com.flash.framework.operator.log.core.processor.builder.OperationBuilder;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 可以用来保存更新前、后记录，删除前记录
 *
 * @author zhurg
 * @date 2020/5/10 - 上午11:28
 */
@Component
public class SaveOperatorRecordOperationLogProcessor implements OperationLogProcessor<Demo> {

    @Autowired
    private OperationBuilder operationBuilder;

    @Override
    public void beforeProcess(OperationLogDTO operationLogDTO, Object... args) {
        //更新前记录更新前原始数据 || 删除前记录历史数据
        if (operationLogDTO.getOperationType().equals(OperatorType.UPDATE.name()) || operationLogDTO.getOperationType().equals(OperatorType.DELETE.name())) {
            Demo demo = Demo.builder().age(1).username("更新前历史数据").build();
            operationLogDTO.setHistryRecord(JSON.toJSONString(demo));
        }
    }

    @Override
    public void afterProcess(OperationLogDTO operationLogDTO, Object result, Object... args) {
        //更新后记录更新数据
        if (operationLogDTO.getOperationType().equals(OperatorType.UPDATE.name())) {
            Demo demo = Demo.builder().age(1).username("更新后新数据").build();
            operationLogDTO.setNewRecord(JSON.toJSONString(demo));
        }
    }

    @Override
    public void exceptionProcess(OperationLogDTO operationLogDTO, Object... args) {

    }

    @Override
    public Set<String> forOperations() {
        return Sets.newHashSet(
                operationBuilder.buildForOperation("测试", "demo5", OperatorType.UPDATE),
                operationBuilder.buildForOperation("测试", "demo6", OperatorType.DELETE)
        );
    }
}
