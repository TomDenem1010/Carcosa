package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardgamePlayDtoTest {

    static Stream<BoardgamePlayDto> dtos() {
        return Stream.of(
                new BoardgamePlayDto(1L, 10L, LocalDate.of(2024, 1, 1), 2, BoardgamePlayWin.SUCCESS, 5, null, null),
                new BoardgamePlayDto(2L, 20L, LocalDate.of(2024, 6, 15), 4, BoardgamePlayWin.FAILURE, 3, null, null),
                new BoardgamePlayDto(null, 30L, LocalDate.of(2025, 12, 31), 1, BoardgamePlayWin.SUCCESS, 1, null,
                        null));
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void accessors_returnConstructorValues(BoardgamePlayDto dto) {
        assertNotNull(dto.boardgameId());
        assertNotNull(dto.playDate());
        assertNotNull(dto.playerCount());
        assertNotNull(dto.win());
        assertNotNull(dto.funScore());
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void equalInstances_withSameFields_areEqual(BoardgamePlayDto original) {
        BoardgamePlayDto copy = new BoardgamePlayDto(
                original.id(), original.boardgameId(), original.playDate(),
                original.playerCount(), original.win(), original.funScore(),
                original.createdAt(), original.updatedAt());
        assertEquals(original, copy);
    }

    @Test
    void instances_withDifferentWin_areNotEqual() {
        BoardgamePlayDto a = new BoardgamePlayDto(1L, 1L, LocalDate.now(), 2, BoardgamePlayWin.SUCCESS, 5, null, null);
        BoardgamePlayDto b = new BoardgamePlayDto(1L, 1L, LocalDate.now(), 2, BoardgamePlayWin.FAILURE, 5, null, null);
        assertNotEquals(a, b);
    }

    @Test
    void instances_withDifferentFunScore_areNotEqual() {
        BoardgamePlayDto a = new BoardgamePlayDto(1L, 1L, LocalDate.now(), 2, BoardgamePlayWin.SUCCESS, 5, null, null);
        BoardgamePlayDto b = new BoardgamePlayDto(1L, 1L, LocalDate.now(), 2, BoardgamePlayWin.SUCCESS, 1, null, null);
        assertNotEquals(a, b);
    }

    @Test
    void instances_withDifferentPlayerCount_areNotEqual() {
        BoardgamePlayDto a = new BoardgamePlayDto(1L, 1L, LocalDate.now(), 2, BoardgamePlayWin.SUCCESS, 5, null, null);
        BoardgamePlayDto b = new BoardgamePlayDto(1L, 1L, LocalDate.now(), 4, BoardgamePlayWin.SUCCESS, 5, null, null);
        assertNotEquals(a, b);
    }
}
