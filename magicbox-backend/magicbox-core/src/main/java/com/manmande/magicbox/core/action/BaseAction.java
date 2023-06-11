package com.manmande.magicbox.core.action;


import com.manmande.magicbox.core.exception.BusinessException;
import com.manmande.magicbox.core.web.dto.PageDto;
import com.manmande.magicbox.core.web.req.PageReq;

import java.util.List;

public interface BaseAction<T> {
    Boolean add(T t) throws BusinessException;
    Boolean remove(T t) throws BusinessException;
    Boolean update(T t) throws BusinessException;

    Boolean enable(T t) throws BusinessException;

    Boolean disable(T t) throws BusinessException;
    List<T> list() throws BusinessException;
    PageDto listPage(PageReq pageReq) throws BusinessException;
}
