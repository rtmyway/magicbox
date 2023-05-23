package com.manmande.magicbox.app.card.controller;


import com.manmande.magicbox.app.card.po.TsCardPo;
import com.manmande.magicbox.app.card.service.CardService;
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

}
