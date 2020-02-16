package com.flash.framework.operator.log.server.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.flash.framework.mybatis.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午1:57
 */
@Data
@TableName("operation_log")
public class OperationLog extends BaseModel {

    private static final long serialVersionUID = -6886938197586217024L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模块
     */
    private String module;

    /**
     * 操作
     */
    private String operator;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 操作类型
     */
    @TableField("operation_type")
    private String operationType;

    /**
     * 操作人ID
     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 操作人
     */
    @TableField("operator_name")
    private String operatorName;

    /**
     * 操作人IP
     */
    @TableField("operator_ip")
    private String operatorIp;

    /**
     * 状态 0 正常 1异常
     */
    private Integer status;

    /**
     * 错误消息
     */
    @TableField("error_msg")
    private String errorMsg;

    /**
     * 操作时间
     */
    @TableField("oper_time")
    private Date operTime;

    /**
     * 历史数据
     */
    @TableField("histry_record")
    private String histryRecord;

    /**
     * 新数据
     */
    @TableField("new_record")
    private String newRecord;

    /**
     * 操作描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 扩展字段
     */
    @TableField("extra_json")
    private String extraJson;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Integer tenantId;
}