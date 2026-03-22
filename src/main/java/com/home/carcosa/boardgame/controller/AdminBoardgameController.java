package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.service.BoardgameTableCsvExportService;
import com.home.carcosa.boardgame.service.BoardgameTableCsvImportService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/admin/boardgame")
@RequiredArgsConstructor
public class AdminBoardgameController {

    private static final Path DEFAULT_DIRECTORY = Path.of("database", "boardgame", "csv");

    private final BoardgameTableCsvExportService boardgameTableCsvExportService;
    private final BoardgameTableCsvImportService boardgameTableCsvImportService;

    @PostMapping("/exportToCsv")
    public ResponseEntity<Void> exportToCsv() {
        try {
            boardgameTableCsvExportService.exportToCsv(DEFAULT_DIRECTORY);
            return ResponseEntity.noContent().build();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CSV export failed", ex);
        }
    }

    @PostMapping("/importFromCsv")
    public ResponseEntity<Void> importFromCsv() {
        try {
            boardgameTableCsvImportService.importFromCsv(DEFAULT_DIRECTORY);
            return ResponseEntity.noContent().build();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CSV import failed", ex);
        }
    }
}
