package com.example.comparedir.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public String testCache(HttpServletRequest request, String topicSubscribeKeys){

        String region = request.getHeader("region-code");
        String user = request.getHeader("user-id");
        return region + "-" + user;
    }

}
