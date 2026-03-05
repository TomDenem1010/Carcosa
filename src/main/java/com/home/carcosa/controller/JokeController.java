package com.home.carcosa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.carcosa.dto.JokeResponse;
import com.home.carcosa.service.JokeService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/joke")
@AllArgsConstructor
public class JokeController{

    private final JokeService jokeService;

    @GetMapping("")
    public JokeResponse getJoke(){
        return jokeService.getJoke();
    }
}
