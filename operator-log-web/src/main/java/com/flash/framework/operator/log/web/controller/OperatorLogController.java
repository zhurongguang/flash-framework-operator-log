package com.flash.framework.operator.log.web.controller;

import com.flash.framework.commons.context.RequestContext;
import com.flash.framework.commons.paging.Paging;
import com.flash.framework.dubbo.common.response.RpcResponse;
import com.flash.framework.dubbo.common.utils.RpcResponseUtils;
import com.flash.framework.operator.log.api.facade.OperatorLogFacade;
import com.flash.framework.operator.log.api.request.OperatorLogInfoRequest;
import com.flash.framework.operator.log.api.request.OperatorLogPagingRequest;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.web.converter.OperationLogConverter;
import com.flash.framework.operator.log.web.vo.OperationLogVO;
import com.flash.framework.web.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 操作日志
 *
 * @author zhurg
 * @date 2019/4/15 - 上午10:51
 */
@Api("[操作日志]")
@RestController
@RequestMapping("/api/logs")
public class OperatorLogController {

    @Reference(version = "${operator.log.reader.version:1.0.0}")
    private OperatorLogFacade operatorLogFacade;

    @Autowired
    private OperationLogConverter operationLogConverter;

    @Autowired
    private RequestContext requestContext;


    @GetMapping("/list")
    @ApiOperation("操作日志列表")
    public Paging<OperationLogDTO> list(OperatorLogPagingRequest request) throws Exception {
        request.setTenantId(requestContext.getTenantId());
        RpcResponse<Paging<OperationLogDTO>> response = operatorLogFacade.queryOperatorLogPage(request);
        return RpcResponseUtils.getResponse(response, msg -> new WebException(msg));
    }

    /**
     * 操作日志详情
     *
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    @ApiOperation("操作日志详情")
    public OperationLogVO info(@PathVariable("id") Long id) throws Exception {
        OperatorLogInfoRequest operatorLogInfoRequest = new OperatorLogInfoRequest();
        operatorLogInfoRequest.setId(id);
        RpcResponse<OperationLogDTO> response = operatorLogFacade.queryOperatorLogById(operatorLogInfoRequest);
        if (response.isSuccess()) {
            if (Objects.nonNull(response.getResult())) {
                return operationLogConverter.convertOperationLogDTO2OperationLogVO(response.getResult());
            }
            return null;
        } else {
            throw new WebException(response.getErrorMsg());
        }
    }
}