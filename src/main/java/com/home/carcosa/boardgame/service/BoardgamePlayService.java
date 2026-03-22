package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.dto.BoardgamePlayDto;
import com.home.carcosa.boardgame.entity.BoardgamePlay;
import com.home.carcosa.boardgame.mapper.BoardgamePlayMapper;
import com.home.carcosa.boardgame.repository.BoardgamePlayRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardgamePlayService {

    private final BoardgamePlayRepository boardgamePlayRepository;
    private final BoardgamePlayMapper boardgamePlayMapper;

    @Transactional
    public BoardgamePlayDto save(BoardgamePlayDto input) {
        BoardgamePlay entity = boardgamePlayMapper.toEntity(input);
        BoardgamePlay saved = boardgamePlayRepository.save(entity);
        return boardgamePlayMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<BoardgamePlayDto> findAll() {
        return boardgamePlayRepository.findAll().stream()
                .map(boardgamePlayMapper::toDto)
                .toList();
    }
}
