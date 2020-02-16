package com.flash.framework.operator.log.server.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.flash.framework.commons.paging.Paging;
import com.flash.framework.dubbo.common.response.RpcResponse;
import com.flash.framework.operator.log.api.reader.OperatorLogReader;
import com.flash.framework.operator.log.api.request.OperatorLogInfoRequest;
import com.flash.framework.operator.log.api.request.OperatorLogPagingRequest;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.server.converter.OperationLogConverter;
import com.flash.framework.operator.log.server.dao.OperatorLogDao;
import com.flash.framework.operator.log.server.model.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午2:08
 */
@Slf4j
@Service(version = "${operator.log.reader.version:1.0.0}", retries = 3, accesslog = "true", timeout = 1000)
public class OperatorLogReaderImpl implements OperatorLogReader {

    @Autowired
    private OperatorLogDao operatorLogDao;

    @Autowired
    private OperationLogConverter operationLogConverter;

    @Override
    public RpcResponse<Paging<OperationLogDTO>> queryOperatorLogPage(OperatorLogPagingRequest operatorLogPagingRequest) {
        try {
            Paging<OperationLogDTO> paging = new Paging<>();
            paging.setPageNo(operatorLogPagingRequest.getPageNo());
            paging.setPageSize(operatorLogPagingRequest.getPageSize());

            Map<String, Object> conditions = JSON.parseObject(JSON.toJSONString(operatorLogPagingRequest));
            long total = operatorLogDao.selectPageCountByCondition(conditions);
            paging.setTotal(total);
            if (total > 0) {
                conditions.put("offset", (operatorLogPagingRequest.getPageNo() - 1) * operatorLogPagingRequest.getPageSize());
                conditions.put("limit", operatorLogPagingRequest.getPageSize());
                List<OperationLog> records = operatorLogDao.selectPageByCondition(conditions);
                if (!CollectionUtils.isEmpty(records)) {
                    paging.setRecords(records.stream()
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
            return RpcResponse.ok(paging);
        } catch (Exception e) {
            log.error("[OperationLog] query operatorLog page failed,cause:", e);
            return RpcResponse.fail("operatorLog.paging.query.failed");
        }
    }

    @Override
    public RpcResponse<OperationLogDTO> queryOperatorLogById(OperatorLogInfoRequest operatorLogInfoRequest) {
        try {
            OperationLog operationLog = operatorLogDao.selectById(operatorLogInfoRequest.getId());
            if (Objects.nonNull(operationLog)) {
                OperationLogDTO operationLogDTO = operationLogConverter.operatorLogModel2OperatorLog(operationLog);
                if (StringUtils.isNotBlank(operationLog.getExtraJson())) {
                    operationLogDTO.setExtra(JSON.parseObject(operationLog.getExtraJson(), new TypeReference<Map<String, String>>() {
                    }));
                }
                return RpcResponse.ok(operationLogDTO);
            }
            return RpcResponse.ok(null);
        } catch (Exception e) {
            log.error("[OperationLog] query operatorLog by id failed,cause:", e);
            return RpcResponse.fail("operatorLog.info.query.failed");
        }
    }
}