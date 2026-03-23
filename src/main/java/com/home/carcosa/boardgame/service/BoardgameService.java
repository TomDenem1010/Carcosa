package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.dto.BoardgameDto;
import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.mapper.BoardgameMapper;
import com.home.carcosa.boardgame.repository.BoardgameRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Sort;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BoardgameService {

    private final BoardgameRepository boardgameRepository;
    private final BoardgameMapper boardgameMapper;

    @Transactional
    public BoardgameDto save(BoardgameDto input) {
        log.debug("Saving boardgame: {}", input);
        if (input.id() != null) {
            Boardgame existing = boardgameRepository.findById(input.id())
                    .orElseThrow(() -> new IllegalArgumentException("Boardgame not found: id=" + input.id()));
            existing.setName(input.name());
            existing.setType(input.type());
            existing.setStatus(input.status());
            existing.setBggLink(input.bggLink());
            Boardgame saved = boardgameRepository.save(existing);
            log.debug("Boardgame saved: {}", saved);
            return boardgameMapper.toDto(saved);
        }

        Boardgame entity = boardgameMapper.toEntity(input);
        Boardgame saved = boardgameRepository.save(entity);
        log.debug("Boardgame saved: {}", saved);
        return boardgameMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<BoardgameDto> findAll() {
        log.debug("Finding all boardgames");
        return boardgameRepository.findAll().stream()
                .map(boardgameMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Boardgame> findAll(Sort sort) {
        log.debug("Finding all boardgames with sort: {}", sort);
        return boardgameRepository.findAll(sort);
    }

    @Transactional(readOnly = true)
    public List<Boardgame> findByNameContainingIgnoreCase(String name, Sort sort) {
        log.debug("Finding boardgames with name containing '{}' and sort: {}", name, sort);
        return boardgameRepository.findByNameContainingIgnoreCase(name, sort);
    }
}
