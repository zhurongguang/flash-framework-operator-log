package com.flash.framework.operator.log.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author zhurg
 * @date 2019/12/23 - 下午4:57
 */
@Data
@ApiModel("demo")
public class Demo implements Serializable {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("扩展字段")
    private Map<String, String> extra;

    @ApiModelProperty("信息")
    private List<String> infos;
}