package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.entity.Boardgame;

import java.util.List;

public record BoardgamesPageData(List<Boardgame> boardgames, String name, String sort, String dir) {
}
