package com.manmande.magicbox.app.card.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manmande.magicbox.app.card.dto.ApplyDetailDto;
import com.manmande.magicbox.app.card.mapper.TsCardMapper;
import com.manmande.magicbox.app.card.po.TsCardPo;
import com.manmande.magicbox.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyService {
    @Autowired
    private TsCardMapper tsCardMapper;

    public ApplyDetailDto load(String cardNo, String cardPassword) {
        List<TsCardPo> poList = tsCardMapper.selectList(new QueryWrapper<TsCardPo>().eq("card_no", cardNo).eq("card_password", cardPassword));
        if (poList.isEmpty()) {
            throw new BusinessException("卡号或密码错误");
        }




        return null;
    }
}
