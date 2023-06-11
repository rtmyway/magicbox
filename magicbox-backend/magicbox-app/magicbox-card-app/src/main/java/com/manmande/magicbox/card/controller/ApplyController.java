package com.manmande.magicbox.card.controller;

import com.manmande.magicbox.card.dto.ApplyDetailDto;
import com.manmande.magicbox.card.req.ApplyCommonReq;
import com.manmande.magicbox.card.req.ApplyProcessReq;
import com.manmande.magicbox.card.service.ApplyService;
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
    public ApplyDetailDto load(@RequestBody ApplyCommonReq req) {
        return applyService.load(req);
    }

    @PostMapping("add")
    public Boolean add(@RequestBody ApplyCommonReq req) {
        return applyService.add(req);
    }

    @PostMapping("process")
    public Boolean process(@RequestBody ApplyProcessReq req) {
        return applyService.process(req);
    }
}
