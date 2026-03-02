package com.home.hastur.controller;

import com.home.hastur.dto.AboutResponse;
import com.home.hastur.service.AboutService;
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
