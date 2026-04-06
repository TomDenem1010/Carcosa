package com.home.carcosa.boardgame.entity;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardgamePlayTest {

    static Stream<BoardgamePlay> plays() {
        return Stream.of(
                BoardgamePlay.builder().id(1L).playDate(LocalDate.of(2024, 1, 1)).playerCount(2)
                        .win(BoardgamePlayWin.SUCCESS).funScore(5).build(),
                BoardgamePlay.builder().id(2L).playDate(LocalDate.of(2024, 6, 1)).playerCount(4)
                        .win(BoardgamePlayWin.FAILURE).funScore(3).build(),
                BoardgamePlay.builder().id(3L).playDate(LocalDate.of(2025, 12, 31)).playerCount(1)
                        .win(BoardgamePlayWin.SUCCESS).funScore(4).build());
    }

    @Test
    void prePersist_setsCreatedAtWhenNull() {
        BoardgamePlay play = new BoardgamePlay();
        play.prePersist();
        assertNotNull(play.getCreatedAt());
    }

    @Test
    void prePersist_setsUpdatedAtWhenNull() {
        BoardgamePlay play = new BoardgamePlay();
        play.prePersist();
        assertNotNull(play.getUpdatedAt());
    }

    @Test
    void prePersist_doesNotOverwriteExistingCreatedAt() {
        LocalDateTime fixed = LocalDateTime.of(2000, 1, 1, 0, 0);
        BoardgamePlay play = new BoardgamePlay();
        play.setCreatedAt(fixed);
        play.prePersist();
        assertEquals(fixed, play.getCreatedAt());
    }

    @Test
    void prePersist_doesNotOverwriteExistingUpdatedAt() {
        LocalDateTime fixed = LocalDateTime.of(2000, 1, 1, 0, 0);
        BoardgamePlay play = new BoardgamePlay();
        play.setUpdatedAt(fixed);
        play.prePersist();
        assertEquals(fixed, play.getUpdatedAt());
    }

    @Test
    void preUpdate_alwaysOverwritesUpdatedAt() {
        LocalDateTime old = LocalDateTime.of(2000, 1, 1, 0, 0);
        BoardgamePlay play = new BoardgamePlay();
        play.setUpdatedAt(old);
        play.preUpdate();
        assertNotEquals(old, play.getUpdatedAt());
    }

    @Test
    void equals_instancesWithSameId_areEqual() {
        BoardgamePlay a = BoardgamePlay.builder().id(1L).win(BoardgamePlayWin.SUCCESS).build();
        BoardgamePlay b = BoardgamePlay.builder().id(1L).win(BoardgamePlayWin.FAILURE).build();
        assertEquals(a, b);
    }

    @Test
    void equals_instancesWithDifferentIds_areNotEqual() {
        BoardgamePlay a = BoardgamePlay.builder().id(1L).build();
        BoardgamePlay b = BoardgamePlay.builder().id(2L).build();
        assertNotEquals(a, b);
    }

    @ParameterizedTest
    @MethodSource("plays")
    void getters_returnValuesSetByBuilder(BoardgamePlay play) {
        assertNotNull(play.getId());
        assertNotNull(play.getPlayDate());
        assertNotNull(play.getPlayerCount());
        assertNotNull(play.getWin());
        assertNotNull(play.getFunScore());
    }
}
