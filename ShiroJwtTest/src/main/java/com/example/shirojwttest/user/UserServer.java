package com.example.shirojwttest.user;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServer {

    @Resource
    UserMapper mapper;

    public User queryUser(String username, String password){
        return mapper.queryUser(username,password);
    }
}
