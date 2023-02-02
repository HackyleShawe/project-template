package com.hackyle.template.service02.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello02Controller {
    @GetMapping("/service02")
    public String service02() {
        return "service02";
    }
}
