package com.home.carcosa.boardgame.dto;

import java.time.LocalDateTime;

public record BoardgameGroupDto(
        Long id,
        Long groupId,
        Long boardgameId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
