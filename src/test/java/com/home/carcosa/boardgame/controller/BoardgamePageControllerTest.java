package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;
import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;
import com.home.carcosa.boardgame.dto.BoardgamesPageData;
import com.home.carcosa.boardgame.dto.GroupPageData;
import com.home.carcosa.boardgame.dto.PlayPageData;
import com.home.carcosa.boardgame.service.BoardgamePageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgamePageControllerTest {

    @Mock
    BoardgamePageService boardgamePageService;
    @InjectMocks
    BoardgamePageController controller;

    // ── boardgames() ────────────────────────────────────────────────────────

    static Stream<String[]> boardgamesArgs() {
        return Stream.of(
                new String[] { "chess", "name", "asc" },
                new String[] { "catan", "id", "desc" },
                new String[] { null, "name", "asc" });
    }

    @ParameterizedTest
    @MethodSource("boardgamesArgs")
    void boardgames_returnsCorrectViewName(String name, String sort, String dir) {
        stubBoardgamesPage(name, sort, dir);
        assertEquals("boardgame/boardgames", controller.boardgames(name, sort, dir, new ExtendedModelMap()));
    }

    @Test
    void boardgames_setsActiveMenuAndSubmenuAttributes() {
        stubBoardgamesPage("x", "name", "asc");
        var model = new ExtendedModelMap();
        controller.boardgames("x", "name", "asc", model);
        assertAll(
                () -> assertEquals("boardgame", model.get("activeMenu")),
                () -> assertEquals("boardgames", model.get("activeSubmenu")));
    }

    @Test
    void boardgames_setsPageDataAttributesFromService() {
        var types = List.of(BoardgameType.BASEGAME);
        var statuses = List.of(BoardgameStatus.OWN);
        var data = new BoardgamesPageData(List.of(), "chess", "name", "asc");
        when(boardgamePageService.boardgameTypes()).thenReturn(types);
        when(boardgamePageService.boardgameStatuses()).thenReturn(statuses);
        when(boardgamePageService.boardgamesPageData("chess", "name", "asc")).thenReturn(data);
        var model = new ExtendedModelMap();
        controller.boardgames("chess", "name", "asc", model);
        assertAll(
                () -> assertEquals(types, model.get("boardgameTypes")),
                () -> assertEquals(statuses, model.get("boardgameStatuses")),
                () -> assertEquals(List.of(), model.get("boardgames")),
                () -> assertEquals("chess", model.get("name")),
                () -> assertEquals("name", model.get("sort")),
                () -> assertEquals("asc", model.get("dir")));
    }

    // ── group() ─────────────────────────────────────────────────────────────

    @Test
    void group_returnsCorrectViewName() {
        when(boardgamePageService.groupPageData()).thenReturn(new GroupPageData(List.of(), Map.of(), 1L));
        assertEquals("boardgame/group", controller.group(new ExtendedModelMap()));
    }

    @Test
    void group_setsNextGroupIdInModel() {
        when(boardgamePageService.groupPageData()).thenReturn(new GroupPageData(List.of(), Map.of(), 7L));
        var model = new ExtendedModelMap();
        controller.group(model);
        assertEquals(7L, model.get("nextGroupId"));
    }

    @Test
    void group_setsActiveMenuAttributes() {
        when(boardgamePageService.groupPageData()).thenReturn(new GroupPageData(List.of(), Map.of(), 1L));
        var model = new ExtendedModelMap();
        controller.group(model);
        assertAll(
                () -> assertEquals("boardgame", model.get("activeMenu")),
                () -> assertEquals("group", model.get("activeSubmenu")));
    }

    // ── play() ──────────────────────────────────────────────────────────────

    static Stream<String[]> playArgs() {
        return Stream.of(
                new String[] { "chess", "playDate", "desc" },
                new String[] { "catan", "funScore", "asc" },
                new String[] { null, "playDate", "desc" });
    }

    @ParameterizedTest
    @MethodSource("playArgs")
    void play_returnsCorrectViewName(String name, String sort, String dir) {
        stubPlayPage(name, sort, dir);
        assertEquals("boardgame/play", controller.play(name, sort, dir, new ExtendedModelMap()));
    }

    @Test
    void play_setsActiveMenuAttributes() {
        stubPlayPage("x", "playDate", "desc");
        var model = new ExtendedModelMap();
        controller.play("x", "playDate", "desc", model);
        assertAll(
                () -> assertEquals("boardgame", model.get("activeMenu")),
                () -> assertEquals("play", model.get("activeSubmenu")));
    }

    @Test
    void play_setsPageDataAttributesFromService() {
        var wins = List.of(BoardgamePlayWin.SUCCESS, BoardgamePlayWin.FAILURE);
        var data = new PlayPageData(List.of(), List.of(), "chess", "playDate", "desc");
        when(boardgamePageService.boardgamePlayWins()).thenReturn(wins);
        when(boardgamePageService.playPageData("chess", "playDate", "desc")).thenReturn(data);
        var model = new ExtendedModelMap();
        controller.play("chess", "playDate", "desc", model);
        assertAll(
                () -> assertEquals(wins, model.get("boardgamePlayWins")),
                () -> assertEquals(List.of(), model.get("boardgames")),
                () -> assertEquals(List.of(), model.get("plays")),
                () -> assertEquals("chess", model.get("name")),
                () -> assertEquals("playDate", model.get("sort")),
                () -> assertEquals("desc", model.get("dir")));
    }

    // ── helpers ─────────────────────────────────────────────────────────────

    private void stubBoardgamesPage(String name, String sort, String dir) {
        when(boardgamePageService.boardgameTypes()).thenReturn(List.of());
        when(boardgamePageService.boardgameStatuses()).thenReturn(List.of());
        when(boardgamePageService.boardgamesPageData(name, sort, dir))
                .thenReturn(new BoardgamesPageData(List.of(), name == null ? "" : name, sort, dir));
    }

    private void stubPlayPage(String name, String sort, String dir) {
        when(boardgamePageService.boardgamePlayWins()).thenReturn(List.of());
        when(boardgamePageService.playPageData(name, sort, dir))
                .thenReturn(new PlayPageData(List.of(), List.of(), name == null ? "" : name, sort, dir));
    }
}
