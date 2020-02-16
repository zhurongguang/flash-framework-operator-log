package com.flash.framework.operator.log.server.dao;

import com.flash.framework.mybatis.mapper.AbstractMapper;
import com.flash.framework.operator.log.server.model.OperationLog;

import java.util.List;
import java.util.Map;

/**
 * 操作日志Dao
 *
 * @author zhurg
 * @date 2019/4/15 - 下午2:04
 */
public interface OperatorLogDao extends AbstractMapper<OperationLog> {

    /**
     * 条件分页查询操作日志
     *
     * @param conditions
     * @return
     */
    List<OperationLog> selectPageByCondition(Map<String, Object> conditions);

    /**
     * 分页查询记录数
     *
     * @param conditions
     * @return
     */
    long selectPageCountByCondition(Map<String, Object> conditions);
}