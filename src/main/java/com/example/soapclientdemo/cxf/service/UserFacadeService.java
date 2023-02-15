package com.example.soapclientdemo.cxf.service;

import com.example.soapdemo.cxf.ws.User;
import com.example.soapdemo.cxf.ws.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserFacadeService {
    @Resource
    private UserService userService;

    public String getUsername(String userId) {
        String userName = userService.getUserName(userId);
        return userName;
    }

    public User getUser(String userId) {
        User user = userService.getUser(userId);
        return user;
    }

    public List<Object> getUserList(String userId) {
        List<Object> userList = userService.getUserList(userId);
        return userList;
    }
}
