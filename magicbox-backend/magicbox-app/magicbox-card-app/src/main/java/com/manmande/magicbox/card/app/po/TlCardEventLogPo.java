package com.manmande.magicbox.app.card.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manmande.magicbox.core.mybatis.po.AuditPo;
import lombok.Data;

@Data
@TableName("tl_card_event_log")
public class TlCardEventLogPo extends AuditPo {
    private String cardNo;
    private String eventItem;
}
