package com.flash.framework.operator.log.server.converter;

import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.server.model.OperationLog;
import org.mapstruct.Mapper;

/**
 * zhurg
 *
 * @author zhurg
 * @date 2019/6/21 - 下午4:20
 */
@Mapper(componentModel = "spring")
public interface OperationLogConverter {

    OperationLog operatorLog2OperatorLogModel(OperationLogDTO operationLogDTO);


    OperationLogDTO operatorLogModel2OperatorLog(OperationLog operationLog);
}