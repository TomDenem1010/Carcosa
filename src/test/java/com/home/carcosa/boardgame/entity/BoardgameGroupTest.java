package com.home.carcosa.boardgame.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardgameGroupTest {

    static Stream<BoardgameGroup> groups() {
        return Stream.of(
                BoardgameGroup.builder().id(1L).groupId(10L).build(),
                BoardgameGroup.builder().id(2L).groupId(20L).build(),
                BoardgameGroup.builder().id(3L).groupId(1L).build());
    }

    @Test
    void prePersist_setsCreatedAtWhenNull() {
        BoardgameGroup group = new BoardgameGroup();
        group.prePersist();
        assertNotNull(group.getCreatedAt());
    }

    @Test
    void prePersist_setsUpdatedAtWhenNull() {
        BoardgameGroup group = new BoardgameGroup();
        group.prePersist();
        assertNotNull(group.getUpdatedAt());
    }

    @Test
    void prePersist_doesNotOverwriteExistingCreatedAt() {
        LocalDateTime fixed = LocalDateTime.of(2000, 1, 1, 0, 0);
        BoardgameGroup group = new BoardgameGroup();
        group.setCreatedAt(fixed);
        group.prePersist();
        assertEquals(fixed, group.getCreatedAt());
    }

    @Test
    void prePersist_doesNotOverwriteExistingUpdatedAt() {
        LocalDateTime fixed = LocalDateTime.of(2000, 1, 1, 0, 0);
        BoardgameGroup group = new BoardgameGroup();
        group.setUpdatedAt(fixed);
        group.prePersist();
        assertEquals(fixed, group.getUpdatedAt());
    }

    @Test
    void preUpdate_alwaysOverwritesUpdatedAt() {
        LocalDateTime old = LocalDateTime.of(2000, 1, 1, 0, 0);
        BoardgameGroup group = new BoardgameGroup();
        group.setUpdatedAt(old);
        group.preUpdate();
        assertNotEquals(old, group.getUpdatedAt());
    }

    @Test
    void equals_instancesWithSameId_areEqual() {
        BoardgameGroup a = BoardgameGroup.builder().id(1L).groupId(10L).build();
        BoardgameGroup b = BoardgameGroup.builder().id(1L).groupId(99L).build();
        assertEquals(a, b);
    }

    @Test
    void equals_instancesWithDifferentIds_areNotEqual() {
        BoardgameGroup a = BoardgameGroup.builder().id(1L).build();
        BoardgameGroup b = BoardgameGroup.builder().id(2L).build();
        assertNotEquals(a, b);
    }

    @ParameterizedTest
    @MethodSource("groups")
    void getters_returnValuesSetByBuilder(BoardgameGroup group) {
        assertNotNull(group.getId());
        assertNotNull(group.getGroupId());
    }
}
