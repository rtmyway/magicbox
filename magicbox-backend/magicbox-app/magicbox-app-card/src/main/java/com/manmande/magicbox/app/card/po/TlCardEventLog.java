package com.manmande.magicbox.app.card.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manmande.magicbox.core.mybatis.po.AuditPo;
import lombok.Data;

@Data
@TableName("ts_card")
public class TlCardEventLog extends AuditPo {
    private String cardNo;
    private String eventItem;
}
