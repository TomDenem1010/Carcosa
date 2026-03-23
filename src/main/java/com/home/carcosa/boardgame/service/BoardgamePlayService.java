package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.dto.BoardgamePlayDto;
import com.home.carcosa.boardgame.entity.BoardgamePlay;
import com.home.carcosa.boardgame.mapper.BoardgamePlayMapper;
import com.home.carcosa.boardgame.repository.BoardgamePlayRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.domain.Sort;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BoardgamePlayService {

    private final BoardgamePlayRepository boardgamePlayRepository;
    private final BoardgamePlayMapper boardgamePlayMapper;

    @Transactional
    @CacheEvict(cacheNames = { "boardgamePlay.findAll.dto", "boardgamePlay.findAll.entity.sorted", "boardgamePlay.findByBoardgameName" }, allEntries = true)
    public BoardgamePlayDto save(BoardgamePlayDto input) {
        log.debug("Saving boardgame play: {}", input);
        BoardgamePlay entity = boardgamePlayMapper.toEntity(input);
        BoardgamePlay saved = boardgamePlayRepository.save(entity);
        log.debug("Boardgame play saved: {}", saved);
        return boardgamePlayMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardgamePlay.findAll.dto")
    public List<BoardgamePlayDto> findAll() {
        log.debug("Finding all boardgame plays");
        return boardgamePlayRepository.findAll().stream()
                .map(boardgamePlayMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardgamePlay.findAll.entity.sorted", key = "#sort == null ? 'null' : #sort.toString()")
    public List<BoardgamePlay> findAll(Sort sort) {
        log.debug("Finding all boardgame plays with sort: {}", sort);
        return boardgamePlayRepository.findAll(sort);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardgamePlay.findByBoardgameName", key = "#name + '|' + (#sort == null ? 'null' : #sort.toString())")
    public List<BoardgamePlay> findByBoardgameNameContainingIgnoreCase(String name, Sort sort) {
        log.debug("Finding boardgame plays with boardgame name containing '{}' and sort: {}", name, sort);
        return boardgamePlayRepository.findByBoardgame_NameContainingIgnoreCase(name, sort);
    }
}
