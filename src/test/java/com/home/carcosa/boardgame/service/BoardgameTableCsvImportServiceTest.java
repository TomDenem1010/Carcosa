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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgameTableCsvImportServiceTest {

    @Mock
    BoardgameRepository boardgameRepository;
    @Mock
    BoardgameGroupRepository boardgameGroupRepository;
    @Mock
    BoardgamePlayRepository boardgamePlayRepository;
    @InjectMocks
    BoardgameTableCsvImportService service;

    @TempDir
    Path tempDir;

    @Captor
    ArgumentCaptor<List<Boardgame>> boardgameCaptor;
    @Captor
    ArgumentCaptor<List<BoardgameGroup>> groupCaptor;
    @Captor
    ArgumentCaptor<List<BoardgamePlay>> playCaptor;

    // ── helpers ──────────────────────────────────────────────────────────────

    private void writeBoardgameCsv(String... rows) throws IOException {
        writeFile("boardgame.csv", "ID,NAME,TYPE,STATUS,BGG_LINK,CREATED_AT,UPDATED_AT", rows);
    }

    private void writeGroupCsv(String... rows) throws IOException {
        writeFile("boardgame_group.csv", "ID,GROUP_ID,BOARDGAME_ID,CREATED_AT,UPDATED_AT", rows);
    }

    private void writePlayCsv(String... rows) throws IOException {
        writeFile("boardgame_play.csv", "ID,BOARDGAME_ID,PLAY_DATE,PLAYER_COUNT,WIN,FUN_SCORE,CREATED_AT,UPDATED_AT",
                rows);
    }

    private void writeFile(String filename, String header, String... rows) throws IOException {
        StringBuilder sb = new StringBuilder(header).append("\n");
        for (String row : rows)
            sb.append(row).append("\n");
        Files.writeString(tempDir.resolve(filename), sb.toString());
    }

    // ── missing files ─────────────────────────────────────────────────────────

    @Test
    void importFromCsv_throwsFileNotFoundWhenBoardgameCsvMissing() {
        assertThrows(FileNotFoundException.class, () -> service.importFromCsv(tempDir));
    }

    @Test
    void importFromCsv_throwsFileNotFoundWhenGroupCsvMissing() throws IOException {
        writeBoardgameCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        assertThrows(FileNotFoundException.class, () -> service.importFromCsv(tempDir));
    }

    @Test
    void importFromCsv_throwsFileNotFoundWhenPlayCsvMissing() throws IOException {
        writeBoardgameCsv();
        writeGroupCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        assertThrows(FileNotFoundException.class, () -> service.importFromCsv(tempDir));
    }

    // ── empty files ───────────────────────────────────────────────────────────

    @Test
    void importFromCsv_emptyFiles_savesNothingToAnyRepository() throws IOException {
        writeBoardgameCsv();
        writeGroupCsv();
        writePlayCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        service.importFromCsv(tempDir);
        verify(boardgameRepository, never()).saveAll(any());
        verify(boardgameGroupRepository, never()).saveAll(any());
        verify(boardgamePlayRepository, never()).saveAll(any());
    }

    // ── boardgame import ──────────────────────────────────────────────────────

    @Test
    void importFromCsv_boardgame_setsIdToNull() throws IOException {
        writeBoardgameCsv("1,Chess,BASEGAME,OWN,http://a,,");
        writeGroupCsv();
        writePlayCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        service.importFromCsv(tempDir);
        verify(boardgameRepository).saveAll(boardgameCaptor.capture());
        assertNull(boardgameCaptor.getValue().get(0).getId());
    }

    @Test
    void importFromCsv_boardgame_mapsAllFields() throws IOException {
        LocalDateTime ts = LocalDateTime.of(2024, 1, 1, 0, 0);
        writeBoardgameCsv("1,Chess,BASEGAME,OWN,http://a,2024-01-01T00:00,2024-01-01T00:00");
        writeGroupCsv();
        writePlayCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        service.importFromCsv(tempDir);
        verify(boardgameRepository).saveAll(boardgameCaptor.capture());
        Boardgame saved = boardgameCaptor.getValue().get(0);
        assertAll(
                () -> assertEquals("Chess", saved.getName()),
                () -> assertEquals(BoardgameType.BASEGAME, saved.getType()),
                () -> assertEquals(BoardgameStatus.OWN, saved.getStatus()),
                () -> assertEquals("http://a", saved.getBggLink()),
                () -> assertEquals(ts, saved.getCreatedAt()),
                () -> assertEquals(ts, saved.getUpdatedAt()));
    }

    @ParameterizedTest
    @MethodSource("boardgameTypeAndStatusCases")
    void importFromCsv_boardgame_parsesTypeAndStatus(String type, String status,
            BoardgameType expectedType, BoardgameStatus expectedStatus) throws IOException {
        writeBoardgameCsv("1,X," + type + "," + status + ",http://a,,");
        writeGroupCsv();
        writePlayCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        service.importFromCsv(tempDir);
        verify(boardgameRepository).saveAll(boardgameCaptor.capture());
        assertAll(
                () -> assertEquals(expectedType, boardgameCaptor.getValue().get(0).getType()),
                () -> assertEquals(expectedStatus, boardgameCaptor.getValue().get(0).getStatus()));
    }

    static Stream<Arguments> boardgameTypeAndStatusCases() {
        return Stream.of(
                Arguments.of("BASEGAME", "OWN", BoardgameType.BASEGAME, BoardgameStatus.OWN),
                Arguments.of("EXPANSION", "SOLD", BoardgameType.EXPANSION, BoardgameStatus.SOLD),
                Arguments.of("BASEGAME", "THINKINGTOSELL", BoardgameType.BASEGAME, BoardgameStatus.THINKINGTOSELL),
                Arguments.of("EXPANSION", "THINKINGTOBUY", BoardgameType.EXPANSION, BoardgameStatus.THINKINGTOBUY));
    }

    @Test
    void importFromCsv_boardgame_savesMultipleRows() throws IOException {
        writeBoardgameCsv("1,Chess,BASEGAME,OWN,http://a,,", "2,Catan,EXPANSION,SOLD,http://b,,");
        writeGroupCsv();
        writePlayCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        service.importFromCsv(tempDir);
        verify(boardgameRepository).saveAll(boardgameCaptor.capture());
        assertEquals(2, boardgameCaptor.getValue().size());
    }

    @Test
    void importFromCsv_boardgame_wrongColumnCount_throwsIllegalArgument() throws IOException {
        writeBoardgameCsv("1,Chess,BASEGAME");
        writeGroupCsv();
        writePlayCsv();
        assertThrows(IllegalArgumentException.class, () -> service.importFromCsv(tempDir));
    }

    // ── CSV parsing ───────────────────────────────────────────────────────────

    @Test
    void importFromCsv_boardgame_quotedNameWithComma_parsedCorrectly() throws IOException {
        writeBoardgameCsv("1,\"War, Peace\",BASEGAME,OWN,http://a,,");
        writeGroupCsv();
        writePlayCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        service.importFromCsv(tempDir);
        verify(boardgameRepository).saveAll(boardgameCaptor.capture());
        assertEquals("War, Peace", boardgameCaptor.getValue().get(0).getName());
    }

    @Test
    void importFromCsv_boardgame_quotedNameWithEscapedQuote_parsedCorrectly() throws IOException {
        writeBoardgameCsv("1,\"He said \"\"hello\"\"\",BASEGAME,OWN,http://a,,");
        writeGroupCsv();
        writePlayCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        service.importFromCsv(tempDir);
        verify(boardgameRepository).saveAll(boardgameCaptor.capture());
        assertEquals("He said \"hello\"", boardgameCaptor.getValue().get(0).getName());
    }

    // ── boardgame_group import ────────────────────────────────────────────────

    @Test
    void importFromCsv_group_mapsGroupIdAndBoardgame() throws IOException {
        writeBoardgameCsv("1,Chess,BASEGAME,OWN,http://a,,");
        writeGroupCsv("1,3,1,2024-01-01T00:00,2024-01-01T00:00");
        writePlayCsv();
        Boardgame savedBg = Boardgame.builder().id(10L).bggLink("http://a").build();
        when(boardgameRepository.findAll()).thenReturn(List.of(savedBg));
        when(boardgameRepository.getReferenceById(10L)).thenReturn(savedBg);
        service.importFromCsv(tempDir);
        verify(boardgameGroupRepository).saveAll(groupCaptor.capture());
        BoardgameGroup saved = groupCaptor.getValue().get(0);
        assertAll(
                () -> assertNull(saved.getId()),
                () -> assertEquals(3L, saved.getGroupId()),
                () -> assertEquals(savedBg, saved.getBoardgame()));
    }

    @Test
    void importFromCsv_group_wrongColumnCount_throwsIllegalArgument() throws IOException {
        writeBoardgameCsv("1,Chess,BASEGAME,OWN,http://a,,");
        writeGroupCsv("1,3");
        writePlayCsv();
        when(boardgameRepository.findAll()).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class, () -> service.importFromCsv(tempDir));
    }

    // ── boardgame_play import ─────────────────────────────────────────────────

    @Test
    void importFromCsv_play_mapsAllFields() throws IOException {
        writeBoardgameCsv("1,Chess,BASEGAME,OWN,http://a,,");
        writeGroupCsv();
        writePlayCsv("1,1,2024-03-15,3,SUCCESS,4,2024-03-15T10:00,2024-03-15T10:00");
        Boardgame savedBg = Boardgame.builder().id(7L).bggLink("http://a").build();
        when(boardgameRepository.findAll()).thenReturn(List.of(savedBg));
        when(boardgameRepository.getReferenceById(7L)).thenReturn(savedBg);
        service.importFromCsv(tempDir);
        verify(boardgamePlayRepository).saveAll(playCaptor.capture());
        BoardgamePlay saved = playCaptor.getValue().get(0);
        assertAll(
                () -> assertNull(saved.getId()),
                () -> assertEquals(LocalDate.of(2024, 3, 15), saved.getPlayDate()),
                () -> assertEquals(3, saved.getPlayerCount()),
                () -> assertEquals(BoardgamePlayWin.SUCCESS, saved.getWin()),
                () -> assertEquals(4, saved.getFunScore()),
                () -> assertEquals(LocalDateTime.of(2024, 3, 15, 10, 0), saved.getCreatedAt()));
    }

    @ParameterizedTest
    @MethodSource("playWinCases")
    void importFromCsv_play_parsesWinField(String winCsv, BoardgamePlayWin expectedWin) throws IOException {
        writeBoardgameCsv("1,Chess,BASEGAME,OWN,http://a,,");
        writeGroupCsv();
        writePlayCsv("1,1,2024-01-01,2," + winCsv + ",5,,");
        Boardgame savedBg = Boardgame.builder().id(1L).bggLink("http://a").build();
        when(boardgameRepository.findAll()).thenReturn(List.of(savedBg));
        when(boardgameRepository.getReferenceById(1L)).thenReturn(savedBg);
        service.importFromCsv(tempDir);
        verify(boardgamePlayRepository).saveAll(playCaptor.capture());
        assertEquals(expectedWin, playCaptor.getValue().get(0).getWin());
    }

    static Stream<Arguments> playWinCases() {
        return Stream.of(
                Arguments.of("SUCCESS", BoardgamePlayWin.SUCCESS),
                Arguments.of("FAILURE", BoardgamePlayWin.FAILURE));
    }

    @Test
    void importFromCsv_play_wrongColumnCount_throwsIllegalArgument() throws IOException {
        writeBoardgameCsv("1,Chess,BASEGAME,OWN,http://a,,");
        writeGroupCsv();
        writePlayCsv("1,1,2024-01-01");
        when(boardgameRepository.findAll()).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class, () -> service.importFromCsv(tempDir));
    }
}
