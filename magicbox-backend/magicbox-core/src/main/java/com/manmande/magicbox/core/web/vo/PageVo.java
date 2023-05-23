package com.manmande.magicbox.core.web.vo;

import lombok.Data;

import java.util.Map;

@Data
public class PageVo {
    private int pageNum;
    private int pageSize;
    private Map params;
}
