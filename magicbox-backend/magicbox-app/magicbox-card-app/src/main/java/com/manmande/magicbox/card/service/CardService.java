package com.manmande.magicbox.card.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manmande.magicbox.card.constant.CardItem;
import com.manmande.magicbox.card.constant.EventItem;
import com.manmande.magicbox.card.dto.CardDetailDto;
import com.manmande.magicbox.card.mapper.TlCardEventLogMapper;
import com.manmande.magicbox.card.mapper.TsCardMapper;
import com.manmande.magicbox.card.mapper.TsConsumerApplyMapper;
import com.manmande.magicbox.card.po.TlCardEventLogPo;
import com.manmande.magicbox.card.po.TsCardPo;
import com.manmande.magicbox.card.po.TsConsumerApplyPo;
import com.manmande.magicbox.card.req.GenerateCardReq;
import com.manmande.magicbox.core.action.BaseAction;
import com.manmande.magicbox.core.exception.BusinessException;
import com.manmande.magicbox.core.web.dto.PageDto;
import com.manmande.magicbox.core.web.req.PageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService implements BaseAction<TsCardPo> {
    @Autowired
    private TsCardMapper tsCardMapper;
    @Autowired
    private TsConsumerApplyMapper tsConsumerApplyMapper;
    @Autowired
    private TlCardEventLogMapper tlCardEventLogMapper;


    public Boolean generate(GenerateCardReq generateCardReq) {
        // 查出默认最大的卡号
        List<TsCardPo> poList = tsCardMapper.selectList(
                new QueryWrapper<TsCardPo>()
                        .eq("card_name", CardItem.DEFAULT.getText())
                        .orderByDesc("card_no")
                        .last("limit 1"));
        String batchNo = IdUtil.nanoId();
        Integer startCardNo = Integer.valueOf(CardItem.DEFAULT.getBeginNo());
        if (!poList.isEmpty()) {
            startCardNo = Integer.valueOf(poList.get(0).getCardNo()) + 1;
        }

        for (int i = 0; i < generateCardReq.getCnt(); i++) {
            TsCardPo tsCardPo = new TsCardPo();
            tsCardPo.setCardNo(String.valueOf(startCardNo + i));
            tsCardPo.setCardName(CardItem.DEFAULT.getText());
            tsCardPo.setCardPassword(RandomUtil.randomString(RandomUtil.BASE_NUMBER, 6));
            tsCardPo.setPrice(CardItem.DEFAULT.getPrice());
            tsCardPo.setCurrentEventItem(EventItem.INIT.getCode());
            tsCardPo.setBatchNo(batchNo);
            tsCardMapper.insert(tsCardPo);

            TlCardEventLogPo tlCardEventLogPo = new TlCardEventLogPo();
            tlCardEventLogPo.setCardNo(tsCardPo.getCardNo());
            tlCardEventLogPo.setEventItem(EventItem.INIT.getCode());
            tlCardEventLogMapper.insert(tlCardEventLogPo);
        }
        return true;
    }

    @Override
    public Boolean add(TsCardPo tsCardPo) throws BusinessException {
        List<TsCardPo> poList = tsCardMapper.selectList(new QueryWrapper<TsCardPo>().eq("card_no", tsCardPo.getCardNo()));
        if (poList.size() > 0) {
            throw new BusinessException("卡号已存在");
        }
        tsCardMapper.insert(tsCardPo);
        return true;
    }

    @Override
    public Boolean remove(TsCardPo tsCardPo) throws BusinessException {
        TsCardPo po = tsCardMapper.selectById(tsCardPo.getId());
        if (po != null) {
            tsConsumerApplyMapper.delete(new QueryWrapper<TsConsumerApplyPo>().eq("card_no", po.getCardNo()));
            tlCardEventLogMapper.delete(new QueryWrapper<TlCardEventLogPo>().eq("card_no", po.getCardNo()));
            tsCardMapper.deleteById(tsCardPo.getId());
        }


        return true;
    }

    @Override
    public Boolean update(TsCardPo tsCardPo) throws BusinessException {
        TsCardPo po = tsCardMapper.selectById(tsCardPo.getId());
        if (po != null) {
            po.setDescription(tsCardPo.getDescription());
            tsCardMapper.updateById(po);
        }
        return true;
    }

    @Override
    public Boolean enable(TsCardPo tsCardPo) throws BusinessException {
        return null;
    }

    @Override
    public Boolean disable(TsCardPo tsCardPo) throws BusinessException {
        return null;
    }

    @Override
    public List<TsCardPo> list() throws BusinessException {
        return tsCardMapper.selectList(new QueryWrapper<TsCardPo>());
    }

    @Override
    public PageDto listPage(PageReq pageReq) throws BusinessException {
        Page<TsCardPo> page = new Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        Object searchValueObj = pageReq.getParams().get("searchValue");
        Object eventItemObj = pageReq.getParams().get("eventItem");
        tsCardMapper.selectCardPage(
                page,
                searchValueObj == null ? "" : String.valueOf(searchValueObj),
                eventItemObj == null ? "" : String.valueOf(eventItemObj)
        );

        PageDto pageDto = new PageDto();
        pageDto.setTotal(page.getTotal());
        pageDto.setPageNum(pageReq.getPageNum());
        pageDto.setPageSize(pageReq.getPageSize());
        pageDto.setList(page.getRecords());

        if (page.getRecords().isEmpty()) {
            return pageDto;
        }

        List<String> cardNoList = page.getRecords().stream().map(TsCardPo::getCardNo).collect(Collectors.toList());
        List<TsConsumerApplyPo> applyPoList = tsConsumerApplyMapper.selectList(new QueryWrapper<TsConsumerApplyPo>().in("card_no", cardNoList));
        List<TlCardEventLogPo> eventLogPoList = tlCardEventLogMapper.selectList(new QueryWrapper<TlCardEventLogPo>().in("card_no", cardNoList));

        List<CardDetailDto> dtoList = new ArrayList<>();
        CardDetailDto dto;
        for (TsCardPo tsCardPo : page.getRecords()) {
            dto = new CardDetailDto();
            dto.setCardPo(tsCardPo);
            List<TsConsumerApplyPo> matchApplyPoList = applyPoList.stream().filter(applyPo -> applyPo.getCardNo().equals(tsCardPo.getCardNo())).collect(Collectors.toList());
            if (!matchApplyPoList.isEmpty()) {
                dto.setApplyPo(matchApplyPoList.get(0));
            }
            dto.setEventLogPoList(eventLogPoList.stream().filter(eventLogPo -> eventLogPo.getCardNo().equals(tsCardPo.getCardNo())).collect(Collectors.toList()));
            dtoList.add(dto);
        }
        pageDto.setList(dtoList);

        return pageDto;
    }


}
