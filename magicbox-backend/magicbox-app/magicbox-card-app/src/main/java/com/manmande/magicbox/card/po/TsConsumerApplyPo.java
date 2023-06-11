package com.manmande.magicbox.card.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manmande.magicbox.core.mybatis.po.AuditPo;
import lombok.Data;

import java.beans.Transient;

@Data
@TableName("ts_consumer_apply")
public class TsConsumerApplyPo extends AuditPo {
    private String phone;
    private String cardNo;
    private String realName;
    private String addressInfo;
}
