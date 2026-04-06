package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.constans.BoardgameStatus;
import com.home.carcosa.boardgame.constans.BoardgameType;
import com.home.carcosa.boardgame.dto.BoardgameDto;
import com.home.carcosa.boardgame.entity.Boardgame;
import com.home.carcosa.boardgame.mapper.BoardgameMapper;
import com.home.carcosa.boardgame.repository.BoardgameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgameServiceTest {

    @Mock
    BoardgameRepository boardgameRepository;
    @Mock
    BoardgameMapper boardgameMapper;
    @InjectMocks
    BoardgameService service;

    static Stream<BoardgameDto> newDtos() {
        return Stream.of(
                new BoardgameDto(null, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "http://a", null, null),
                new BoardgameDto(null, "Catan", BoardgameType.EXPANSION, BoardgameStatus.SOLD, "http://b", null, null),
                new BoardgameDto(null, "Go", BoardgameType.BASEGAME, BoardgameStatus.THINKINGTOBUY, "http://c", null,
                        null));
    }

    static Stream<BoardgameDto> existingDtos() {
        return Stream.of(
                new BoardgameDto(1L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "http://a", null, null),
                new BoardgameDto(2L, "Catan", BoardgameType.EXPANSION, BoardgameStatus.SOLD, "http://b", null, null));
    }

    // ── save – new (null id) ───────────────────────────────────────────────

    @ParameterizedTest
    @MethodSource("newDtos")
    void save_nullId_usesMapperToEntityThenSaves(BoardgameDto dto) {
        Boardgame entity = new Boardgame();
        when(boardgameMapper.toEntity(dto)).thenReturn(entity);
        when(boardgameRepository.save(entity)).thenReturn(entity);
        when(boardgameMapper.toDto(entity)).thenReturn(dto);

        assertEquals(dto, service.save(dto));
    }

    @Test
    void save_nullId_neverCallsFindById() {
        BoardgameDto dto = new BoardgameDto(null, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", null,
                null);
        Boardgame entity = new Boardgame();
        when(boardgameMapper.toEntity(dto)).thenReturn(entity);
        when(boardgameRepository.save(entity)).thenReturn(entity);
        when(boardgameMapper.toDto(entity)).thenReturn(dto);

        service.save(dto);
        verify(boardgameRepository, never()).findById(any());
    }

    // ── save – update (non-null id) ────────────────────────────────────────

    @ParameterizedTest
    @MethodSource("existingDtos")
    void save_existingId_updatesFieldsOnFoundEntity(BoardgameDto dto) {
        Boardgame existing = Boardgame.builder().id(dto.id()).name("Old").build();
        when(boardgameRepository.findById(dto.id())).thenReturn(Optional.of(existing));
        when(boardgameRepository.save(existing)).thenReturn(existing);
        when(boardgameMapper.toDto(existing)).thenReturn(dto);

        service.save(dto);
        assertAll(
                () -> assertEquals(dto.name(), existing.getName()),
                () -> assertEquals(dto.type(), existing.getType()),
                () -> assertEquals(dto.status(), existing.getStatus()),
                () -> assertEquals(dto.bggLink(), existing.getBggLink()));
    }

    @Test
    void save_existingId_throwsWhenBoardgameNotFound() {
        BoardgameDto dto = new BoardgameDto(99L, "X", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", null, null);
        when(boardgameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.save(dto));
    }

    @Test
    void save_existingId_neverCallsMapperToEntity() {
        BoardgameDto dto = new BoardgameDto(1L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", null, null);
        Boardgame existing = Boardgame.builder().id(1L).build();
        when(boardgameRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(boardgameRepository.save(existing)).thenReturn(existing);
        when(boardgameMapper.toDto(existing)).thenReturn(dto);

        service.save(dto);
        verify(boardgameMapper, never()).toEntity(any());
    }

    // ── findAll() ────────────────────────────────────────────────────────────

    @Test
    void findAll_returnsDtosMappedFromRepository() {
        Boardgame entity = new Boardgame();
        BoardgameDto dto = new BoardgameDto(1L, "Chess", BoardgameType.BASEGAME, BoardgameStatus.OWN, "l", null, null);
        when(boardgameRepository.findAll()).thenReturn(List.of(entity));
        when(boardgameMapper.toDto(entity)).thenReturn(dto);

        assertEquals(List.of(dto), service.findAll());
    }

    @Test
    void findAll_returnsEmptyListWhenRepositoryIsEmpty() {
        when(boardgameRepository.findAll()).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    // ── findAll(Sort) ────────────────────────────────────────────────────────

    @Test
    void findAllSorted_delegatesToRepositoryWithGivenSort() {
        Sort sort = Sort.by("name");
        List<Boardgame> expected = List.of(new Boardgame());
        when(boardgameRepository.findAll(sort)).thenReturn(expected);

        assertEquals(expected, service.findAll(sort));
    }

    @Test
    void findAllSorted_passesExactSortToRepository() {
        Sort sort = Sort.by(Sort.Direction.DESC, "status");
        when(boardgameRepository.findAll(sort)).thenReturn(List.of());

        service.findAll(sort);
        verify(boardgameRepository).findAll(sort);
    }

    @Test
    void findAllSorted_returnsEmptyListWhenRepositoryIsEmpty() {
        Sort sort = Sort.by("name");
        when(boardgameRepository.findAll(sort)).thenReturn(List.of());

        assertTrue(service.findAll(sort).isEmpty());
    }

    // ── findByNameContainingIgnoreCase ────────────────────────────────────────

    @Test
    void findByName_delegatesToRepositoryWithNameAndSort() {
        Sort sort = Sort.by("name");
        List<Boardgame> expected = List.of(new Boardgame());
        when(boardgameRepository.findByNameContainingIgnoreCase("chess", sort)).thenReturn(expected);

        assertEquals(expected, service.findByNameContainingIgnoreCase("chess", sort));
    }

    @Test
    void findByName_returnsEmptyListWhenNoMatches() {
        Sort sort = Sort.by("name");
        when(boardgameRepository.findByNameContainingIgnoreCase("zzz", sort)).thenReturn(List.of());

        assertTrue(service.findByNameContainingIgnoreCase("zzz", sort).isEmpty());
    }

    @Test
    void findByName_passesExactNameAndSortToRepository() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        when(boardgameRepository.findByNameContainingIgnoreCase("chess", sort)).thenReturn(List.of());

        service.findByNameContainingIgnoreCase("chess", sort);
        verify(boardgameRepository).findByNameContainingIgnoreCase("chess", sort);
    }
}
