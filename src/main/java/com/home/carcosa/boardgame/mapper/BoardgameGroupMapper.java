package com.home.carcosa.boardgame.mapper;

import com.home.carcosa.boardgame.dto.BoardgameGroupDto;
import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import com.home.carcosa.boardgame.repository.BoardgameRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BoardgameGroupMapper {

    private final BoardgameRepository boardgameRepository;

    public BoardgameGroupDto toDto(BoardgameGroup entity) {
        return new BoardgameGroupDto(
                entity.getId(),
                entity.getBoardgame().getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public BoardgameGroup toEntity(BoardgameGroupDto dto) {
        BoardgameGroup entity = new BoardgameGroup();
        entity.setId(dto.id());
        entity.setBoardgame(boardgameRef(dto.boardgameId()));
        entity.setCreatedAt(dto.createdAt());
        entity.setUpdatedAt(dto.updatedAt());
        return entity;
    }

    private Boardgame boardgameRef(Long boardgameId) {
        return boardgameRepository.findById(boardgameId)
                .orElseThrow(() -> new IllegalArgumentException("Boardgame not found: " + boardgameId));
    }
}
