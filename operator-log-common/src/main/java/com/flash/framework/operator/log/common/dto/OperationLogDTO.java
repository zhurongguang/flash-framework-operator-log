package com.flash.framework.operator.log.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 操作日志
 *
 * @author zhurg
 * @date 2019/2/8 - 上午10:01
 */
@Data
@ApiModel("操作日志DTO")
public class OperationLogDTO implements Serializable {

    private static final long serialVersionUID = -8279414471944198534L;

    /**
     * id
     */
    @ApiModelProperty("操作日志id")
    private Long id;

    /**
     * 模块
     */
    @ApiModelProperty("模块")
    private String module;

    /**
     * 操作
     */
    @ApiModelProperty("操作")
    private String operator;

    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    private String params;

    /**
     * 请求方法
     */
    @ApiModelProperty("请求方法")
    private String method;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型 INSERT 创建 UPDATE 更新 DELETE 删除 UNKNOWN 其他")
    private String operationType;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人id")
    private Long operatorId;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人名称")
    private String operatorName;

    /**
     * 操作人IP
     */
    @ApiModelProperty("操作人IP")
    private String operatorIp;

    /**
     * 状态 0 正常 1异常
     */
    @ApiModelProperty("操作结果 0 正常 1 异常")
    private Integer status;

    /**
     * 错误消息
     */
    @ApiModelProperty("错误信息")
    private String errorMsg;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /**
     * 历史数据
     */
    @ApiModelProperty("历史数据")
    private String histryRecord;

    /**
     * 新数据
     */
    @ApiModelProperty("新数据")
    private String newRecord;

    /**
     * 操作描述
     */
    @ApiModelProperty("操作描述")
    private String desc;

    /**
     * 扩展字段
     */
    private Map<String, String> extra;

    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    private Integer tenantId;

}