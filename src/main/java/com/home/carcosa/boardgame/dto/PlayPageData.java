package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgamePlay;

import java.util.List;

public record PlayPageData(List<Boardgame> boardgames, List<BoardgamePlay> plays, String name, String sort,
        String dir) {
}
