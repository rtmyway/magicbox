package com.manmande.magicbox.card.controller;


import com.manmande.magicbox.card.po.TsCardPo;
import com.manmande.magicbox.card.req.GenerateCardReq;
import com.manmande.magicbox.card.service.CardService;
import com.manmande.magicbox.core.web.dto.PageDto;
import com.manmande.magicbox.core.web.req.PageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("card")
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping("generate")
    public Boolean generate(@RequestBody GenerateCardReq generateCardReq) {
        return cardService.generate(generateCardReq);
    }

    @PostMapping("add")
    public Boolean add(@RequestBody TsCardPo tsCardPo) {
        return cardService.add(tsCardPo);
    }

    @PostMapping("update")
    public Boolean update(@RequestBody TsCardPo tsCardPo) {
        return cardService.update(tsCardPo);
    }

    @PostMapping("remove")
    public Boolean remove(@RequestBody TsCardPo tsCardPo) {
        return cardService.remove(tsCardPo);
    }

    @PostMapping("list-page")
    public PageDto listPage(@RequestBody PageReq pageReq) {
        return cardService.listPage(pageReq);
    }

}
