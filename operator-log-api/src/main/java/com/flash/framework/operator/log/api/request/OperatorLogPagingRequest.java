package com.flash.framework.operator.log.api.request;

import com.flash.framework.dubbo.common.request.PagingRpcRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * 操作日志分页查询请求
 *
 * @author zhurg
 * @date 2019/6/21 - 下午2:54
 */
@Data
@ApiModel(
        description = "操作日志分页查询请求参数",
        value = "OperatorLogPagingRequest",
        parent = PagingRpcRequest.class
)
public class OperatorLogPagingRequest extends PagingRpcRequest {

    private static final long serialVersionUID = 7210744250729466309L;

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
     * 操作类型
     */
    @ApiModelProperty("操作类型 INSERT 创建 UPDATE 更新 DELETE 删除 UNKNOWN 其他")
    private String operatorType;

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
     * 查询时间段
     */
    @ApiModelProperty("查询开始时间")
    private String startTime;
    /**
     * 查询时间段
     */
    @ApiModelProperty("查询结束时间")
    private String endTime;

    /**
     * 操作人集合
     */
    @ApiModelProperty("操作人集合")
    private Set<Long> operatorIds;
}