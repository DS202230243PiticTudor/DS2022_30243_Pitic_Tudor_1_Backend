package com.ds.management.web.controllers;

import com.ds.management.exception.domain.EmailExistException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class LoginController {
    @GetMapping("/login")
    public String login() throws EmailExistException {
        throw new EmailExistException("This email address is already taken");
    }
}
