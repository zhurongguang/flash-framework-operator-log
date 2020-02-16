package com.flash.framework.operator.log.server.service;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.server.converter.OperationLogConverter;
import com.flash.framework.operator.log.server.dao.OperatorLogDao;
import com.flash.framework.operator.log.server.model.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午4:31
 */
@Slf4j
@Service
public class OperatorLogService {

    @Autowired
    private OperatorLogDao operatorLogDao;

    @Autowired
    private OperationLogConverter operationLogConverter;

    public boolean insertOperatorLog(OperationLogDTO operationLogDTO) {
        try {
            OperationLog model = operationLogConverter.operatorLog2OperatorLogModel(operationLogDTO);
            if (MapUtils.isNotEmpty(operationLogDTO.getExtra())) {
                model.setExtraJson(JSON.toJSONString(operationLogDTO.getExtra()));
            }
            model.setCreatedAt(operationLogDTO.getOperTime());
            model.setCreateBy(Objects.isNull(operationLogDTO.getOperatorId()) ? null : operationLogDTO.getOperatorId().toString());
            model.setUpdateBy(model.getCreateBy());
            model.setUpdatedAt(model.getCreatedAt());
            return operatorLogDao.insert(model) == 1;
        } catch (Exception e) {
            log.error("[OperationLog] operation log save failed,cause:", e);
            return false;
        }
    }
}