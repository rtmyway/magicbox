/*
 * Copyright (c) 2015—2030 GantSoftware.Co.Ltd. All rights reserved.
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * is not allowed to be distributed or copied without the license from
 * GantSoftware.Co.Ltd. Please contact the company for more information.
 */

package com.manmande.magicbox.core.web.handler;

import com.manmande.magicbox.core.exception.BusinessException;
import com.manmande.magicbox.core.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Locale;

@ControllerAdvice()
@Slf4j
public class ResponseHandler implements ResponseBodyAdvice {
    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof R) {
            return body;
        }
        return R.success(body);
    }

    @ResponseBody
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<R> handleException(final Exception e, final ServletWebRequest req) {
        //获取接口路径
        String apiUrl = req.getRequest().getServletPath();
        String[] paramArray = {};
        String errorCode = "";
        String errorMsg = "";
        if (e instanceof BusinessException) {
            BusinessException pe = (BusinessException) e;
            paramArray = pe.getParamArray();
            errorCode = pe.getErrorCode();
            // 输出日志
            log.error("{}, errorCode={}, paramArray={}", apiUrl, errorCode, paramArray);
        } else {
            errorCode = "SYSTEM_ERROR";
            // 输出日志
            log.error("  【" + apiUrl + "】", e);
        }

        try {
            Locale locale = LocaleContextHolder.getLocale();
            errorMsg =  messageSource.getMessage(errorCode, paramArray, locale);
        } catch (NoSuchMessageException ne) {
            errorMsg = errorCode;
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.error(errorMsg));
    }
}
