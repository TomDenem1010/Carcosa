package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;
import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;
import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import com.home.carcosa.boardgame.entity.BoardgamePlay;
import com.home.carcosa.boardgame.repository.BoardgameGroupRepository;
import com.home.carcosa.boardgame.repository.BoardgamePlayRepository;
import com.home.carcosa.boardgame.repository.BoardgameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardgameTableCsvExportServiceTest {

    @Mock
    BoardgameRepository boardgameRepository;
    @Mock
    BoardgameGroupRepository boardgameGroupRepository;
    @Mock
    BoardgamePlayRepository boardgamePlayRepository;
    @InjectMocks
    BoardgameTableCsvExportService service;

    @TempDir
    Path tempDir;

    // ── helpers ──────────────────────────────────────────────────────────────

    private void stubAllEmpty() {
        when(boardgameRepository.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgameGroupRepository.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayRepository.findAll(any(Sort.class))).thenReturn(List.of());
    }

    private void stubBoardgames(List<Boardgame> list) {
        when(boardgameRepository.findAll(any(Sort.class))).thenReturn(list);
        when(boardgameGroupRepository.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayRepository.findAll(any(Sort.class))).thenReturn(List.of());
    }

    private List<String> exportAndReadLines(String filename) throws IOException {
        service.exportToCsv(tempDir);
        return Files.readAllLines(tempDir.resolve(filename));
    }

    // ── file creation ────────────────────────────────────────────────────────

    @Test
    void exportToCsv_createsBoardgameCsvFile() throws IOException {
        stubAllEmpty();
        service.exportToCsv(tempDir);
        assertTrue(Files.exists(tempDir.resolve("boardgame.csv")));
    }

    @Test
    void exportToCsv_createsBoardgameGroupCsvFile() throws IOException {
        stubAllEmpty();
        service.exportToCsv(tempDir);
        assertTrue(Files.exists(tempDir.resolve("boardgame_group.csv")));
    }

    @Test
    void exportToCsv_createsBoardgamePlayCsvFile() throws IOException {
        stubAllEmpty();
        service.exportToCsv(tempDir);
        assertTrue(Files.exists(tempDir.resolve("boardgame_play.csv")));
    }

    @Test
    void exportToCsv_createsOutputDirectoryIfMissing() throws IOException {
        stubAllEmpty();
        Path subDir = tempDir.resolve("new_subdir");
        service.exportToCsv(subDir);
        assertTrue(Files.isDirectory(subDir));
    }

    // ── headers ──────────────────────────────────────────────────────────────

    @Test
    void exportToCsv_boardgameCsv_hasCorrectHeader() throws IOException {
        stubAllEmpty();
        assertEquals("ID,NAME,TYPE,STATUS,BGG_LINK,CREATED_AT,UPDATED_AT",
                exportAndReadLines("boardgame.csv").get(0));
    }

    @Test
    void exportToCsv_boardgameGroupCsv_hasCorrectHeader() throws IOException {
        stubAllEmpty();
        assertEquals("ID,GROUP_ID,BOARDGAME_ID,CREATED_AT,UPDATED_AT",
                exportAndReadLines("boardgame_group.csv").get(0));
    }

    @Test
    void exportToCsv_boardgamePlayCsv_hasCorrectHeader() throws IOException {
        stubAllEmpty();
        assertEquals("ID,BOARDGAME_ID,PLAY_DATE,PLAYER_COUNT,WIN,FUN_SCORE,CREATED_AT,UPDATED_AT",
                exportAndReadLines("boardgame_play.csv").get(0));
    }

    // ── boardgame row count ───────────────────────────────────────────────────

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 3 })
    void exportToCsv_boardgameCsv_writesOneRowPerBoardgame(int count) throws IOException {
        Boardgame bg = Boardgame.builder().id(1L).name("X").type(BoardgameType.BASEGAME).status(BoardgameStatus.OWN)
                .bggLink("l").build();
        stubBoardgames(Collections.nCopies(count, bg));
        assertEquals(count + 1, exportAndReadLines("boardgame.csv").size());
    }

    // ── boardgame field values ────────────────────────────────────────────────

    @Test
    void exportToCsv_boardgameCsv_writesCorrectFieldValues() throws IOException {
        LocalDateTime ts = LocalDateTime.of(2024, 1, 1, 0, 0);
        Boardgame bg = Boardgame.builder().id(5L).name("Chess").type(BoardgameType.BASEGAME).status(BoardgameStatus.OWN)
                .bggLink("http://a").createdAt(ts).updatedAt(ts).build();
        stubBoardgames(List.of(bg));
        assertEquals("5,Chess,BASEGAME,OWN,http://a,2024-01-01T00:00,2024-01-01T00:00",
                exportAndReadLines("boardgame.csv").get(1));
    }

    @Test
    void exportToCsv_boardgameCsv_nullTimestampsWrittenAsEmpty() throws IOException {
        Boardgame bg = Boardgame.builder().id(1L).name("Chess").type(BoardgameType.BASEGAME).status(BoardgameStatus.OWN)
                .bggLink("http://a").build();
        stubBoardgames(List.of(bg));
        assertTrue(exportAndReadLines("boardgame.csv").get(1).endsWith(",,"));
    }

    // ── CSV escaping ──────────────────────────────────────────────────────────

    static Stream<org.junit.jupiter.params.provider.Arguments> nameEscapingCases() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("Chess", "Chess"),
                org.junit.jupiter.params.provider.Arguments.of("A,B", "\"A,B\""),
                org.junit.jupiter.params.provider.Arguments.of("A\"B", "\"A\"\"B\""),
                org.junit.jupiter.params.provider.Arguments.of("A\nB", "\"A\nB\""));
    }

    @ParameterizedTest
    @MethodSource("nameEscapingCases")
    void exportToCsv_boardgameCsv_escapesNameFieldCorrectly(String nameInput, String expectedCsvToken)
            throws IOException {
        Boardgame bg = Boardgame.builder().id(1L).name(nameInput).type(BoardgameType.BASEGAME)
                .status(BoardgameStatus.OWN).bggLink("http://a").build();
        stubBoardgames(List.of(bg));
        service.exportToCsv(tempDir);
        assertTrue(Files.readString(tempDir.resolve("boardgame.csv")).contains(expectedCsvToken));
    }

    // ── boardgame_group row ───────────────────────────────────────────────────

    @Test
    void exportToCsv_boardgameGroupCsv_writesCorrectFieldValues() throws IOException {
        LocalDateTime ts = LocalDateTime.of(2024, 6, 1, 12, 0);
        Boardgame bg = Boardgame.builder().id(10L).build();
        BoardgameGroup group = BoardgameGroup.builder().id(1L).groupId(3L).boardgame(bg).createdAt(ts).updatedAt(ts)
                .build();
        when(boardgameRepository.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgameGroupRepository.findAll(any(Sort.class))).thenReturn(List.of(group));
        when(boardgamePlayRepository.findAll(any(Sort.class))).thenReturn(List.of());
        assertEquals("1,3,10,2024-06-01T12:00,2024-06-01T12:00",
                exportAndReadLines("boardgame_group.csv").get(1));
    }

    // ── boardgame_play row ────────────────────────────────────────────────────

    @Test
    void exportToCsv_boardgamePlayCsv_writesCorrectFieldValues() throws IOException {
        LocalDate date = LocalDate.of(2024, 3, 15);
        LocalDateTime ts = LocalDateTime.of(2024, 3, 15, 10, 0);
        Boardgame bg = Boardgame.builder().id(7L).build();
        BoardgamePlay play = BoardgamePlay.builder().id(2L).boardgame(bg).playDate(date).playerCount(3)
                .win(BoardgamePlayWin.SUCCESS).funScore(4).createdAt(ts).updatedAt(ts).build();
        when(boardgameRepository.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgameGroupRepository.findAll(any(Sort.class))).thenReturn(List.of());
        when(boardgamePlayRepository.findAll(any(Sort.class))).thenReturn(List.of(play));
        assertEquals("2,7,2024-03-15,3,SUCCESS,4,2024-03-15T10:00,2024-03-15T10:00",
                exportAndReadLines("boardgame_play.csv").get(1));
    }
}
