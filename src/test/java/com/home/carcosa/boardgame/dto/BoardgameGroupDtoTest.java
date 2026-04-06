package com.home.carcosa.boardgame.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardgameGroupDtoTest {

    static Stream<BoardgameGroupDto> dtos() {
        return Stream.of(
                new BoardgameGroupDto(1L, 10L, 100L, null, null),
                new BoardgameGroupDto(null, 20L, 200L, null, null),
                new BoardgameGroupDto(3L, 1L, 999L, null, null));
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void accessors_returnConstructorValues(BoardgameGroupDto dto) {
        assertNotNull(dto.groupId());
        assertNotNull(dto.boardgameId());
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void equalInstances_withSameFields_areEqual(BoardgameGroupDto original) {
        BoardgameGroupDto copy = new BoardgameGroupDto(
                original.id(), original.groupId(), original.boardgameId(),
                original.createdAt(), original.updatedAt());
        assertEquals(original, copy);
    }

    @Test
    void instances_withDifferentGroupIds_areNotEqual() {
        BoardgameGroupDto a = new BoardgameGroupDto(1L, 10L, 100L, null, null);
        BoardgameGroupDto b = new BoardgameGroupDto(1L, 99L, 100L, null, null);
        assertNotEquals(a, b);
    }

    @Test
    void instances_withDifferentBoardgameIds_areNotEqual() {
        BoardgameGroupDto a = new BoardgameGroupDto(1L, 10L, 100L, null, null);
        BoardgameGroupDto b = new BoardgameGroupDto(1L, 10L, 999L, null, null);
        assertNotEquals(a, b);
    }

    @Test
    void timestamps_whenProvided_areReturnedByAccessors() {
        LocalDateTime now = LocalDateTime.of(2024, 6, 1, 0, 0);
        BoardgameGroupDto dto = new BoardgameGroupDto(1L, 1L, 1L, now, now);
        assertEquals(now, dto.createdAt());
        assertEquals(now, dto.updatedAt());
    }
}
