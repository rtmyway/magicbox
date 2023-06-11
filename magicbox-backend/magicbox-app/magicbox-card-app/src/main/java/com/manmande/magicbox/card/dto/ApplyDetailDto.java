package com.manmande.magicbox.card.dto;

import com.manmande.magicbox.card.po.TlCardEventLogPo;
import com.manmande.magicbox.card.po.TsConsumerApplyPo;
import lombok.Data;

import java.util.List;

@Data
public class ApplyDetailDto {
    private String cardNo;
    private String cardName;
    private TsConsumerApplyPo applyPo;
    private List<TlCardEventLogPo> eventLogPoList;
}
