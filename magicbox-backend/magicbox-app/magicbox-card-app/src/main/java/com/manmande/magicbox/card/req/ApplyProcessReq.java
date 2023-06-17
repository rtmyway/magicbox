package com.manmande.magicbox.card.req;

import lombok.Data;

@Data
public class ApplyProcessReq {
    private String cardId;
    private String eventItemCode;
    private String phone;
    private String realName;
    private String addressInfo;
    private String expectedDate;
    private String description;
}
