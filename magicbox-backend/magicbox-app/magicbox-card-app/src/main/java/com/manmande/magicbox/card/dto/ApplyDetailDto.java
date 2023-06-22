package com.manmande.magicbox.card.dto;

import com.manmande.magicbox.card.po.TlCardEventLogPo;
import com.manmande.magicbox.card.po.TsCardPo;
import com.manmande.magicbox.card.po.TsConsumerApplyPo;
import lombok.Data;

import java.util.List;

@Data
public class ApplyDetailDto {
    private TsCardPo cardPo;
    private TsConsumerApplyPo applyPo;
    private List<TlCardEventLogPo> eventLogPoList;
}
