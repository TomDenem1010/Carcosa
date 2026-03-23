package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.dto.BoardgameGroupDto;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import com.home.carcosa.boardgame.mapper.BoardgameGroupMapper;
import com.home.carcosa.boardgame.repository.BoardgameGroupRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Sort;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BoardgameGroupService {

    private final BoardgameGroupRepository boardgameGroupRepository;
    private final BoardgameGroupMapper boardgameGroupMapper;

    @Transactional
    public BoardgameGroupDto save(BoardgameGroupDto input) {
        log.debug("Saving boardgame group: {}", input);
        BoardgameGroup entity = boardgameGroupMapper.toEntity(input);
        BoardgameGroup saved = boardgameGroupRepository.save(entity);
        return boardgameGroupMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<BoardgameGroupDto> findAll() {
        log.debug("Finding all boardgame groups");
        return boardgameGroupRepository.findAll().stream()
                .map(boardgameGroupMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BoardgameGroup> findAll(Sort sort) {
        log.debug("Finding all boardgame groups with sort: {}", sort);
        return boardgameGroupRepository.findAll(sort);
    }
}
