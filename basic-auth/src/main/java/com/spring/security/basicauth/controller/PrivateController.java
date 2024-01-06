package com.spring.security.basicauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {

    @GetMapping
    public String helloWorld() {
        return "Hello World! from private endpoint";
    }

    // @PreAuthorize("hasRole('USER')") EndPoint seviyesinde rol verme islemi bu sekilde yapilir
    @GetMapping("/user")
    public String helloWorldUserPrivate() {
        return "Hello World! from user private endpoint";
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String helloWorldAdminPrivate() {
        return "Hello World! from admin private endpoint";
    }
}
