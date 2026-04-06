package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.entity.Boardgame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardgamesPageDataTest {

    static Stream<BoardgamesPageData> data() {
        return Stream.of(
                new BoardgamesPageData(List.of(), "chess", "name", "asc"),
                new BoardgamesPageData(List.of(), "", "id", "desc"),
                new BoardgamesPageData(List.of(), null, "type", "asc"));
    }

    @ParameterizedTest
    @MethodSource("data")
    void accessors_returnConstructorValues(BoardgamesPageData d) {
        assertNotNull(d.boardgames());
        assertNotNull(d.sort());
        assertNotNull(d.dir());
    }

    @ParameterizedTest
    @MethodSource("data")
    void equalInstances_withSameFields_areEqual(BoardgamesPageData original) {
        BoardgamesPageData copy = new BoardgamesPageData(
                original.boardgames(), original.name(), original.sort(), original.dir());
        assertEquals(original, copy);
    }

    @Test
    void instances_withDifferentSort_areNotEqual() {
        BoardgamesPageData a = new BoardgamesPageData(List.of(), "x", "name", "asc");
        BoardgamesPageData b = new BoardgamesPageData(List.of(), "x", "id", "asc");
        assertNotEquals(a, b);
    }

    @Test
    void instances_withDifferentDir_areNotEqual() {
        BoardgamesPageData a = new BoardgamesPageData(List.of(), "x", "name", "asc");
        BoardgamesPageData b = new BoardgamesPageData(List.of(), "x", "name", "desc");
        assertNotEquals(a, b);
    }

    @Test
    void boardgames_withPopulatedList_preservesContent() {
        Boardgame bg = new Boardgame();
        BoardgamesPageData d = new BoardgamesPageData(List.of(bg), "x", "name", "asc");
        assertEquals(1, d.boardgames().size());
    }
}
