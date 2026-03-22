package com.home.carcosa.boardgame.repository;

import com.home.carcosa.boardgame.entity.BoardgameGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardgameGroupRepository extends JpaRepository<BoardgameGroup, Long> {
}
