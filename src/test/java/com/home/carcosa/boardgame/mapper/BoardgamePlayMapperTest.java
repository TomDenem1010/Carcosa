package com.home.carcosa.boardgame.mapper;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;
import com.home.carcosa.boardgame.dto.BoardgamePlayDto;
import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgamePlay;
import com.home.carcosa.boardgame.repository.BoardgameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgamePlayMapperTest {

    @Mock
    BoardgameRepository boardgameRepository;
    @InjectMocks
    BoardgamePlayMapper mapper;

    static Stream<BoardgamePlayDto> dtos() {
        LocalDateTime ts = LocalDateTime.of(2024, 1, 1, 0, 0);
        return Stream.of(
                new BoardgamePlayDto(1L, 10L, LocalDate.of(2024, 1, 1), 2, BoardgamePlayWin.SUCCESS, 5, ts, ts),
                new BoardgamePlayDto(2L, 20L, LocalDate.of(2024, 6, 15), 4, BoardgamePlayWin.FAILURE, 3, ts, ts),
                new BoardgamePlayDto(3L, 30L, LocalDate.of(2025, 12, 31), 1, BoardgamePlayWin.SUCCESS, 4, ts, ts));
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void toDto_mapsAllFieldsFromEntity(BoardgamePlayDto dto) {
        Boardgame bg = Boardgame.builder().id(dto.boardgameId()).build();
        BoardgamePlay entity = BoardgamePlay.builder()
                .id(dto.id()).boardgame(bg).playDate(dto.playDate())
                .playerCount(dto.playerCount()).win(dto.win()).funScore(dto.funScore())
                .createdAt(dto.createdAt()).updatedAt(dto.updatedAt()).build();

        BoardgamePlayDto result = mapper.toDto(entity);

        assertAll(
                () -> assertEquals(entity.getId(), result.id()),
                () -> assertEquals(entity.getBoardgame().getId(), result.boardgameId()),
                () -> assertEquals(entity.getPlayDate(), result.playDate()),
                () -> assertEquals(entity.getPlayerCount(), result.playerCount()),
                () -> assertEquals(entity.getWin(), result.win()),
                () -> assertEquals(entity.getFunScore(), result.funScore()),
                () -> assertEquals(entity.getCreatedAt(), result.createdAt()),
                () -> assertEquals(entity.getUpdatedAt(), result.updatedAt()));
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void toEntity_mapsAllFieldsFromDto(BoardgamePlayDto dto) {
        Boardgame bg = Boardgame.builder().id(dto.boardgameId()).build();
        when(boardgameRepository.findById(dto.boardgameId())).thenReturn(Optional.of(bg));

        BoardgamePlay entity = mapper.toEntity(dto);

        assertAll(
                () -> assertEquals(dto.id(), entity.getId()),
                () -> assertEquals(dto.boardgameId(), entity.getBoardgame().getId()),
                () -> assertEquals(dto.playDate(), entity.getPlayDate()),
                () -> assertEquals(dto.playerCount(), entity.getPlayerCount()),
                () -> assertEquals(dto.win(), entity.getWin()),
                () -> assertEquals(dto.funScore(), entity.getFunScore()),
                () -> assertEquals(dto.createdAt(), entity.getCreatedAt()),
                () -> assertEquals(dto.updatedAt(), entity.getUpdatedAt()));
    }

    @Test
    void toEntity_throwsWhenBoardgameNotFound() {
        BoardgamePlayDto dto = new BoardgamePlayDto(1L, 999L, LocalDate.now(), 2, BoardgamePlayWin.SUCCESS, 5, null,
                null);
        when(boardgameRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(dto));
    }
}
