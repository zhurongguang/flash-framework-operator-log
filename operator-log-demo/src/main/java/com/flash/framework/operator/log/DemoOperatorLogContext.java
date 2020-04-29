package com.flash.framework.operator.log;

import com.flash.framework.commons.context.RequestContext;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2019/4/15 - 下午8:37
 */
@Component
public class DemoOperatorLogContext implements RequestContext {

    @Override
    public Long getUserId() {
        return 1L;
    }

    @Override
    public Integer getTenantId() {
        return 1;
    }

    @Override
    public String getUserName() {
        return "xxx";
    }

    @Override
    public String getIp() {
        return "192.168.3.4";
    }
}
