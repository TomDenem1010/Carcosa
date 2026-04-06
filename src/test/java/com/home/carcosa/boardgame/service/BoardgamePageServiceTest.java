package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;
import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;
import com.home.carcosa.boardgame.dto.BoardgamesPageData;
import com.home.carcosa.boardgame.dto.GroupPageData;
import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgamePageServiceTest {

    @Mock
    BoardgameService boardgameService;
    @Mock
    BoardgameGroupService boardgameGroupService;
    @Mock
    BoardgamePlayService boardgamePlayService;
    @InjectMocks
    BoardgamePageService service;
    @Captor
    ArgumentCaptor<Sort> sortCaptor;

    // ── enum list methods ──────────────────────────────────────────────────

    @Test
    void boardgameTypes_containsAllEnumValues() {
        assertIterableEquals(List.of(BoardgameType.values()), service.boardgameTypes());
    }

    @Test
    void boardgameStatuses_containsAllEnumValues() {
        assertIterableEquals(List.of(BoardgameStatus.values()), service.boardgameStatuses());
    }

    @Test
    void boardgamePlayWins_containsAllEnumValues() {
        assertIterableEquals(List.of(BoardgamePlayWin.values()), service.boardgamePlayWins());
    }

    // ── boardgamesPageData – name handling ─────────────────────────────────

    @Test
    void boardgamesPageData_withName_callsFindByNameContainingIgnoreCase() {
        when(boardgameService.findByNameContainingIgnoreCase(eq("chess"), any())).thenReturn(List.of());
        service.boardgamesPageData("chess", "name", "asc");
        verify(boardgameService).findByNameContainingIgnoreCase(eq("chess"), any());
    }

    @Test
    void boardgamesPageData_nameWithWhitespace_trimsBeforeQuery() {
        when(boardgameService.findByNameContainingIgnoreCase(eq("chess"), any())).thenReturn(List.of());
        service.boardgamesPageData("  chess  ", "name", "asc");
        verify(boardgameService).findByNameContainingIgnoreCase(eq("chess"), any());
    }

    @Test
    void boardgamesPageData_nullName_callsFindAllWithSort() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        service.boardgamesPageData(null, "name", "asc");
        verify(boardgameService).findAll(any(Sort.class));
    }

    @Test
    void boardgamesPageData_nullName_resultNameIsEmpty() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        BoardgamesPageData data = service.boardgamesPageData(null, "name", "asc");
        assertEquals("", data.name());
    }

    // ── boardgamesPageData – direction ─────────────────────────────────────

    @ParameterizedTest
    @CsvSource({ "asc, asc", "ASC, asc" })
    void boardgamesPageData_ascDir_setsDirToAscInResult(String dir, String expected) {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals(expected, service.boardgamesPageData(null, "name", dir).dir());
    }

    @ParameterizedTest
    @CsvSource({ "desc, desc", "DESC, desc" })
    void boardgamesPageData_descDir_setsDirToDescInResult(String dir, String expected) {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals(expected, service.boardgamesPageData(null, "name", dir).dir());
    }

    @Test
    void boardgamesPageData_nullDir_defaultsToAsc() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals("asc", service.boardgamesPageData(null, "name", null).dir());
    }

    // ── boardgamesPageData – sort default ──────────────────────────────────

    @Test
    void boardgamesPageData_nullSort_defaultsToNameInResult() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals("name", service.boardgamesPageData(null, null, "asc").sort());
    }

    @Test
    void boardgamesPageData_blankSort_defaultsToNameInResult() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals("name", service.boardgamesPageData(null, "   ", "asc").sort());
    }

    // ── boardgamesPageData – sort property mapping ─────────────────────────

    static Stream<Arguments> boardgameSortMappings() {
        return Stream.of(
                Arguments.of("id", "id"),
                Arguments.of("name", "name"),
                Arguments.of("type", "type"),
                Arguments.of("status", "status"),
                Arguments.of("createdat", "createdAt"),
                Arguments.of("updatedat", "updatedAt"),
                Arguments.of("unknown", "name"),
                Arguments.of(null, "name"));
    }

    @ParameterizedTest
    @MethodSource("boardgameSortMappings")
    void boardgamesPageData_sortInput_mapsToCorrectEntityProperty(String sortInput, String expectedProperty) {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        service.boardgamesPageData(null, sortInput, "asc");
        verify(boardgameService).findAll(sortCaptor.capture());
        assertEquals(expectedProperty, sortCaptor.getValue().iterator().next().getProperty());
    }

    // ── groupPageData ──────────────────────────────────────────────────────

    @Test
    void groupPageData_noGroups_nextGroupIdIsOne() {
        when(boardgameGroupService.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals(1L, service.groupPageData().nextGroupId());
    }

    @Test
    void groupPageData_withGroups_nextGroupIdIsMaxGroupIdPlusOne() {
        Boardgame bg = Boardgame.builder().id(10L).build();
        List<BoardgameGroup> groups = List.of(
                BoardgameGroup.builder().groupId(3L).boardgame(bg).build(),
                BoardgameGroup.builder().groupId(7L).boardgame(bg).build());
        when(boardgameGroupService.findAll(any(Sort.class))).thenReturn(groups);
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals(8L, service.groupPageData().nextGroupId());
    }

    @Test
    void groupPageData_groupedBoardgamesExcludedFromAvailableList() {
        Boardgame grouped = Boardgame.builder().id(1L).build();
        Boardgame available = Boardgame.builder().id(2L).build();
        when(boardgameGroupService.findAll(any())).thenReturn(
                List.of(BoardgameGroup.builder().groupId(1L).boardgame(grouped).build()));
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of(grouped, available));
        GroupPageData data = service.groupPageData();
        assertAll(
                () -> assertEquals(1, data.boardgames().size()),
                () -> assertEquals(2L, data.boardgames().get(0).getId()));
    }

    @Test
    void groupPageData_groupsByGroupId_groupsEntriesCorrectly() {
        Boardgame bg1 = Boardgame.builder().id(11L).build();
        Boardgame bg2 = Boardgame.builder().id(22L).build();
        when(boardgameGroupService.findAll(any())).thenReturn(List.of(
                BoardgameGroup.builder().groupId(1L).boardgame(bg1).build(),
                BoardgameGroup.builder().groupId(1L).boardgame(bg2).build()));
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals(2, service.groupPageData().groupsByGroupId().get(1L).size());
    }

    // ── playPageData – direction ───────────────────────────────────────────

    @Test
    void playPageData_nullDir_defaultsToDesc() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals("desc", service.playPageData(null, "playDate", null).dir());
    }

    @ParameterizedTest
    @CsvSource({ "asc, asc", "ASC, asc" })
    void playPageData_ascDir_overridesDefaultToAsc(String dir, String expected) {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals(expected, service.playPageData(null, "playDate", dir).dir());
    }

    // ── playPageData – name handling ───────────────────────────────────────

    @Test
    void playPageData_nullName_callsFindAllPlays() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayService.findAll(any(Sort.class))).thenReturn(List.of());
        service.playPageData(null, "playDate", "desc");
        verify(boardgamePlayService).findAll(any(Sort.class));
    }

    @Test
    void playPageData_withName_callsFindByBoardgameNameContainingIgnoreCase() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayService.findByBoardgameNameContainingIgnoreCase(eq("chess"), any())).thenReturn(List.of());
        service.playPageData("chess", "playDate", "desc");
        verify(boardgamePlayService).findByBoardgameNameContainingIgnoreCase(eq("chess"), any());
    }

    @Test
    void playPageData_alwaysLoadsAllBoardgamesForDropdown() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayService.findAll(any(Sort.class))).thenReturn(List.of());
        service.playPageData(null, "playDate", "desc");
        verify(boardgameService).findAll(any(Sort.class));
    }

    // ── playPageData – sort default ────────────────────────────────────────

    @Test
    void playPageData_nullSort_defaultsToPlayDateInResult() {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayService.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals("playDate", service.playPageData(null, null, "desc").sort());
    }

    // ── playPageData – sort property mapping ───────────────────────────────

    static Stream<Arguments> playSortMappings() {
        return Stream.of(
                Arguments.of("id", "id"),
                Arguments.of("boardgame", "boardgame.name"),
                Arguments.of("playdate", "playDate"),
                Arguments.of("playercount", "playerCount"),
                Arguments.of("win", "win"),
                Arguments.of("funscore", "funScore"),
                Arguments.of("createdat", "createdAt"),
                Arguments.of("updatedat", "updatedAt"),
                Arguments.of("unknown", "playDate"),
                Arguments.of(null, "playDate"));
    }

    @ParameterizedTest
    @MethodSource("playSortMappings")
    void playPageData_sortInput_mapsToCorrectEntityProperty(String sortInput, String expectedProperty) {
        when(boardgameService.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayService.findAll(any(Sort.class))).thenReturn(List.of());
        service.playPageData(null, sortInput, "asc");
        verify(boardgamePlayService).findAll(sortCaptor.capture());
        assertEquals(expectedProperty, sortCaptor.getValue().iterator().next().getProperty());
    }
}
