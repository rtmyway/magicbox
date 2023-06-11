package com.manmande.magicbox.card.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manmande.magicbox.core.mybatis.po.AuditPo;
import lombok.Data;

@Data
@TableName("ts_card")
public class TsCardPo extends AuditPo {
    private String cardNo;
    private String cardName;
    private String cardPassword;
    private Integer price;
    private String batchNo;
}
