package com.flash.framework.operator.log.server.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.flash.framework.commons.paging.Paging;
import com.flash.framework.dubbo.common.response.RpcResponse;
import com.flash.framework.operator.log.api.facade.OperatorLogFacade;
import com.flash.framework.operator.log.api.request.OperatorLogInfoRequest;
import com.flash.framework.operator.log.api.request.OperatorLogPagingRequest;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.server.converter.OperationLogConverter;
import com.flash.framework.operator.log.server.model.OperationLog;
import com.flash.framework.operator.log.server.service.OperatorLogService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午2:08
 */
@Slf4j
@DubboService(version = "${operator.log.api.version:1.0.0}", retries = 0, timeout = 1000)
public class OperatorLogFacadeImpl implements OperatorLogFacade {

    @Autowired
    private OperatorLogService operatorLogService;

    @Autowired
    private OperationLogConverter operationLogConverter;

    @Override
    public RpcResponse<Paging<OperationLogDTO>> queryOperatorLogPage(OperatorLogPagingRequest operatorLogPagingRequest) {
        try {
            Paging<OperationLogDTO> paging = new Paging<>();
            paging.setPageNo(operatorLogPagingRequest.getPageNo());
            paging.setPageSize(operatorLogPagingRequest.getPageSize());

            Map<String, Object> conditions = JSON.parseObject(JSON.toJSONString(operatorLogPagingRequest));
            paging = operatorLogService.paging(paging, conditions);
            return RpcResponse.ok(paging);
        } catch (Exception e) {
            log.error("[OperationLog] query operatorLog page failed,cause:{}", Throwables.getStackTraceAsString(e));
            return RpcResponse.fail("operatorLog.paging.query.failed");
        }
    }

    @Override
    public RpcResponse<OperationLogDTO> queryOperatorLogById(OperatorLogInfoRequest operatorLogInfoRequest) {
        try {
            OperationLog operationLog = operatorLogService.getById(operatorLogInfoRequest.getId());
            if (Objects.nonNull(operationLog)) {
                OperationLogDTO operationLogDTO = operationLogConverter.operatorLogModel2OperatorLog(operationLog);
                if (StringUtils.isNotBlank(operationLog.getExtraJson())) {
                    operationLogDTO.setExtra(JSON.parseObject(operationLog.getExtraJson(), new TypeReference<Map<String, String>>() {
                    }));
                }
                return RpcResponse.ok(operationLogDTO);
            }
            return RpcResponse.fail("operatorLog.not.exists");
        } catch (Exception e) {
            log.error("[OperationLog] query operatorLog by id failed,cause:{}", Throwables.getStackTraceAsString(e));
            return RpcResponse.fail("operatorLog.info.query.failed");
        }
    }
}