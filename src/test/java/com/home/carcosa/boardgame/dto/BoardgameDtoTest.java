package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardgameDtoTest {

    static Stream<BoardgameDto> dtos() {
        return Stream.of(
                new BoardgameDto(1L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "http://a", null, null),
                new BoardgameDto(2L, "Catan", BoardgameType.EXPANSION, BoardgameStatus.SOLD, "http://b", null, null),
                new BoardgameDto(null, "Go", BoardgameType.BASEGAME, BoardgameStatus.THINKINGTOBUY, "http://c", null,
                        null));
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void accessors_returnConstructorValues(BoardgameDto dto) {
        assertNotNull(dto.name());
        assertNotNull(dto.type());
        assertNotNull(dto.status());
        assertNotNull(dto.bggLink());
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void equalInstances_withSameFields_areEqual(BoardgameDto original) {
        BoardgameDto copy = new BoardgameDto(
                original.id(), original.name(), original.type(), original.status(),
                original.bggLink(), original.createdAt(), original.updatedAt());
        assertEquals(original, copy);
    }

    @Test
    void instances_withDifferentIds_areNotEqual() {
        BoardgameDto a = new BoardgameDto(1L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", null, null);
        BoardgameDto b = new BoardgameDto(2L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", null, null);
        assertNotEquals(a, b);
    }

    @Test
    void instances_withDifferentNames_areNotEqual() {
        BoardgameDto a = new BoardgameDto(1L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", null, null);
        BoardgameDto b = new BoardgameDto(1L, "Catan", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", null, null);
        assertNotEquals(a, b);
    }

    @Test
    void timestamps_whenProvided_areReturnedByAccessors() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);
        BoardgameDto dto = new BoardgameDto(1L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", now, now);
        assertEquals(now, dto.createdAt());
        assertEquals(now, dto.updatedAt());
    }
}
