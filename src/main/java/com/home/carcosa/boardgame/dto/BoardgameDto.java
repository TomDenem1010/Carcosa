package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;

import java.time.LocalDateTime;

public record BoardgameDto(
        Long id,
        String name,
        BoardgameType type,
        BoardgameStatus status,
        String bggLink,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
