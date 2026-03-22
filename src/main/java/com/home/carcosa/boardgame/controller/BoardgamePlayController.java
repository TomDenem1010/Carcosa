package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.dto.BoardgamePlayDto;
import com.home.carcosa.boardgame.service.BoardgamePlayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boardgamePlay")
@RequiredArgsConstructor
public class BoardgamePlayController {

    private final BoardgamePlayService boardgamePlayService;

    @PostMapping("/save")
    public BoardgamePlayDto save(@RequestBody BoardgamePlayDto input) {
        return boardgamePlayService.save(input);
    }

    @GetMapping("/findAll")
    public List<BoardgamePlayDto> findAll() {
        return boardgamePlayService.findAll();
    }
}
