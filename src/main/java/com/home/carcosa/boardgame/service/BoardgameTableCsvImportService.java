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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoardgameTableCsvImportService {

    private final BoardgameRepository boardgameRepository;
    private final BoardgameGroupRepository boardgameGroupRepository;
    private final BoardgamePlayRepository boardgamePlayRepository;

    public BoardgameTableCsvImportService(
            BoardgameRepository boardgameRepository,
            BoardgameGroupRepository boardgameGroupRepository,
            BoardgamePlayRepository boardgamePlayRepository) {
        this.boardgameRepository = boardgameRepository;
        this.boardgameGroupRepository = boardgameGroupRepository;
        this.boardgamePlayRepository = boardgamePlayRepository;
    }

    @Transactional
    public void importFromCsv(Path inputDirectory) throws IOException {
        Map<Long, String> csvBoardgameIdToBggLink = importBoardgame(inputDirectory.resolve("boardgame.csv"));
        Map<String, Long> bggLinkToBoardgameId = loadBoardgameIdByBggLink();

        importBoardgameGroup(inputDirectory.resolve("boardgame_group.csv"), csvBoardgameIdToBggLink,
                bggLinkToBoardgameId);
        importBoardgamePlay(inputDirectory.resolve("boardgame_play.csv"), csvBoardgameIdToBggLink,
                bggLinkToBoardgameId);
    }

    private Map<Long, String> importBoardgame(Path csvFile) throws IOException {
        List<List<String>> records = readCsvFile(csvFile);
        List<Boardgame> toSave = new ArrayList<>(records.size());
        Map<Long, String> csvIdToBggLink = new HashMap<>(Math.max(16, records.size()));

        for (List<String> row : records) {
            requireColumns(csvFile, row, 7);

            Long csvId = parseLong(row.get(0));
            String name = row.get(1);
            BoardgameType type = parseEnum(BoardgameType.class, row.get(2));
            BoardgameStatus status = parseEnum(BoardgameStatus.class, row.get(3));
            String bggLink = row.get(4);
            LocalDateTime createdAt = parseLocalDateTime(row.get(5));
            LocalDateTime updatedAt = parseLocalDateTime(row.get(6));

            csvIdToBggLink.put(csvId, bggLink);

            toSave.add(Boardgame.builder()
                    .id(null)
                    .name(name)
                    .type(type)
                    .status(status)
                    .bggLink(bggLink)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build());
        }

        if (!toSave.isEmpty()) {
            boardgameRepository.saveAll(toSave);
        }

        return csvIdToBggLink;
    }

    private void importBoardgameGroup(
            Path csvFile,
            Map<Long, String> csvBoardgameIdToBggLink,
            Map<String, Long> bggLinkToBoardgameId) throws IOException {
        List<List<String>> records = readCsvFile(csvFile);
        List<BoardgameGroup> toSave = new ArrayList<>(records.size());

        for (List<String> row : records) {
            requireColumns(csvFile, row, 5);

            Long groupId = parseLong(row.get(1));
            Long boardgameId = parseLong(row.get(2));
            LocalDateTime createdAt = parseLocalDateTime(row.get(3));
            LocalDateTime updatedAt = parseLocalDateTime(row.get(4));

            toSave.add(BoardgameGroup.builder()
                    .id(null)
                    .groupId(groupId)
                    .boardgame(
                            boardgameRepository.getReferenceById(
                                    bggLinkToBoardgameId.get(
                                            csvBoardgameIdToBggLink.get(boardgameId))))
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build());
        }

        if (!toSave.isEmpty()) {
            boardgameGroupRepository.saveAll(toSave);
        }
    }

    private void importBoardgamePlay(
            Path csvFile,
            Map<Long, String> csvBoardgameIdToBggLink,
            Map<String, Long> bggLinkToBoardgameId) throws IOException {
        List<List<String>> records = readCsvFile(csvFile);
        List<BoardgamePlay> toSave = new ArrayList<>(records.size());

        for (List<String> row : records) {
            requireColumns(csvFile, row, 8);

            Long boardgameId = parseLong(row.get(1));
            LocalDate playDate = parseLocalDate(row.get(2));
            Integer playerCount = parseInt(row.get(3));
            BoardgamePlayWin win = parseEnum(BoardgamePlayWin.class, row.get(4));
            Integer funScore = parseInt(row.get(5));
            LocalDateTime createdAt = parseLocalDateTime(row.get(6));
            LocalDateTime updatedAt = parseLocalDateTime(row.get(7));

            toSave.add(BoardgamePlay.builder()
                    .id(null)
                    .boardgame(
                            boardgameRepository.getReferenceById(
                                    bggLinkToBoardgameId.get(
                                            csvBoardgameIdToBggLink.get(boardgameId))))
                    .playDate(playDate)
                    .playerCount(playerCount)
                    .win(win)
                    .funScore(funScore)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build());
        }

        if (!toSave.isEmpty()) {
            boardgamePlayRepository.saveAll(toSave);
        }
    }

    private Map<String, Long> loadBoardgameIdByBggLink() {
        return boardgameRepository.findAll().stream()
                .filter(bg -> bg.getBggLink() != null && !bg.getBggLink().isBlank())
                .collect(Collectors.toMap(
                        Boardgame::getBggLink,
                        Boardgame::getId,
                        (a, b) -> a));
    }

    private static void requireColumns(Path csvFile, List<String> row, int expectedColumns) {
        if (row.size() != expectedColumns) {
            throw new IllegalArgumentException(
                    "Unexpected column count in " + csvFile.getFileName() + ": expected " + expectedColumns + ", got "
                            + row.size());
        }
    }

    private static Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Long.parseLong(value);
    }

    private static Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Integer.parseInt(value);
    }

    private static LocalDate parseLocalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value);
    }

    private static LocalDateTime parseLocalDateTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(value);
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Enum.valueOf(enumClass, value);
    }

    private static List<List<String>> readCsvFile(Path csvFile) throws IOException {
        if (!Files.exists(csvFile)) {
            throw new FileNotFoundException("CSV file not found: " + csvFile);
        }

        try (BufferedReader reader = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null) {
                return List.of();
            }

            List<List<String>> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String record = line;
                while (!isCompleteCsvRecord(record)) {
                    String next = reader.readLine();
                    if (next == null) {
                        break;
                    }
                    record = record + "\n" + next;
                }

                if (record.isBlank()) {
                    continue;
                }

                rows.add(parseCsvRecord(record));
            }
            return rows;
        }
    }

    private static boolean isCompleteCsvRecord(String record) {
        boolean inQuotes = false;
        for (int i = 0; i < record.length(); i++) {
            char ch = record.charAt(i);
            if (ch != '"') {
                continue;
            }
            if (inQuotes) {
                if (i + 1 < record.length() && record.charAt(i + 1) == '"') {
                    i++;
                    continue;
                }
                inQuotes = false;
            } else {
                inQuotes = true;
            }
        }
        return !inQuotes;
    }

    private static List<String> parseCsvRecord(String record) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < record.length(); i++) {
            char ch = record.charAt(i);
            if (inQuotes) {
                if (ch == '"') {
                    if (i + 1 < record.length() && record.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(ch);
                }
                continue;
            }

            if (ch == ',') {
                fields.add(current.toString());
                current.setLength(0);
                continue;
            }

            if (ch == '"') {
                inQuotes = true;
                continue;
            }

            current.append(ch);
        }

        fields.add(current.toString());
        return fields;
    }
}
