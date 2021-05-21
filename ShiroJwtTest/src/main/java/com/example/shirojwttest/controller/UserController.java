package com.example.shirojwttest.controller;

import com.example.shirojwttest.Utils.JwtUtil;
import com.example.shirojwttest.model.ResultMap;
import com.example.shirojwttest.shiro.JwtToken;
import com.example.shirojwttest.user.User;
import com.example.shirojwttest.user.UserServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class UserController {

    @RequestMapping({"/", "/index"})
    public String index() {
        return "首页";
    }

    @RequestMapping("/add")
    public String add() {
        return "添加";
    }

    @RequestMapping("/update")
    public String update() {
        return "更新";
    }

    @RequestMapping("/noRole")
    public String noRole() {
        return "没有权限";
    }

    @Resource
    UserServer userServer;

    @RequestMapping("/login")
    public Map<String, Object> login(String username, String password) {
        ResultMap map = new ResultMap();
        User user = userServer.queryUser(username, password);
        if (user == null) {
            map.fail().code(401).message("不存在此用户");
            return map;
        }
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        String token = JwtUtil.getToken(user);
        JwtToken jwtToken = new JwtToken(token);
        //执行登录方法，如果没有异常说明OK
        subject.login(jwtToken);
        map.success().code(200).message("登录成功").put("token", token);

        return map;
    }

}
