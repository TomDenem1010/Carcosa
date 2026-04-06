package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.service.BoardgameTableCsvExportService;
import com.home.carcosa.boardgame.service.BoardgameTableCsvImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminBoardgameControllerTest {

    @Mock
    BoardgameTableCsvExportService exportService;
    @Mock
    BoardgameTableCsvImportService importService;
    @InjectMocks
    AdminBoardgameController controller;

    @Test
    void exportToCsv_callsExportServiceAndReturns204() throws IOException {
        ResponseEntity<Void> response = controller.exportToCsv();
        verify(exportService).exportToCsv(any());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void exportToCsv_wrapsIoExceptionAsInternalServerError() throws IOException {
        doThrow(new IOException("disk error")).when(exportService).exportToCsv(any());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> controller.exportToCsv());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
    }

    @Test
    void importFromCsv_callsImportServiceAndReturns204() throws IOException {
        ResponseEntity<Void> response = controller.importFromCsv();
        verify(importService).importFromCsv(any());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void importFromCsv_wrapsIoExceptionAsInternalServerError() throws IOException {
        doThrow(new IOException("disk error")).when(importService).importFromCsv(any());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> controller.importFromCsv());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
    }
}
