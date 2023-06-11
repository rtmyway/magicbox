package com.manmande.magicbox.card.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manmande.magicbox.card.po.TsCardPo;
import org.apache.ibatis.annotations.Param;

public interface TsCardMapper extends BaseMapper<TsCardPo> {
    IPage<TsCardPo> selectCardPage(Page<?> page, @Param("searchValue") String searchValue);
}
