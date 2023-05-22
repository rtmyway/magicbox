package com.manmande.magicbox.demo.controller;

import com.manmande.magicbox.core.exception.BaseException;
import com.manmande.magicbox.core.exception.BusinessException;
import com.manmande.magicbox.demo.mapper.UserMapper;
import com.manmande.magicbox.demo.po.UserPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("test")
    public UserPo test() {
        UserPo userPo = new UserPo();
        userPo.setUsername("test1");
        userPo.setPassword("test2");
        userPo.setId("222");
//        userMapper.insert(userPo);
        if (userPo.getId() != null) {
            throw new BusinessException("ERROR_001", new String[]{"test1", "test2"});
        }
        return userPo;
    }
}
