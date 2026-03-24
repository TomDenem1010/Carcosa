package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.service.BoardgamePageService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/boardgame")
@AllArgsConstructor
public class BoardgamePageController {

    private final BoardgamePageService boardgamePageService;

    @GetMapping("/boardgames")
    public String boardgames(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir,
            Model model) {
        model.addAttribute("activeMenu", "boardgame");
        model.addAttribute("activeSubmenu", "boardgames");
        model.addAttribute("boardgameTypes", boardgamePageService.boardgameTypes());
        model.addAttribute("boardgameStatuses", boardgamePageService.boardgameStatuses());
        model.addAttribute("boardgames", boardgamePageService.boardgamesPageData(name, sort, dir).boardgames());
        model.addAttribute("name", boardgamePageService.boardgamesPageData(name, sort, dir).name());
        model.addAttribute("sort", boardgamePageService.boardgamesPageData(name, sort, dir).sort());
        model.addAttribute("dir", boardgamePageService.boardgamesPageData(name, sort, dir).dir());
        return "boardgame/boardgames";
    }

    @GetMapping("/group")
    public String group(Model model) {
        model.addAttribute("activeMenu", "boardgame");
        model.addAttribute("activeSubmenu", "group");
        model.addAttribute("boardgames", boardgamePageService.groupPageData().boardgames());
        model.addAttribute("groupsByGroupId", boardgamePageService.groupPageData().groupsByGroupId());
        model.addAttribute("nextGroupId", boardgamePageService.groupPageData().nextGroupId());
        return "boardgame/group";
    }

    @GetMapping("/play")
    public String play(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "sort", required = false, defaultValue = "playDate") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "desc") String dir,
            Model model) {
        model.addAttribute("activeMenu", "boardgame");
        model.addAttribute("activeSubmenu", "play");
        model.addAttribute("boardgamePlayWins", boardgamePageService.boardgamePlayWins());
        model.addAttribute("boardgames", boardgamePageService.playPageData(name, sort, dir).boardgames());
        model.addAttribute("plays", boardgamePageService.playPageData(name, sort, dir).plays());
        model.addAttribute("name", boardgamePageService.playPageData(name, sort, dir).name());
        model.addAttribute("sort", boardgamePageService.playPageData(name, sort, dir).sort());
        model.addAttribute("dir", boardgamePageService.playPageData(name, sort, dir).dir());
        return "boardgame/play";
    }
}
