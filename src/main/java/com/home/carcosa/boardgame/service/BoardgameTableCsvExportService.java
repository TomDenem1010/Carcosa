package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import com.home.carcosa.boardgame.entity.BoardgamePlay;
import com.home.carcosa.boardgame.repository.BoardgameGroupRepository;
import com.home.carcosa.boardgame.repository.BoardgamePlayRepository;
import com.home.carcosa.boardgame.repository.BoardgameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class BoardgameTableCsvExportService {

    private final BoardgameRepository boardgameRepository;
    private final BoardgameGroupRepository boardgameGroupRepository;
    private final BoardgamePlayRepository boardgamePlayRepository;

    public BoardgameTableCsvExportService(
            BoardgameRepository boardgameRepository,
            BoardgameGroupRepository boardgameGroupRepository,
            BoardgamePlayRepository boardgamePlayRepository) {
        this.boardgameRepository = boardgameRepository;
        this.boardgameGroupRepository = boardgameGroupRepository;
        this.boardgamePlayRepository = boardgamePlayRepository;
    }

    @Transactional(readOnly = true)
    public void exportToCsv(Path outputDirectory) throws IOException {
        Files.createDirectories(outputDirectory);

        exportBoardgame(outputDirectory.resolve("boardgame.csv"));
        exportBoardgameGroup(outputDirectory.resolve("boardgame_group.csv"));
        exportBoardgamePlay(outputDirectory.resolve("boardgame_play.csv"));
    }

    private void exportBoardgame(Path outputFile) throws IOException {
        try (BufferedWriter writer = newWriter(outputFile)) {
            writer.write("ID,NAME,TYPE,STATUS,BGG_LINK,CREATED_AT,UPDATED_AT");
            writer.newLine();

            for (Boardgame row : boardgameRepository.findAll()) {
                writer.write(joinCsv(
                        row.getId(),
                        row.getName(),
                        row.getType(),
                        row.getStatus(),
                        row.getBggLink(),
                        row.getCreatedAt(),
                        row.getUpdatedAt()));
                writer.newLine();
            }
        }
    }

    private void exportBoardgameGroup(Path outputFile) throws IOException {
        try (BufferedWriter writer = newWriter(outputFile)) {
            writer.write("ID,GROUP_ID,BOARDGAME_ID,CREATED_AT,UPDATED_AT");
            writer.newLine();

            for (BoardgameGroup row : boardgameGroupRepository.findAll()) {
                writer.write(joinCsv(
                        row.getId(),
                        row.getGroupId(),
                        row.getBoardgame().getId(),
                        row.getCreatedAt(),
                        row.getUpdatedAt()));
                writer.newLine();
            }
        }
    }

    private void exportBoardgamePlay(Path outputFile) throws IOException {
        try (BufferedWriter writer = newWriter(outputFile)) {
            writer.write("ID,BOARDGAME_ID,PLAY_DATE,PLAYER_COUNT,WIN,FUN_SCORE,CREATED_AT,UPDATED_AT");
            writer.newLine();

            for (BoardgamePlay row : boardgamePlayRepository.findAll()) {
                writer.write(joinCsv(
                        row.getId(),
                        row.getBoardgame().getId(),
                        row.getPlayDate(),
                        row.getPlayerCount(),
                        row.getWin(),
                        row.getFunScore(),
                        row.getCreatedAt(),
                        row.getUpdatedAt()));
                writer.newLine();
            }
        }
    }

    private static BufferedWriter newWriter(Path outputFile) throws IOException {
        return Files.newBufferedWriter(
                outputFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static String joinCsv(Object... values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(escapeCsv(formatCell(values[i])));
        }
        return sb.toString();
    }

    private static String formatCell(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof LocalDateTime ldt) {
            return ldt.toString();
        }
        if (value instanceof LocalDate ld) {
            return ld.toString();
        }
        return value.toString();
    }

    private static String escapeCsv(String input) {
        if (input == null) {
            return "";
        }

        boolean mustQuote = false;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == ',' || ch == '"' || ch == '\n' || ch == '\r') {
                mustQuote = true;
                break;
            }
        }

        if (!mustQuote) {
            return input;
        }

        String escapedQuotes = input.replace("\"", "\"\"");
        return "\"" + escapedQuotes + "\"";
    }
}
