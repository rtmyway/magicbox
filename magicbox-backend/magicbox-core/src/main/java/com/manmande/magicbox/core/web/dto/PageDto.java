package com.manmande.magicbox.core.web.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data

public class PageDto {
    private List list;
    private long total;
    private int pageNum;
    private int pageSize;
}
