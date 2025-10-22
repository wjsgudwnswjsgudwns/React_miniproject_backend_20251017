package com.jhj.miniproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping({
        "/", 
        "/login", 
        "/signup", 
        "/board/**", 
        "/tactics/**", 
        "/buy", 
        "/payment", 
        "/myorder"
    })
    public String forward() {
        return "forward:/index.html";
    }
}