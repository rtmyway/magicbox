package com.manmande.magicbox.card.req;

import lombok.Data;

@Data
public class ApplyCommonReq {
    private String phone;
    private String cardNo;
    private String cardPassword;
    private String realName;
    private String addressInfo;
}
