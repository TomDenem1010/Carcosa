package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;
import com.home.carcosa.boardgame.dto.BoardgamePlayDto;
import com.home.carcosa.boardgame.service.BoardgamePlayService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgamePlayControllerTest {

    @Mock
    BoardgamePlayService boardgamePlayService;
    @InjectMocks
    BoardgamePlayController controller;

    static Stream<BoardgamePlayDto> playDtos() {
        return Stream.of(
                new BoardgamePlayDto(1L, 10L, LocalDate.of(2024, 1, 1), 2, BoardgamePlayWin.SUCCESS, 5, null, null),
                new BoardgamePlayDto(2L, 20L, LocalDate.of(2024, 6, 15), 4, BoardgamePlayWin.FAILURE, 3, null, null),
                new BoardgamePlayDto(null, 30L, LocalDate.of(2025, 12, 31), 1, BoardgamePlayWin.SUCCESS, 4, null,
                        null));
    }

    @ParameterizedTest
    @MethodSource("playDtos")
    void save_delegatesToServiceAndReturnsResult(BoardgamePlayDto dto) {
        when(boardgamePlayService.save(dto)).thenReturn(dto);
        assertEquals(dto, controller.save(dto));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 5 })
    void findAll_returnsListOfSizeFromService(int size) {
        BoardgamePlayDto dto = new BoardgamePlayDto(1L, 1L, LocalDate.now(), 2, BoardgamePlayWin.SUCCESS, 5, null,
                null);
        List<BoardgamePlayDto> list = Collections.nCopies(size, dto);
        when(boardgamePlayService.findAll()).thenReturn(list);
        assertEquals(list, controller.findAll());
    }
}
