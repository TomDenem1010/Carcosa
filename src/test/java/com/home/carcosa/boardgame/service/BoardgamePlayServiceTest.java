package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.constans.BoardgamePlayWin;
import com.home.carcosa.boardgame.dto.BoardgamePlayDto;
import com.home.carcosa.boardgame.entity.BoardgamePlay;
import com.home.carcosa.boardgame.mapper.BoardgamePlayMapper;
import com.home.carcosa.boardgame.repository.BoardgamePlayRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgamePlayServiceTest {

    @Mock
    BoardgamePlayRepository boardgamePlayRepository;
    @Mock
    BoardgamePlayMapper boardgamePlayMapper;
    @InjectMocks
    BoardgamePlayService service;

    static Stream<BoardgamePlayDto> dtos() {
        return Stream.of(
                new BoardgamePlayDto(1L, 10L, LocalDate.of(2024, 1, 1), 2, BoardgamePlayWin.SUCCESS, 5, null, null),
                new BoardgamePlayDto(2L, 20L, LocalDate.of(2024, 6, 15), 4, BoardgamePlayWin.FAILURE, 3, null, null),
                new BoardgamePlayDto(null, 30L, LocalDate.of(2025, 1, 1), 1, BoardgamePlayWin.SUCCESS, 4, null, null));
    }

    // ── save ────────────────────────────────────────────────────────────────

    @ParameterizedTest
    @MethodSource("dtos")
    void save_mapsToEntityThenSavesThenMapsToDto(BoardgamePlayDto dto) {
        BoardgamePlay entity = new BoardgamePlay();
        when(boardgamePlayMapper.toEntity(dto)).thenReturn(entity);
        when(boardgamePlayRepository.save(entity)).thenReturn(entity);
        when(boardgamePlayMapper.toDto(entity)).thenReturn(dto);

        assertEquals(dto, service.save(dto));
    }

    @Test
    void save_callsRepositorySaveExactlyOnce() {
        BoardgamePlayDto dto = new BoardgamePlayDto(1L, 10L, LocalDate.now(), 2, BoardgamePlayWin.SUCCESS, 5, null,
                null);
        BoardgamePlay entity = new BoardgamePlay();
        when(boardgamePlayMapper.toEntity(dto)).thenReturn(entity);
        when(boardgamePlayRepository.save(entity)).thenReturn(entity);
        when(boardgamePlayMapper.toDto(entity)).thenReturn(dto);

        service.save(dto);
        verify(boardgamePlayRepository, times(1)).save(entity);
    }

    // ── findAll() ────────────────────────────────────────────────────────────

    @ParameterizedTest
    @MethodSource("dtos")
    void findAll_returnsDtosMappedFromRepository(BoardgamePlayDto dto) {
        BoardgamePlay entity = new BoardgamePlay();
        when(boardgamePlayRepository.findAll()).thenReturn(List.of(entity));
        when(boardgamePlayMapper.toDto(entity)).thenReturn(dto);

        assertEquals(List.of(dto), service.findAll());
    }

    @Test
    void findAll_returnsEmptyListWhenRepositoryIsEmpty() {
        when(boardgamePlayRepository.findAll()).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    // ── findAll(Sort) ────────────────────────────────────────────────────────

    @Test
    void findAllSorted_delegatesToRepositoryWithGivenSort() {
        Sort sort = Sort.by("playDate");
        List<BoardgamePlay> expected = List.of(new BoardgamePlay());
        when(boardgamePlayRepository.findAll(sort)).thenReturn(expected);

        assertEquals(expected, service.findAll(sort));
    }

    @Test
    void findAllSorted_returnsEmptyListWhenRepositoryIsEmpty() {
        Sort sort = Sort.by("playDate");
        when(boardgamePlayRepository.findAll(sort)).thenReturn(List.of());

        assertTrue(service.findAll(sort).isEmpty());
    }

    @Test
    void findAllSorted_passesExactSortToRepository() {
        Sort sort = Sort.by(Sort.Direction.DESC, "funScore");
        when(boardgamePlayRepository.findAll(sort)).thenReturn(List.of());

        service.findAll(sort);
        verify(boardgamePlayRepository).findAll(sort);
    }

    // ── findByBoardgameNameContainingIgnoreCase ───────────────────────────────

    @Test
    void findByBoardgameName_delegatesToRepositoryWithNameAndSort() {
        Sort sort = Sort.by("playDate");
        List<BoardgamePlay> expected = List.of(new BoardgamePlay());
        when(boardgamePlayRepository.findByBoardgame_NameContainingIgnoreCase("chess", sort)).thenReturn(expected);

        assertEquals(expected, service.findByBoardgameNameContainingIgnoreCase("chess", sort));
    }

    @Test
    void findByBoardgameName_returnsEmptyListWhenNoMatches() {
        Sort sort = Sort.by("playDate");
        when(boardgamePlayRepository.findByBoardgame_NameContainingIgnoreCase("zzz", sort)).thenReturn(List.of());

        assertTrue(service.findByBoardgameNameContainingIgnoreCase("zzz", sort).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("dtos")
    void findByBoardgameName_passesExactNameToRepository(BoardgamePlayDto dto) {
        String name = "chess";
        Sort sort = Sort.by("playDate");
        when(boardgamePlayRepository.findByBoardgame_NameContainingIgnoreCase(name, sort)).thenReturn(List.of());

        service.findByBoardgameNameContainingIgnoreCase(name, sort);
        verify(boardgamePlayRepository).findByBoardgame_NameContainingIgnoreCase(name, sort);
    }
}
