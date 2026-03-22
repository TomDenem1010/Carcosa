package com.home.carcosa.boardgame.repository;

import com.home.carcosa.boardgame.entity.BoardgamePlay;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardgamePlayRepository extends JpaRepository<BoardgamePlay, Long> {

    @Override
    @EntityGraph(attributePaths = { "boardgame" })
    List<BoardgamePlay> findAll(Sort sort);

    @EntityGraph(attributePaths = { "boardgame" })
    List<BoardgamePlay> findByBoardgame_NameContainingIgnoreCase(String name, Sort sort);
}
