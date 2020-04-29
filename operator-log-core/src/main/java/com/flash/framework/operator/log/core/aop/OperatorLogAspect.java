package com.flash.framework.operator.log.core.aop;

import com.alibaba.fastjson.JSON;
import com.flash.framework.commons.context.RequestContext;
import com.flash.framework.commons.utils.AopUtils;
import com.flash.framework.operator.log.api.OperationLogConstants;
import com.flash.framework.operator.log.api.processor.AbstractOperationLogProcessor;
import com.flash.framework.operator.log.api.resover.LogParameter;
import com.flash.framework.operator.log.common.annotation.OperationLog;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.common.processor.OperationLogProcessor;
import com.flash.framework.operator.log.core.writer.OperationLogWriter;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 操作日志切面
 *
 * @author zhurg
 * @date 2019/2/8 - 下午2:14
 */
@Slf4j
@Aspect
public class OperatorLogAspect {

    private Map<Class<? extends OperationLogProcessor>, OperationLogProcessor> logProcessorMap = Maps.newConcurrentMap();

    @Autowired
    private OperationLogWriter operationLogWriter;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RequestContext requestContext;

    @PostConstruct
    public void init() {
        Map<String, AbstractOperationLogProcessor> processorBeans = applicationContext.getBeansOfType(AbstractOperationLogProcessor.class);
        if (MapUtils.isNotEmpty(processorBeans)) {
            processorBeans.values().forEach(operationLogProcessor -> logProcessorMap.put(operationLogProcessor.getClass(), operationLogProcessor));
        }
    }

    @Around(value = "@annotation(com.flash.framework.operator.log.common.annotation.OperationLog)")
    public Object operator(ProceedingJoinPoint joinPoint) throws Throwable {
        OperationLogDTO operationLogDTO = new OperationLogDTO();

        Method method = AopUtils.getMethod(joinPoint);
        OperationLog anno = method.getAnnotation(OperationLog.class);
        operationLogDTO.setMethod(method.getName());
        operationLogDTO.setModule(anno.module());
        operationLogDTO.setDesc(anno.desc());
        operationLogDTO.setOperationType(anno.operatorType().name());
        operationLogDTO.setOperTime(new Date());
        operationLogDTO.setOperator(anno.operator());
        operationLogDTO.setOperatorId(requestContext.getUserId());
        operationLogDTO.setOperatorIp(requestContext.getIp());
        operationLogDTO.setOperatorName(requestContext.getUserName());
        operationLogDTO.setTenantId(requestContext.getTenantId());

        Object[] args = joinPoint.getArgs();
        if (Objects.nonNull(args) && args.length > 0) {
            List<LogParameter> parameters = Lists.newArrayList();
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                parameters.add(LogParameter.builder()
                        .parameterClass(method.getParameterTypes()[i].getName())
                        .params(Objects.nonNull(args[i]) ? JSON.toJSONString(args[i]) : null)
                        .build());
            }
            if (CollectionUtils.isNotEmpty(parameters)) {
                operationLogDTO.setParams(JSON.toJSONString(parameters));
            }
        }

        OperationLogProcessor operationLogProcessor = null;
        if (Objects.nonNull(anno.logProcessor()) && anno.logProcessor().length > 0) {
            operationLogProcessor = logProcessorMap.get(anno.logProcessor()[0]);
        }
        try {

            doProcessorBefore(operationLogProcessor, operationLogDTO, args);

            Object rs = joinPoint.proceed();
            operationLogDTO.setStatus(OperationLogConstants.OPERATOR_LOG_SUCCESS);

            doProcessorAfter(operationLogProcessor, operationLogDTO, rs, args);

            return rs;
        } catch (Throwable e) {
            operationLogDTO.setStatus(OperationLogConstants.OPERATOR_LOG_ERROR);
            operationLogDTO.setErrorMsg(e.getMessage().length() > 1000 ? e.getMessage().substring(0, 1000) : e.getMessage());

            doProcessorException(operationLogProcessor, operationLogDTO, args);

            throw e;
        } finally {
            try {
                operationLogWriter.saveOperationLog(operationLogDTO);
            } catch (Exception e) {
                log.error("[OperationLog] save operation log failed,cause:{}", Throwables.getStackTraceAsString(e));
            }
        }
    }

    private void doProcessorBefore(OperationLogProcessor operationLogProcessor, OperationLogDTO operationLogDTO, Object... args) {
        if (Objects.nonNull(operationLogProcessor)) {
            try {
                operationLogProcessor.beforeProcess(operationLogDTO, args);
            } catch (Throwable e) {
                log.error("[OperationLog] OperationLogProcessor {} do beforeProcess failed,cause:{}", operationLogProcessor.getClass().getCanonicalName(), Throwables.getStackTraceAsString(e));
            }
        }
    }

    private void doProcessorAfter(OperationLogProcessor operationLogProcessor, OperationLogDTO operationLogDTO, Object result, Object... args) {
        if (Objects.nonNull(operationLogProcessor)) {
            try {
                operationLogProcessor.afterProcess(operationLogDTO, result, args);
            } catch (Throwable e) {
                log.error("[OperationLog] OperationLogProcessor {} do afterProcess failed,cause:{}", operationLogProcessor.getClass().getCanonicalName(), Throwables.getStackTraceAsString(e));
            }
        }
    }

    private void doProcessorException(OperationLogProcessor operationLogProcessor, OperationLogDTO operationLogDTO, Object... args) {
        if (Objects.nonNull(operationLogProcessor)) {
            try {
                operationLogProcessor.exceptionProcess(operationLogDTO, args);
            } catch (Throwable e) {
                log.error("[OperationLog] OperationLogProcessor {} do exceptionProcess failed,cause:{}", operationLogProcessor.getClass().getCanonicalName(), Throwables.getStackTraceAsString(e));
            }
        }
    }
}