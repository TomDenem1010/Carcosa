package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.dto.BoardgameDto;
import com.home.carcosa.boardgame.service.BoardgameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boardgame")
@RequiredArgsConstructor
public class BoardgameController {

    private final BoardgameService boardgameService;

    @PostMapping("/save")
    public BoardgameDto save(@RequestBody BoardgameDto input) {
        return boardgameService.save(input);
    }

    @GetMapping("/findAll")
    public List<BoardgameDto> findAll() {
        return boardgameService.findAll();
    }
}
