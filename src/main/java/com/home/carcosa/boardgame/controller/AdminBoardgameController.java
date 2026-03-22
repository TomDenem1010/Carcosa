package com.home.carcosa.boardgame.controller;

import com.home.carcosa.boardgame.service.BoardgameTableCsvExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/admin/boardgame")
@RequiredArgsConstructor
public class AdminBoardgameController {

    private final BoardgameTableCsvExportService boardgameTableCsvExportService;

    @PostMapping("/exportToCsv")
    public ResponseEntity<Void> exportToCsv() {
        try {
            boardgameTableCsvExportService.exportToCsv();
            return ResponseEntity.noContent().build();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CSV export failed", ex);
        }
    }
}
