package com.manmande.magicbox.card.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manmande.magicbox.card.constant.EventItem;
import com.manmande.magicbox.card.dto.ApplyDetailDto;
import com.manmande.magicbox.card.mapper.TlCardEventLogMapper;
import com.manmande.magicbox.card.mapper.TsCardMapper;
import com.manmande.magicbox.card.mapper.TsConsumerApplyMapper;
import com.manmande.magicbox.card.po.TlCardEventLogPo;
import com.manmande.magicbox.card.po.TsCardPo;
import com.manmande.magicbox.card.po.TsConsumerApplyPo;
import com.manmande.magicbox.card.req.ApplyCommonReq;
import com.manmande.magicbox.card.req.ApplyProcessReq;
import com.manmande.magicbox.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplyService {
    @Autowired
    private TsCardMapper tsCardMapper;
    @Autowired
    private TsConsumerApplyMapper tsConsumerApplyMapper;
    @Autowired
    private TlCardEventLogMapper tlCardEventLogMapper;

    public ApplyDetailDto load(ApplyCommonReq req) {
        List<TsCardPo> poList = tsCardMapper.selectList(new QueryWrapper<TsCardPo>().eq("card_no", req.getCardNo()).eq("card_password", req.getCardPassword()));
        if (poList.isEmpty()) {
            throw new BusinessException("卡号或密码错误");
        }
        ApplyDetailDto dto = new ApplyDetailDto();
        dto.setCardNo(poList.get(0).getCardNo());
        dto.setCardName(poList.get(0).getCardName());
        List<TsConsumerApplyPo> applyPoList = tsConsumerApplyMapper.selectList(new QueryWrapper<TsConsumerApplyPo>().eq("card_no", req.getCardNo()));
        if (!applyPoList.isEmpty()) {
            dto.setApplyPo(applyPoList.get(0));
        }
        List<TlCardEventLogPo> eventLogPoList = tlCardEventLogMapper.selectList(new QueryWrapper<TlCardEventLogPo>().eq("card_no", req.getCardNo()));
        dto.setEventLogPoList(eventLogPoList);
        return dto;
    }

    @Transactional
    public Boolean add(ApplyCommonReq req) {
        List<TsCardPo> poList = tsCardMapper.selectList(new QueryWrapper<TsCardPo>().eq("card_no", req.getCardNo()).eq("card_password", req.getCardPassword()));
        if (poList.isEmpty()) {
            throw new BusinessException("卡号或密码错误");
        }

        TsCardPo cardPo = poList.get(0);

        List<TlCardEventLogPo> eventLogPoList = tlCardEventLogMapper.selectList(new QueryWrapper<TlCardEventLogPo>().eq("card_no", req.getCardNo()).orderByAsc("created_at"));
        TlCardEventLogPo lastPo = eventLogPoList.get(eventLogPoList.size() - 1);
        EventItem lastEventItem = EventItem.getByCode(lastPo.getEventItem());

        if (!lastEventItem.getOrder().equals(EventItem.SOLD.getOrder())) {
            throw new BusinessException("此卡状态异常，请联销售人员");
        }

        TsConsumerApplyPo applyPo = new TsConsumerApplyPo();
        applyPo.setCardNo(req.getCardNo());
        applyPo.setPhone(req.getPhone());
        applyPo.setRealName(req.getRealName());
        applyPo.setAddressInfo(req.getAddressInfo());
        applyPo.setExpectedDate(req.getExpectedDate());
        applyPo.setDescription(req.getDescription());
        tsConsumerApplyMapper.insert(applyPo);

        TlCardEventLogPo eventLogPo = new TlCardEventLogPo();
        eventLogPo.setCardNo(req.getCardNo());
        eventLogPo.setEventItem(EventItem.APPLY.getCode());
        eventLogPo.setDescription(req.getDescription());
        tlCardEventLogMapper.insert(eventLogPo);

        cardPo.setCurrentEventItem(EventItem.APPLY.getCode());
        tsCardMapper.updateById(cardPo);
        return true;
    }

    @Transactional
    public Boolean process(ApplyProcessReq req) {
        TsCardPo tsCardPo = tsCardMapper.selectById(req.getCardId());
        if (tsCardPo == null) {
            throw new BusinessException("卡不存在");
        }

        List<TlCardEventLogPo> eventLogPoList = tlCardEventLogMapper.selectList(new QueryWrapper<TlCardEventLogPo>().eq("card_no", tsCardPo.getCardNo()).orderByAsc("created_at"));
        TlCardEventLogPo lastPo = eventLogPoList.get(eventLogPoList.size() - 1);
        EventItem lastEventItem = EventItem.getByCode(lastPo.getEventItem());
        EventItem newEventItem = EventItem.getByCode(req.getEventItemCode());

        if (newEventItem.getOrder() != lastEventItem.getOrder() + 1) {
            throw new BusinessException("操作错误");
        }

        if (newEventItem.getCode().equals(EventItem.APPLY.getCode())) {
            // 插入申请记录
            TsConsumerApplyPo applyPo = new TsConsumerApplyPo();
            applyPo.setCardNo(tsCardPo.getCardNo());
            applyPo.setPhone(req.getPhone());
            applyPo.setRealName(req.getRealName());
            applyPo.setAddressInfo(req.getAddressInfo());
            applyPo.setExpectedDate(req.getExpectedDate());
            applyPo.setDescription(req.getDescription());
            tsConsumerApplyMapper.insert(applyPo);
        }

        TlCardEventLogPo eventLogPo = new TlCardEventLogPo();
        eventLogPo.setCardNo(tsCardPo.getCardNo());
        eventLogPo.setEventItem(newEventItem.getCode());
        eventLogPo.setDescription(req.getDescription());
        tlCardEventLogMapper.insert(eventLogPo);

        tsCardPo.setCurrentEventItem(newEventItem.getCode());
        tsCardMapper.updateById(tsCardPo);
        return true;
    }
}
