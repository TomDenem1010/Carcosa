package com.home.carcosa.boardgame.mapper;

import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;
import com.home.carcosa.boardgame.dto.BoardgameDto;
import com.home.carcosa.boardgame.entity.Boardgame;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardgameMapperTest {

    private final BoardgameMapper mapper = new BoardgameMapper();

    static Stream<Boardgame> entities() {
        LocalDateTime ts = LocalDateTime.of(2024, 1, 1, 0, 0);
        return Stream.of(
                Boardgame.builder().id(1L).name("Chess").type(BoardgameType.BASEGAME).status(BoardgameStatus.OWN)
                        .bggLink("http://a").createdAt(ts).updatedAt(ts).build(),
                Boardgame.builder().id(2L).name("Catan").type(BoardgameType.EXPANSION).status(BoardgameStatus.SOLD)
                        .bggLink("http://b").createdAt(ts).updatedAt(ts).build(),
                Boardgame.builder().id(3L).name("Go").type(BoardgameType.BASEGAME).status(BoardgameStatus.THINKINGTOBUY)
                        .bggLink("http://c").createdAt(ts).updatedAt(ts).build());
    }

    static Stream<BoardgameDto> dtos() {
        LocalDateTime ts = LocalDateTime.of(2024, 6, 1, 0, 0);
        return Stream.of(
                new BoardgameDto(1L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "http://a", ts, ts),
                new BoardgameDto(2L, "Catan", BoardgameType.EXPANSION, BoardgameStatus.SOLD, "http://b", ts, ts),
                new BoardgameDto(3L, "Go", BoardgameType.BASEGAME, BoardgameStatus.THINKINGTOBUY, "http://c", ts, ts));
    }

    @ParameterizedTest
    @MethodSource("entities")
    void toDto_mapsAllFieldsFromEntity(Boardgame entity) {
        BoardgameDto dto = mapper.toDto(entity);
        assertAll(
                () -> assertEquals(entity.getId(), dto.id()),
                () -> assertEquals(entity.getName(), dto.name()),
                () -> assertEquals(entity.getType(), dto.type()),
                () -> assertEquals(entity.getStatus(), dto.status()),
                () -> assertEquals(entity.getBggLink(), dto.bggLink()),
                () -> assertEquals(entity.getCreatedAt(), dto.createdAt()),
                () -> assertEquals(entity.getUpdatedAt(), dto.updatedAt()));
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void toEntity_mapsAllFieldsFromDto(BoardgameDto dto) {
        Boardgame entity = mapper.toEntity(dto);
        assertAll(
                () -> assertEquals(dto.id(), entity.getId()),
                () -> assertEquals(dto.name(), entity.getName()),
                () -> assertEquals(dto.type(), entity.getType()),
                () -> assertEquals(dto.status(), entity.getStatus()),
                () -> assertEquals(dto.bggLink(), entity.getBggLink()),
                () -> assertEquals(dto.createdAt(), entity.getCreatedAt()),
                () -> assertEquals(dto.updatedAt(), entity.getUpdatedAt()));
    }
}
