package com.home.carcosa.boardgame.mapper;

import com.home.carcosa.boardgame.dto.BoardgameGroupDto;
import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import com.home.carcosa.boardgame.repository.BoardgameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgameGroupMapperTest {

    @Mock
    BoardgameRepository boardgameRepository;
    @InjectMocks
    BoardgameGroupMapper mapper;

    static Stream<BoardgameGroupDto> dtos() {
        LocalDateTime ts = LocalDateTime.of(2024, 1, 1, 0, 0);
        return Stream.of(
                new BoardgameGroupDto(1L, 10L, 100L, ts, ts),
                new BoardgameGroupDto(2L, 20L, 200L, ts, ts),
                new BoardgameGroupDto(3L, 1L, 999L, ts, ts));
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void toDto_mapsAllFieldsFromEntity(BoardgameGroupDto dto) {
        Boardgame bg = Boardgame.builder().id(dto.boardgameId()).build();
        BoardgameGroup entity = BoardgameGroup.builder()
                .id(dto.id()).groupId(dto.groupId()).boardgame(bg)
                .createdAt(dto.createdAt()).updatedAt(dto.updatedAt()).build();

        BoardgameGroupDto result = mapper.toDto(entity);

        assertAll(
                () -> assertEquals(entity.getId(), result.id()),
                () -> assertEquals(entity.getGroupId(), result.groupId()),
                () -> assertEquals(entity.getBoardgame().getId(), result.boardgameId()),
                () -> assertEquals(entity.getCreatedAt(), result.createdAt()),
                () -> assertEquals(entity.getUpdatedAt(), result.updatedAt()));
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void toEntity_mapsAllFieldsFromDto(BoardgameGroupDto dto) {
        Boardgame bg = Boardgame.builder().id(dto.boardgameId()).build();
        when(boardgameRepository.findById(dto.boardgameId())).thenReturn(Optional.of(bg));

        BoardgameGroup entity = mapper.toEntity(dto);

        assertAll(
                () -> assertEquals(dto.id(), entity.getId()),
                () -> assertEquals(dto.groupId(), entity.getGroupId()),
                () -> assertEquals(dto.boardgameId(), entity.getBoardgame().getId()),
                () -> assertEquals(dto.createdAt(), entity.getCreatedAt()),
                () -> assertEquals(dto.updatedAt(), entity.getUpdatedAt()));
    }

    @Test
    void toEntity_throwsWhenBoardgameNotFound() {
        BoardgameGroupDto dto = new BoardgameGroupDto(1L, 10L, 999L, null, null);
        when(boardgameRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(dto));
    }
}
