package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.dto.BoardgamesPageData;
import com.home.carcosa.boardgame.dto.GroupPageData;
import com.home.carcosa.boardgame.dto.PlayPageData;
import com.home.carcosa.boardgame.constans.BoardgamePlayWin;
import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;
import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgameGroup;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BoardgamePageService {

    private final BoardgameService boardgameService;
    private final BoardgameGroupService boardgameGroupService;
    private final BoardgamePlayService boardgamePlayService;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardgamePage.enums.types")
    public List<BoardgameType> boardgameTypes() {
        return List.of(BoardgameType.values());
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardgamePage.enums.statuses")
    public List<BoardgameStatus> boardgameStatuses() {
        return List.of(BoardgameStatus.values());
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardgamePage.enums.playWins")
    public List<BoardgamePlayWin> boardgamePlayWins() {
        return List.of(BoardgamePlayWin.values());
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardgamePage.boardgames", key = "T(org.springframework.util.StringUtils).trimWhitespace(#name == null ? '' : #name).toLowerCase()"
            + "+ '|' + (#sort == null ? '' : #sort.trim().toLowerCase())"
            + "+ '|' + (#dir == null ? '' : #dir.trim().toLowerCase())", sync = true)
    public BoardgamesPageData boardgamesPageData(String name, String sort, String dir) {
        Sort.Direction direction = directionOf(dir, Sort.Direction.ASC);
        Sort sortSpec = Sort.by(direction, mapBoardgameSortProperty(sort));

        BoardgamesPageData data = new BoardgamesPageData(
                hasText(name)
                        ? boardgameService.findByNameContainingIgnoreCase(name.trim(), sortSpec)
                        : boardgameService.findAll(sortSpec),
                trimToEmpty(name),
                defaultIfBlank(sort, "name"),
                directionValue(direction));

        log.debug("Boardgames page data built: name='{}', sort='{}', dir='{}', count={}",
                data.name(),
                data.sort(),
                data.dir(),
                data.boardgames().size());
        return data;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardgamePage.group", sync = true)
    public GroupPageData groupPageData() {
        List<BoardgameGroup> groups = boardgameGroupService.findAll(
                Sort.by(Sort.Order.asc("groupId"), Sort.Order.asc("boardgame.name")));

        long nextGroupId = groups.stream()
                .map(BoardgameGroup::getGroupId)
                .filter(Objects::nonNull)
                .max(Long::compareTo)
                .map(max -> max + 1)
                .orElse(1L);

        Set<Long> groupedBoardgameIds = groups.stream()
                .map(BoardgameGroup::getBoardgame)
                .filter(Objects::nonNull)
                .map(Boardgame::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        GroupPageData data = new GroupPageData(
                boardgameService.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                        .filter(bg -> bg.getId() != null && !groupedBoardgameIds.contains(bg.getId()))
                        .toList(),
                groups.stream().collect(Collectors.groupingBy(
                        BoardgameGroup::getGroupId,
                        LinkedHashMap::new,
                        Collectors.toList())),
                nextGroupId);

        log.debug("Group page data built: groups={}, availableBoardgames={}, nextGroupId={}", groups.size(),
                data.boardgames().size(), data.nextGroupId());

        return data;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "boardgamePage.play", key = "T(org.springframework.util.StringUtils).trimWhitespace(#name == null ? '' : #name).toLowerCase()"
            + "+ '|' + (#sort == null ? '' : #sort.trim().toLowerCase())"
            + "+ '|' + (#dir == null ? '' : #dir.trim().toLowerCase())", sync = true)
    public PlayPageData playPageData(String name, String sort, String dir) {
        Sort.Direction direction = directionOf(dir, Sort.Direction.DESC);
        Sort sortSpec = Sort.by(direction, mapPlaySortProperty(sort));

        PlayPageData data = new PlayPageData(
                boardgameService.findAll(Sort.by(Sort.Direction.ASC, "name")),
                hasText(name)
                        ? boardgamePlayService.findByBoardgameNameContainingIgnoreCase(name.trim(), sortSpec)
                        : boardgamePlayService.findAll(sortSpec),
                trimToEmpty(name),
                defaultIfBlank(sort, "playDate"),
                directionValue(direction));

        log.debug("Play page data built: name='{}', sort='{}', dir='{}', plays={}", data.name(), data.sort(),
                data.dir(), data.plays().size());

        return data;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private static String defaultIfBlank(String value, String defaultValue) {
        return hasText(value) ? value : defaultValue;
    }

    private static String directionValue(Sort.Direction direction) {
        return direction == Sort.Direction.ASC ? "asc" : "desc";
    }

    private static Sort.Direction directionOf(String dir, Sort.Direction defaultDirection) {
        if (dir == null) {
            return defaultDirection;
        }
        if ("asc".equalsIgnoreCase(dir)) {
            return Sort.Direction.ASC;
        }
        if ("desc".equalsIgnoreCase(dir)) {
            return Sort.Direction.DESC;
        }
        return defaultDirection;
    }

    private static String mapBoardgameSortProperty(String sort) {
        if (sort == null) {
            return "name";
        }
        String key = sort.trim().toLowerCase(Locale.ROOT);
        return switch (key) {
            case "id" -> "id";
            case "name" -> "name";
            case "type" -> "type";
            case "status" -> "status";
            case "createdat" -> "createdAt";
            case "updatedat" -> "updatedAt";
            default -> "name";
        };
    }

    private static String mapPlaySortProperty(String sort) {
        if (sort == null) {
            return "playDate";
        }
        String key = sort.trim().toLowerCase(Locale.ROOT);
        return switch (key) {
            case "id" -> "id";
            case "boardgame", "boardgamename", "name" -> "boardgame.name";
            case "playdate" -> "playDate";
            case "playercount" -> "playerCount";
            case "win" -> "win";
            case "funscore" -> "funScore";
            case "createdat" -> "createdAt";
            case "updatedat" -> "updatedAt";
            default -> "playDate";
        };
    }

}
