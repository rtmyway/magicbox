package com.manmande.magicbox.core.exception;

public class BusinessException extends BaseException{
    public BusinessException() {
        super();
    }

    public BusinessException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BusinessException(String errorCode, String[] paramArray) {
        super();
        this.errorCode = errorCode;
        this.paramArray = paramArray;
    }
}
