package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import com.home.carcosa.boardgame.entity.BoardgamePlay;
import com.home.carcosa.boardgame.service.BoardgameGroupService;
import com.home.carcosa.boardgame.service.BoardgamePlayService;
import com.home.carcosa.boardgame.service.BoardgameService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/boardgame")
public class BoardgamePageController {

    private final BoardgameService boardgameService;
    private final BoardgameGroupService boardgameGroupService;
    private final BoardgamePlayService boardgamePlayService;

    public BoardgamePageController(
            BoardgameService boardgameService,
            BoardgameGroupService boardgameGroupService,
            BoardgamePlayService boardgamePlayService) {
        this.boardgameService = boardgameService;
        this.boardgameGroupService = boardgameGroupService;
        this.boardgamePlayService = boardgamePlayService;
    }

    @GetMapping("/boardgames")
    public String boardgames(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir,
            Model model) {
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortSpec = Sort.by(direction, "name");

        List<Boardgame> boardgames;
        if (name != null && !name.isBlank()) {
            boardgames = boardgameService.findByNameContainingIgnoreCase(name.trim(), sortSpec);
        } else {
            boardgames = boardgameService.findAll(sortSpec);
        }
        model.addAttribute("activeMenu", "boardgame");
        model.addAttribute("activeSubmenu", "boardgames");
        model.addAttribute("boardgames", boardgames);
        model.addAttribute("name", name == null ? "" : name);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", direction == Sort.Direction.ASC ? "asc" : "desc");
        return "boardgame/boardgames";
    }

    @GetMapping("/group")
    public String group(Model model) {
        List<Boardgame> boardgames = boardgameService.findAll(Sort.by(Sort.Direction.ASC, "name"));
        List<BoardgameGroup> groups = boardgameGroupService.findAll(
                Sort.by(Sort.Order.asc("groupId"), Sort.Order.asc("boardgame.name")));

        Map<Long, List<BoardgameGroup>> byGroupId = new LinkedHashMap<>();
        for (BoardgameGroup group : groups) {
            byGroupId.computeIfAbsent(group.getGroupId(), ignored -> new java.util.ArrayList<>()).add(group);
        }
        model.addAttribute("activeMenu", "boardgame");
        model.addAttribute("activeSubmenu", "group");
        model.addAttribute("boardgames", boardgames);
        model.addAttribute("groupsByGroupId", byGroupId);
        return "boardgame/group";
    }

    @GetMapping("/play")
    public String play(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "sort", required = false, defaultValue = "playDate") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "desc") String dir,
            Model model) {
        List<Boardgame> boardgames = boardgameService.findAll(Sort.by(Sort.Direction.ASC, "name"));
        Sort.Direction direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProperty = mapSortProperty(sort);
        Sort sortSpec = Sort.by(direction, sortProperty);

        List<BoardgamePlay> plays;
        if (name != null && !name.isBlank()) {
            plays = boardgamePlayService.findByBoardgameNameContainingIgnoreCase(name.trim(), sortSpec);
        } else {
            plays = boardgamePlayService.findAll(sortSpec);
        }

        model.addAttribute("activeMenu", "boardgame");
        model.addAttribute("activeSubmenu", "play");
        model.addAttribute("boardgames", boardgames);
        model.addAttribute("plays", plays);
        model.addAttribute("name", name == null ? "" : name);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", direction == Sort.Direction.ASC ? "asc" : "desc");
        return "boardgame/play";
    }

    private static String mapSortProperty(String sort) {
        if (sort == null) {
            return "playDate";
        }
        String key = sort.trim().toLowerCase(Locale.ROOT);
        return switch (key) {
            case "id" -> "id";
            case "boardgame", "boardgamename", "name" -> "boardgame.name";
            case "playdate" -> "playDate";
            case "playercount" -> "playerCount";
            case "win" -> "win";
            case "funscore" -> "funScore";
            case "createdat" -> "createdAt";
            case "updatedat" -> "updatedAt";
            default -> "playDate";
        };
    }
}
