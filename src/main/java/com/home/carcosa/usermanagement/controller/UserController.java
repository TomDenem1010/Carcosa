package com.home.carcosa.usermanagement.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.carcosa.usermanagement.dto.UserCreateRequest;
import com.home.carcosa.usermanagement.dto.UserCreateResponse;
import com.home.carcosa.usermanagement.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController{

    private final UserService userService;

    @PostMapping("/create")
    public UserCreateResponse createUser(@RequestBody UserCreateRequest request){
        return userService.createUser(request);
    }
}
