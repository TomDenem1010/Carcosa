package com.home.carcosa.boardgame.mapper;

import org.springframework.stereotype.Component;

import com.home.carcosa.boardgame.dto.BoardgameDto;
import com.home.carcosa.boardgame.entity.Boardgame;

@Component
public final class BoardgameMapper {

    public BoardgameDto toDto(Boardgame entity) {
        return new BoardgameDto(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getStatus(),
                entity.getBggLink(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public Boardgame toEntity(BoardgameDto dto) {
        Boardgame entity = new Boardgame();
        entity.setId(dto.id());
        entity.setName(dto.name());
        entity.setType(dto.type());
        entity.setStatus(dto.status());
        entity.setBggLink(dto.bggLink());
        entity.setCreatedAt(dto.createdAt());
        entity.setUpdatedAt(dto.updatedAt());
        return entity;
    }
}
