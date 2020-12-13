package com.flash.framework.operator.log.core.aop;

import com.alibaba.fastjson.JSON;
import com.flash.framework.commons.context.RequestContext;
import com.flash.framework.commons.utils.AopUtils;
import com.flash.framework.operator.log.common.OperationLogConstants;
import com.flash.framework.operator.log.common.annotation.OperationLog;
import com.flash.framework.operator.log.common.collector.OperationLogCollector;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.common.resover.LogParameter;
import com.flash.framework.operator.log.core.processor.OperationLogProcessor;
import com.flash.framework.operator.log.core.processor.OperationLogProcessorRegistry;
import com.flash.framework.operator.log.core.processor.builder.OperationBuilder;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 操作日志切面
 *
 * @author zhurg
 * @date 2019/2/8 - 下午2:14
 */
@Slf4j
@Aspect
public class OperatorLogAspect {

    @Autowired
    private OperationLogCollector operationLogCollector;

    @Autowired
    private RequestContext requestContext;

    @Autowired
    private OperationLogProcessorRegistry operationLogProcessorRegistry;

    @Autowired
    private OperationBuilder operationBuilder;

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
            Annotation[][] paramsAnnos = method.getParameterAnnotations();
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                parameters.add(LogParameter.builder()
                        .parameterClass(method.getParameterTypes()[i])
                        .params(Objects.nonNull(args[i]) ? JSON.toJSONString(args[i]) : null)
                        .name(findSwaggerApiParamAnno(paramsAnnos, i))
                        .build());
            }
            if (CollectionUtils.isNotEmpty(parameters)) {
                operationLogDTO.setParams(JSON.toJSONString(parameters));
            }
        }

        OperationLogProcessor operationLogProcessor = operationLogProcessorRegistry.getOrDefault(
                operationBuilder.buildForOperation(operationLogDTO.getModule(), operationLogDTO.getOperator(), anno.operatorType()));

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
                operationLogCollector.collectOperationLog(operationLogDTO);
            } catch (Exception e) {
                log.error("[OperationLog] save operation log failed,cause:{}", Throwables.getStackTraceAsString(e));
            }
        }
    }

    private void doProcessorBefore(OperationLogProcessor operationLogProcessor, OperationLogDTO operationLogDTO, Object... args) {
        try {
            operationLogProcessor.beforeProcess(operationLogDTO, args);
        } catch (Throwable e) {
            log.error("[OperationLog] OperationLogProcessor {} do beforeProcess failed,cause:{}", operationLogProcessor, Throwables.getStackTraceAsString(e));
        }
    }

    private void doProcessorAfter(OperationLogProcessor operationLogProcessor, OperationLogDTO operationLogDTO, Object result, Object... args) {
        try {
            operationLogProcessor.afterProcess(operationLogDTO, result, args);
        } catch (Throwable e) {
            log.error("[OperationLog] OperationLogProcessor {} do afterProcess failed,cause:{}", operationLogProcessor, Throwables.getStackTraceAsString(e));
        }
    }

    private void doProcessorException(OperationLogProcessor operationLogProcessor, OperationLogDTO operationLogDTO, Object... args) {
        try {
            operationLogProcessor.exceptionProcess(operationLogDTO, args);
        } catch (Throwable e) {
            log.error("[OperationLog] OperationLogProcessor {} do exceptionProcess failed,cause:{}", operationLogProcessor, Throwables.getStackTraceAsString(e));
        }
    }

    private String findSwaggerApiParamAnno(Annotation[][] paramsAnnos, int index) {
        if (Objects.isNull(paramsAnnos)) {
            return null;
        }
        Annotation[] annotations = paramsAnnos[index];
        if (Objects.isNull(annotations) || annotations.length <= 0) {
            return null;
        }
        Optional<Annotation> optionalAnnotation = Lists.newArrayList(annotations).stream()
                .filter(anno -> anno instanceof ApiParam)
                .findFirst();
        if (!optionalAnnotation.isPresent()) {
            return null;
        }
        ApiParam apiParam = (ApiParam) optionalAnnotation.get();
        if (apiParam.hidden()) {
            return null;
        }
        return apiParam.name();
    }
}