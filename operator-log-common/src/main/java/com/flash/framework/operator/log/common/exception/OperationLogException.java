package com.flash.framework.operator.log.common.exception;

/**
 * @author zhurg
 * @date 2020/4/30 - 上午11:34
 */
public class OperationLogException extends RuntimeException {

    private static final long serialVersionUID = 1261874158412869327L;

    public OperationLogException() {
    }

    public OperationLogException(String message) {
        super(message);
    }

    public OperationLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationLogException(Throwable cause) {
        super(cause);
    }

    public OperationLogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}