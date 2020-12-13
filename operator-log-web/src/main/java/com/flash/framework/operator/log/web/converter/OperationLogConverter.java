package com.flash.framework.operator.log.web.converter;

import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.common.enums.OperatorType;
import com.flash.framework.operator.log.common.resover.LogParameterResover;
import com.flash.framework.operator.log.core.processor.OperationLogProcessor;
import com.flash.framework.operator.log.core.processor.OperationLogProcessorRegistry;
import com.flash.framework.operator.log.core.processor.builder.OperationBuilder;
import com.flash.framework.operator.log.web.vo.OperationLogVO;
import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author zhurg
 * @date 2019/9/9 - 下午6:00
 */
@Slf4j
@Component
@AllArgsConstructor
public class OperationLogConverter {

    private final LogParameterResover logParameterResover;

    private final OperationLogProcessorRegistry operationLogProcessorRegistry;

    private final OperationBuilder operationBuilder;

    public OperationLogVO convertOperationLogDTO2OperationLogVO(OperationLogDTO operationLogDTO) {
        OperationLogVO operationLogVO = new OperationLogVO();
        operationLogVO.setId(operationLogDTO.getId());
        operationLogVO.setDesc(operationLogDTO.getDesc());
        operationLogVO.setModule(operationLogDTO.getModule());
        operationLogVO.setOperator(operationLogDTO.getOperator());
        operationLogVO.setOperatorId(operationLogDTO.getOperatorId());
        operationLogVO.setOperatorIp(operationLogDTO.getOperatorIp());
        operationLogVO.setOperatorName(operationLogDTO.getOperatorName());
        operationLogVO.setOperatorType(operationLogDTO.getOperationType());
        operationLogVO.setMethod(operationLogDTO.getMethod());
        operationLogVO.setStatus(operationLogDTO.getStatus());
        operationLogVO.setErrorMsg(operationLogDTO.getErrorMsg());
        operationLogVO.setOperTime(operationLogDTO.getOperTime());
        //解析操作参数
        if (StringUtils.isNotBlank(operationLogDTO.getParams())) {
            operationLogVO.setParams(logParameterResover.resover(operationLogDTO.getParams()));
        }
        OperationLogProcessor operationLogProcessor = operationLogProcessorRegistry.getOrDefault(
                operationBuilder.buildForOperation(operationLogVO.getModule(), operationLogVO.getOperator(), OperatorType.valueOf(operationLogVO.getOperatorType())));
        //解析操作前数据记录和操作后数据记录
        if (StringUtils.isNotBlank(operationLogDTO.getHistryRecord())) {
            try {
                operationLogVO.setHistoryRecord(operationLogProcessor.analysisRecord(operationLogDTO.getHistryRecord()));
            } catch (Exception e) {
                log.error("[OperationLog] analysis histry record failed,cause:{}", Throwables.getStackTraceAsString(e));
            }
        }
        if (StringUtils.isNotBlank(operationLogDTO.getNewRecord())) {
            try {
                operationLogVO.setNewRecord(operationLogProcessor.analysisRecord(operationLogDTO.getNewRecord()));
            } catch (Exception e) {
                log.error("[OperationLog] analysis new record failed,cause:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return operationLogVO;
    }
}