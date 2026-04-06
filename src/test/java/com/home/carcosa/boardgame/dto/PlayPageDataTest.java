package com.home.carcosa.boardgame.dto;

import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgamePlay;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PlayPageDataTest {

    static Stream<PlayPageData> data() {
        return Stream.of(
                new PlayPageData(List.of(), List.of(), "chess", "playDate", "desc"),
                new PlayPageData(List.of(), List.of(), "", "funScore", "asc"),
                new PlayPageData(List.of(), List.of(), null, "playDate", "desc"));
    }

    @ParameterizedTest
    @MethodSource("data")
    void accessors_returnConstructorValues(PlayPageData d) {
        assertNotNull(d.boardgames());
        assertNotNull(d.plays());
        assertNotNull(d.sort());
        assertNotNull(d.dir());
    }

    @ParameterizedTest
    @MethodSource("data")
    void equalInstances_withSameFields_areEqual(PlayPageData original) {
        PlayPageData copy = new PlayPageData(
                original.boardgames(), original.plays(), original.name(), original.sort(), original.dir());
        assertEquals(original, copy);
    }

    @Test
    void instances_withDifferentSort_areNotEqual() {
        PlayPageData a = new PlayPageData(List.of(), List.of(), "x", "playDate", "desc");
        PlayPageData b = new PlayPageData(List.of(), List.of(), "x", "funScore", "desc");
        assertNotEquals(a, b);
    }

    @Test
    void instances_withDifferentDir_areNotEqual() {
        PlayPageData a = new PlayPageData(List.of(), List.of(), "x", "playDate", "asc");
        PlayPageData b = new PlayPageData(List.of(), List.of(), "x", "playDate", "desc");
        assertNotEquals(a, b);
    }

    @Test
    void plays_withPopulatedList_preservesContent() {
        BoardgamePlay play = new BoardgamePlay();
        PlayPageData data = new PlayPageData(List.of(), List.of(play), "x", "playDate", "desc");
        assertEquals(1, data.plays().size());
    }

    @Test
    void boardgames_withPopulatedList_preservesContent() {
        Boardgame bg = new Boardgame();
        PlayPageData data = new PlayPageData(List.of(bg), List.of(), "x", "playDate", "desc");
        assertEquals(1, data.boardgames().size());
    }
}
