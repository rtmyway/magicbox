package com.manmande.magicbox.core.web.req;

import lombok.Data;

import java.util.Map;

@Data
public class PageReq {
    private int pageNum;
    private int pageSize;
    private Map params;
}
