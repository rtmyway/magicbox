package com.manmande.magicbox.app.card.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manmande.magicbox.core.mybatis.po.AuditPo;
import lombok.Data;

@Data
@TableName("ts_consumer_apply")
public class TsConsumerApplyPo extends AuditPo {
    private String phone;
    private String cardNo;
    private String realName;
    private String addressInfo;
}
