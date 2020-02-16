package com.flash.framework.operator.log.web.converter;

import com.flash.framework.operator.log.api.OperationLogConstants;
import com.flash.framework.operator.log.api.processor.AbstractOperationLogProcessor;
import com.flash.framework.operator.log.api.resover.LogParameterResover;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.web.vo.OperationLogVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhurg
 * @date 2019/9/9 - 下午6:00
 */
@Slf4j
@Component
public class OperationLogConverter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private LogParameterResover logParameterResover;


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
        if (null != logParameterResover && StringUtils.isNotBlank(operationLogDTO.getParams())) {
            operationLogVO.setParams(logParameterResover.resover(operationLogDTO.getParams()));
        }
        if (StringUtils.isNotBlank(operationLogDTO.getHistryRecord()) || StringUtils.isNotBlank(operationLogDTO.getNewRecord())) {
            if (MapUtils.isNotEmpty(operationLogDTO.getExtra()) && operationLogDTO.getExtra().containsKey(OperationLogConstants.OPERATION_LOG_PROCESSOR)) {
                String logProcessorStr = operationLogDTO.getExtra().get(OperationLogConstants.OPERATION_LOG_PROCESSOR);
                try {
                    Class<?> clazz = Class.forName(logProcessorStr);
                    Object bean = applicationContext.getBean(clazz);
                    if (bean instanceof AbstractOperationLogProcessor) {
                        AbstractOperationLogProcessor operationLogProcessor = (AbstractOperationLogProcessor) bean;
                        if (StringUtils.isNotBlank(operationLogDTO.getHistryRecord())) {
                            operationLogVO.setHistoryRecord(operationLogProcessor.analysisRecord(operationLogDTO.getHistryRecord()));
                        }
                        if (StringUtils.isNotBlank(operationLogDTO.getNewRecord())) {
                            operationLogVO.setNewRecord(operationLogProcessor.analysisRecord(operationLogDTO.getNewRecord()));
                        }
                    }
                } catch (Exception e) {
                    log.warn("[OperationLog] OperationLogProcessor class {} not exists,error:", logProcessorStr, e);
                }
            }
        }
        return operationLogVO;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        try {
            logParameterResover = applicationContext.getBean(LogParameterResover.class);
        } catch (Exception e) {
        }
    }
}