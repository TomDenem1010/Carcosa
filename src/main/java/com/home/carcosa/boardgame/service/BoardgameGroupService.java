package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.dto.BoardgameGroupDto;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import com.home.carcosa.boardgame.mapper.BoardgameGroupMapper;
import com.home.carcosa.boardgame.repository.BoardgameGroupRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardgameGroupService {

    private final BoardgameGroupRepository boardgameGroupRepository;
    private final BoardgameGroupMapper boardgameGroupMapper;

    @Transactional
    public BoardgameGroupDto save(BoardgameGroupDto input) {
        BoardgameGroup entity = boardgameGroupMapper.toEntity(input);
        BoardgameGroup saved = boardgameGroupRepository.save(entity);
        return boardgameGroupMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<BoardgameGroupDto> findAll() {
        return boardgameGroupRepository.findAll().stream()
                .map(boardgameGroupMapper::toDto)
                .toList();
    }
}
