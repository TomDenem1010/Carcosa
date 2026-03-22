package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BoardgamePlayDto(
        Long id,
        Long boardgameId,
        LocalDate playDate,
        Integer playerCount,
        BoardgamePlayWin win,
        Integer funScore,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
