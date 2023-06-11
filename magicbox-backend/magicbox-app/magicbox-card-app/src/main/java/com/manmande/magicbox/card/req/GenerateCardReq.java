package com.manmande.magicbox.card.req;

import com.manmande.magicbox.core.web.req.BaseReq;
import lombok.Data;

@Data
public class GenerateCardReq extends BaseReq {
    private Integer cnt;
}
