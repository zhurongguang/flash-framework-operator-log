package com.flash.framework.operator.log.api.resover;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhurg
 * @date 2019/9/6 - 上午11:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogParameter implements Serializable {

    private static final long serialVersionUID = -3987055494205357666L;

    private String parameterClass;

    private String params;
}