package com.flash.framework.operator.log.api.facade;


import com.flash.framework.commons.paging.Paging;
import com.flash.framework.dubbo.common.response.RpcResponse;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.api.request.OperatorLogInfoRequest;
import com.flash.framework.operator.log.api.request.OperatorLogPagingRequest;

/**
 * 操作日志查询接口
 *
 * @author zhurg
 * @date 2019/2/8 - 上午10:06
 */
public interface OperatorLogFacade {

    /**
     * 分页查询操作日志
     *
     * @param operatorLogPagingRequest
     * @return
     */
    RpcResponse<Paging<OperationLogDTO>> queryOperatorLogPage(OperatorLogPagingRequest operatorLogPagingRequest);

    /**
     * 基于ID查询操作日志详情
     *
     * @param operatorLogInfoRequest
     * @return
     */
    RpcResponse<OperationLogDTO> queryOperatorLogById(OperatorLogInfoRequest operatorLogInfoRequest);
}