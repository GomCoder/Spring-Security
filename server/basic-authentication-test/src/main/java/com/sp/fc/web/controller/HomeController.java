package com.sp.fc.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/greeting")
    public String greeting() {
        return "Hello";
    }

    /**
     * 서버에 데이터의 변경을 요청한다거나 변경을 가하는 작업
     * @return
     */
    @PostMapping("/greeting")
    public String greeting(@RequestBody String name) {
        return "Hello " + name;
    }
}
