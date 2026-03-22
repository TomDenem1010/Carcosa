package com.home.carcosa.boardgame.mapper;

import com.home.carcosa.boardgame.dto.BoardgamePlayDto;
import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgamePlay;
import com.home.carcosa.boardgame.repository.BoardgameRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BoardgamePlayMapper {

    private final BoardgameRepository boardgameRepository;

    public BoardgamePlayDto toDto(BoardgamePlay entity) {
        return new BoardgamePlayDto(
                entity.getId(),
                entity.getBoardgame().getId(),
                entity.getPlayDate(),
                entity.getPlayerCount(),
                entity.getWin(),
                entity.getFunScore(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public BoardgamePlay toEntity(BoardgamePlayDto dto) {
        BoardgamePlay entity = new BoardgamePlay();
        entity.setId(dto.id());
        entity.setBoardgame(boardgameRef(dto.boardgameId()));
        entity.setPlayDate(dto.playDate());
        entity.setPlayerCount(dto.playerCount());
        entity.setWin(dto.win());
        entity.setFunScore(dto.funScore());
        entity.setCreatedAt(dto.createdAt());
        entity.setUpdatedAt(dto.updatedAt());
        return entity;
    }

    private Boardgame boardgameRef(Long boardgameId) {
        return boardgameRepository.findById(boardgameId)
                .orElseThrow(() -> new IllegalArgumentException("Boardgame not found: " + boardgameId));
    }
}
