package com.manmande.magicbox.app.card.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manmande.magicbox.app.card.mapper.TsCardMapper;
import com.manmande.magicbox.app.card.po.TsCardPo;
import com.manmande.magicbox.core.action.BaseAction;
import com.manmande.magicbox.core.exception.BusinessException;
import com.manmande.magicbox.core.web.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService implements BaseAction<TsCardPo> {
    @Autowired
    private TsCardMapper tsCardMapper;

    @Override
    public Boolean add(TsCardPo tsCardPo) throws BusinessException {
        tsCardMapper.insert(tsCardPo);
        return true;
    }

    @Override
    public Boolean remove(TsCardPo tsCardPo) throws BusinessException {
        tsCardMapper.deleteById(tsCardPo.getId());
        return true;
    }

    @Override
    public Boolean update(TsCardPo tsCardPo) throws BusinessException {
        tsCardMapper.updateById(tsCardPo);
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
    public PageDto listPage(PageDto pageDto) throws BusinessException {
        return null;
    }
}
