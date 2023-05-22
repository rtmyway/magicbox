package com.manmande.magicbox.core.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {
    /** 错误代码 */
    protected String errorCode;
    /** 替换参数 */
    protected String[] paramArray;
    /** 错误信息 */
    protected String errorMessage;

    public BaseException() {
        super();
    }

    public BaseException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, String[] paramArray) {
        super();
        this.errorCode = errorCode;
        this.paramArray = paramArray;
    }
}
