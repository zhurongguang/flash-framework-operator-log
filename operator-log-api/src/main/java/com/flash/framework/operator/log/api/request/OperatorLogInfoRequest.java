package com.flash.framework.operator.log.api.request;

import com.flash.framework.dubbo.common.request.RpcRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 操作日志详情请求
 *
 * @author zhurg
 * @date 2019/6/21 - 下午4:06
 */
@Data
@ApiModel(
        description = "操作日志详情查询参数",
        parent = RpcRequest.class,
        value = "OperatorLogInfoRequest"
)
public class OperatorLogInfoRequest extends RpcRequest {

    private static final long serialVersionUID = 6189957775191424777L;

    @ApiModelProperty(value = "操作日志id", required = true)
    @NotNull(message = "operator.log.id.is.null")
    private Long id;
}