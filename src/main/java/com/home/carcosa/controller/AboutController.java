package com.home.carcosa.controller;

import com.home.carcosa.dto.AboutResponse;
import com.home.carcosa.service.AboutService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/about")
@AllArgsConstructor
public class AboutController{

    private final AboutService aboutService;

    @GetMapping("")
    public AboutResponse getAbout(){
        return aboutService.getAboutResponse();
    }
}
