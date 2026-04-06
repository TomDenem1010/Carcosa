package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.dto.BoardgameGroupDto;
import com.home.carcosa.boardgame.service.BoardgameGroupService;
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
class BoardgameGroupControllerTest {

    @Mock
    BoardgameGroupService boardgameGroupService;
    @InjectMocks
    BoardgameGroupController controller;

    static Stream<BoardgameGroupDto> groupDtos() {
        return Stream.of(
                new BoardgameGroupDto(1L, 10L, 100L, null, null),
                new BoardgameGroupDto(null, 20L, 200L, null, null),
                new BoardgameGroupDto(3L, 1L, 999L, null, null));
    }

    @ParameterizedTest
    @MethodSource("groupDtos")
    void save_delegatesToServiceAndReturnsResult(BoardgameGroupDto dto) {
        when(boardgameGroupService.save(dto)).thenReturn(dto);
        assertEquals(dto, controller.save(dto));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 5 })
    void findAll_returnsListOfSizeFromService(int size) {
        List<BoardgameGroupDto> list = Collections.nCopies(size, new BoardgameGroupDto(1L, 1L, 1L, null, null));
        when(boardgameGroupService.findAll()).thenReturn(list);
        assertEquals(list, controller.findAll());
    }
}
