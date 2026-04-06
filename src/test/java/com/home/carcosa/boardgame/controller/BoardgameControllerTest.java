package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;
import com.home.carcosa.boardgame.dto.BoardgameDto;
import com.home.carcosa.boardgame.service.BoardgameService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgameControllerTest {

    @Mock
    BoardgameService boardgameService;
    @InjectMocks
    BoardgameController controller;

    static Stream<BoardgameDto> boardgameDtos() {
        return Stream.of(
                new BoardgameDto(1L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "http://a", null, null),
                new BoardgameDto(2L, "Catan", BoardgameType.EXPANSION, BoardgameStatus.SOLD, "http://b", null, null),
                new BoardgameDto(null, "Go", BoardgameType.BASEGAME, BoardgameStatus.THINKINGTOBUY, "http://c", null,
                        null));
    }

    @ParameterizedTest
    @MethodSource("boardgameDtos")
    void save_delegatesToServiceAndReturnsResult(BoardgameDto dto) {
        when(boardgameService.save(dto)).thenReturn(dto);
        assertEquals(dto, controller.save(dto));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 3 })
    void findAll_returnsListOfSizeFromService(int size) {
        BoardgameDto dto = new BoardgameDto(1L, "X", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", null, null);
        List<BoardgameDto> list = Collections.nCopies(size, dto);
        when(boardgameService.findAll()).thenReturn(list);
        assertEquals(list, controller.findAll());
    }
}
