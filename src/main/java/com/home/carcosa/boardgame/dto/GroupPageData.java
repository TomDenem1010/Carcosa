package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgameGroup;

import java.util.List;
import java.util.Map;

public record GroupPageData(List<Boardgame> boardgames, Map<Long, List<BoardgameGroup>> groupsByGroupId,
        long nextGroupId) {
}
