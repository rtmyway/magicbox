package com.manmande.magicbox.demo.controller;

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
        userMapper.insert(userPo);
        return userPo;
    }
}
