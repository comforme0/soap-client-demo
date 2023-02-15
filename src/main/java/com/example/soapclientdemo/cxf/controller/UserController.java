package com.example.soapclientdemo.cxf.controller;

import com.example.soapclientdemo.cxf.service.UserFacadeService;
import com.example.soapdemo.cxf.ws.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user-client")
public class UserController {
    @Resource
    private UserFacadeService userFacadeService;

    @GetMapping("/get")
    public String getUsername(String userId){
        System.out.println("client#userId:" + userId);
        return userFacadeService.getUsername(userId);
    }

    @GetMapping("/getUser")
    public User getUser(String userId){
        System.out.println("client#userId:" + userId);
        return userFacadeService.getUser(userId);
    }

    @GetMapping("/getUserList")
    public List<Object> getUserList(String userId){
        System.out.println("client#userId:" + userId);
        return userFacadeService.getUserList(userId);
    }
}
