package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GroupPageDataTest {

    @ParameterizedTest
    @ValueSource(longs = { 1L, 5L, 100L })
    void nextGroupId_isReturnedCorrectly(long nextId) {
        GroupPageData data = new GroupPageData(List.of(), Map.of(), nextId);
        assertEquals(nextId, data.nextGroupId());
    }

    @Test
    void boardgames_withEmptyList_returnsEmpty() {
        GroupPageData data = new GroupPageData(List.of(), Map.of(), 1L);
        assertTrue(data.boardgames().isEmpty());
    }

    @Test
    void boardgames_withPopulatedList_preservesContent() {
        Boardgame bg = new Boardgame();
        GroupPageData data = new GroupPageData(List.of(bg), Map.of(), 1L);
        assertEquals(1, data.boardgames().size());
    }

    @Test
    void groupsByGroupId_withPopulatedMap_preservesContent() {
        BoardgameGroup group = new BoardgameGroup();
        GroupPageData data = new GroupPageData(List.of(), Map.of(1L, List.of(group)), 2L);
        assertEquals(1, data.groupsByGroupId().get(1L).size());
    }

    @Test
    void equalInstances_withSameFields_areEqual() {
        GroupPageData a = new GroupPageData(List.of(), Map.of(), 3L);
        GroupPageData b = new GroupPageData(List.of(), Map.of(), 3L);
        assertEquals(a, b);
    }

    @Test
    void instances_withDifferentNextGroupId_areNotEqual() {
        GroupPageData a = new GroupPageData(List.of(), Map.of(), 1L);
        GroupPageData b = new GroupPageData(List.of(), Map.of(), 2L);
        assertNotEquals(a, b);
    }
}
