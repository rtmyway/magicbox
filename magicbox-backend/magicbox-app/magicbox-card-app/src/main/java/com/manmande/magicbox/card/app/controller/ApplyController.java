package com.manmande.magicbox.app.card.controller;

import com.manmande.magicbox.app.card.dto.ApplyDetailDto;
import com.manmande.magicbox.app.card.po.TsCardPo;
import com.manmande.magicbox.app.card.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("apply")
public class ApplyController {
    @Autowired
    private ApplyService applyService;
    @PostMapping("load")
    public ApplyDetailDto load(@RequestBody TsCardPo tsCardPo) {
        return applyService.load(tsCardPo.getCardNo(), tsCardPo.getCardPassword());
    }
}
