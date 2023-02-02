package com.hackyle.template.service01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello01Controller {
    @GetMapping("/service01")
    public String service01() {
        return "service01";
    }

}
