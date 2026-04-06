package com.home.carcosa.boardgame.entity;

import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardgameTest {

    static Stream<Boardgame> boardgames() {
        return Stream.of(
                Boardgame.builder().id(1L).name("Chess").type(BoardgameType.BASEGAME).status(BoardgameStatus.OWN)
                        .bggLink("http://a").build(),
                Boardgame.builder().id(2L).name("Catan").type(BoardgameType.EXPANSION).status(BoardgameStatus.SOLD)
                        .bggLink("http://b").build(),
                Boardgame.builder().id(3L).name("Go").type(BoardgameType.BASEGAME).status(BoardgameStatus.THINKINGTOBUY)
                        .bggLink("http://c").build());
    }

    @Test
    void prePersist_setsCreatedAtWhenNull() {
        Boardgame bg = new Boardgame();
        bg.prePersist();
        assertNotNull(bg.getCreatedAt());
    }

    @Test
    void prePersist_setsUpdatedAtWhenNull() {
        Boardgame bg = new Boardgame();
        bg.prePersist();
        assertNotNull(bg.getUpdatedAt());
    }

    @Test
    void prePersist_doesNotOverwriteExistingCreatedAt() {
        LocalDateTime fixed = LocalDateTime.of(2000, 1, 1, 0, 0);
        Boardgame bg = new Boardgame();
        bg.setCreatedAt(fixed);
        bg.prePersist();
        assertEquals(fixed, bg.getCreatedAt());
    }

    @Test
    void prePersist_doesNotOverwriteExistingUpdatedAt() {
        LocalDateTime fixed = LocalDateTime.of(2000, 1, 1, 0, 0);
        Boardgame bg = new Boardgame();
        bg.setUpdatedAt(fixed);
        bg.prePersist();
        assertEquals(fixed, bg.getUpdatedAt());
    }

    @Test
    void preUpdate_alwaysOverwritesUpdatedAt() {
        LocalDateTime old = LocalDateTime.of(2000, 1, 1, 0, 0);
        Boardgame bg = new Boardgame();
        bg.setUpdatedAt(old);
        bg.preUpdate();
        assertNotEquals(old, bg.getUpdatedAt());
    }

    @Test
    void equals_instancesWithSameId_areEqual() {
        Boardgame a = Boardgame.builder().id(1L).name("Chess").build();
        Boardgame b = Boardgame.builder().id(1L).name("Different").build();
        assertEquals(a, b);
    }

    @Test
    void equals_instancesWithDifferentIds_areNotEqual() {
        Boardgame a = Boardgame.builder().id(1L).build();
        Boardgame b = Boardgame.builder().id(2L).build();
        assertNotEquals(a, b);
    }

    @Test
    void builder_defaultPlaysListIsInitialized() {
        Boardgame bg = Boardgame.builder().build();
        assertNotNull(bg.getPlays());
    }

    @ParameterizedTest
    @MethodSource("boardgames")
    void getters_returnValuesSetByBuilder(Boardgame bg) {
        assertNotNull(bg.getId());
        assertNotNull(bg.getName());
        assertNotNull(bg.getType());
        assertNotNull(bg.getStatus());
        assertNotNull(bg.getBggLink());
    }
}
