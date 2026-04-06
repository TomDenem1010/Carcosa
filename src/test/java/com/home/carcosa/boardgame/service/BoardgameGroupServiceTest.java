package com.home.carcosa.boardgame.service;

import com.home.carcosa.boardgame.dto.BoardgameGroupDto;
import com.home.carcosa.boardgame.entity.BoardgameGroup;
import com.home.carcosa.boardgame.mapper.BoardgameGroupMapper;
import com.home.carcosa.boardgame.repository.BoardgameGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardgameGroupServiceTest {

    @Mock
    BoardgameGroupRepository boardgameGroupRepository;
    @Mock
    BoardgameGroupMapper boardgameGroupMapper;
    @InjectMocks
    BoardgameGroupService service;

    static Stream<BoardgameGroupDto> dtos() {
        return Stream.of(
                new BoardgameGroupDto(1L, 10L, 100L, null, null),
                new BoardgameGroupDto(2L, 20L, 200L, null, null),
                new BoardgameGroupDto(null, 1L, 999L, null, null));
    }

    // ── save ────────────────────────────────────────────────────────────────

    @ParameterizedTest
    @MethodSource("dtos")
    void save_mapsToEntityThenSavesThenMapsToDto(BoardgameGroupDto dto) {
        BoardgameGroup entity = new BoardgameGroup();
        when(boardgameGroupMapper.toEntity(dto)).thenReturn(entity);
        when(boardgameGroupRepository.save(entity)).thenReturn(entity);
        when(boardgameGroupMapper.toDto(entity)).thenReturn(dto);

        assertEquals(dto, service.save(dto));
    }

    @Test
    void save_callsRepositorySaveExactlyOnce() {
        BoardgameGroupDto dto = new BoardgameGroupDto(1L, 10L, 100L, null, null);
        BoardgameGroup entity = new BoardgameGroup();
        when(boardgameGroupMapper.toEntity(dto)).thenReturn(entity);
        when(boardgameGroupRepository.save(entity)).thenReturn(entity);
        when(boardgameGroupMapper.toDto(entity)).thenReturn(dto);

        service.save(dto);
        verify(boardgameGroupRepository, times(1)).save(entity);
    }

    // ── findAll() ────────────────────────────────────────────────────────────

    @ParameterizedTest
    @MethodSource("dtos")
    void findAll_returnsDtosMappedFromRepository(BoardgameGroupDto dto) {
        BoardgameGroup entity = new BoardgameGroup();
        when(boardgameGroupRepository.findAll()).thenReturn(List.of(entity));
        when(boardgameGroupMapper.toDto(entity)).thenReturn(dto);

        assertEquals(List.of(dto), service.findAll());
    }

    @Test
    void findAll_returnsEmptyListWhenRepositoryIsEmpty() {
        when(boardgameGroupRepository.findAll()).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    // ── findAll(Sort) ────────────────────────────────────────────────────────

    @Test
    void findAllSorted_delegatesToRepositoryWithGivenSort() {
        Sort sort = Sort.by("groupId");
        List<BoardgameGroup> expected = List.of(new BoardgameGroup());
        when(boardgameGroupRepository.findAll(sort)).thenReturn(expected);

        assertEquals(expected, service.findAll(sort));
    }

    @Test
    void findAllSorted_returnsEmptyListWhenRepositoryIsEmpty() {
        Sort sort = Sort.by("groupId");
        when(boardgameGroupRepository.findAll(sort)).thenReturn(List.of());

        assertTrue(service.findAll(sort).isEmpty());
    }

    @Test
    void findAllSorted_passesExactSortToRepository() {
        Sort sort = Sort.by(Sort.Direction.DESC, "groupId");
        when(boardgameGroupRepository.findAll(sort)).thenReturn(List.of());

        service.findAll(sort);
        verify(boardgameGroupRepository).findAll(sort);
    }
}
