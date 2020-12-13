package com.flash.framework.operator.log.server.service;

import com.flash.framework.commons.paging.Paging;
import com.flash.framework.mybatis.service.BaseService;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.server.model.OperationLog;

import java.util.Map;

/**
 * @author zhurg
 * @date 2020/4/30 - 下午12:03
 */
public interface OperatorLogService extends BaseService<OperationLog> {

    boolean insert(OperationLogDTO operationLogDTO);

    Paging<OperationLogDTO> paging(Paging<OperationLogDTO> page, Map<String, Object> conditions);
}