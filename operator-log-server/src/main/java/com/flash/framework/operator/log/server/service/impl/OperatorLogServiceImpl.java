package com.flash.framework.operator.log.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.flash.framework.commons.paging.Paging;
import com.flash.framework.mybatis.service.BaseServiceImpl;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.server.converter.OperationLogConverter;
import com.flash.framework.operator.log.server.dao.OperatorLogDao;
import com.flash.framework.operator.log.server.model.OperationLog;
import com.flash.framework.operator.log.server.service.OperatorLogService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午4:31
 */
@Slf4j
@Service
public class OperatorLogServiceImpl extends BaseServiceImpl<OperatorLogDao, OperationLog> implements OperatorLogService {


    @Autowired
    private OperationLogConverter operationLogConverter;

    @Override
    public boolean insert(OperationLogDTO operationLogDTO) {
        try {
            OperationLog model = operationLogConverter.operatorLog2OperatorLogModel(operationLogDTO);
            if (MapUtils.isNotEmpty(operationLogDTO.getExtra())) {
                model.setExtraJson(JSON.toJSONString(operationLogDTO.getExtra()));
            }
            model.setCreatedAt(operationLogDTO.getOperTime());
            model.setCreateBy(Objects.isNull(operationLogDTO.getOperatorId()) ? null : operationLogDTO.getOperatorId().toString());
            model.setUpdateBy(model.getCreateBy());
            model.setUpdatedAt(new Date());
            model.setDeleted(Boolean.FALSE);
            return save(model);
        } catch (Exception e) {
            log.error("[OperationLog] operation log save failed,cause:{}", Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public Paging<OperationLogDTO> paging(Paging<OperationLogDTO> page, Map<String, Object> conditions) {
        long total = baseMapper.selectPageCountByCondition(conditions);
        page.setTotal(total);
        if (total > 0) {
            conditions.put("offset", (page.getPageNo() - 1) * page.getPageSize());
            conditions.put("limit", page.getPageSize());
            List<OperationLog> records = baseMapper.selectPageByCondition(conditions);
            if (!CollectionUtils.isEmpty(records)) {
                page.setRecords(records.stream()
                        .map(record -> {
                            OperationLogDTO operationLogDTO = operationLogConverter.operatorLogModel2OperatorLog(record);
                            if (StringUtils.isNotBlank(record.getExtraJson())) {
                                operationLogDTO.setExtra(JSON.parseObject(record.getExtraJson(), new TypeReference<Map<String, String>>() {
                                }));
                            }
                            return operationLogDTO;
                        })
                        .collect(Collectors.toList()));
            }
        }
        return page;
    }
}