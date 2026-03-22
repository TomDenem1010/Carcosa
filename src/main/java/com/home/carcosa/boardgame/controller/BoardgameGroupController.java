package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.dto.BoardgameGroupDto;
import com.home.carcosa.boardgame.service.BoardgameGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boardgameGroup")
@RequiredArgsConstructor
public class BoardgameGroupController {

    private final BoardgameGroupService boardgameGroupService;

    @PostMapping("/save")
    public BoardgameGroupDto save(@RequestBody BoardgameGroupDto input) {
        return boardgameGroupService.save(input);
    }

    @GetMapping("/findAll")
    public List<BoardgameGroupDto> findAll() {
        return boardgameGroupService.findAll();
    }
}
