package com.example.soapclientdemo.cxf.config;

import com.example.soapdemo.cxf.ws.UserService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfClientConfig {
    @Value("${server.serviceUrl}")
    private String serviceUrl;

    @Bean
    public UserService userService() throws Exception {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(UserService.class);
        jaxWsProxyFactoryBean.setAddress(serviceUrl);

        UserService userService = (UserService) jaxWsProxyFactoryBean.create();
        return userService;
    }
}
