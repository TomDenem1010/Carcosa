package com.home.carcosa.boardgame.repository;

import com.home.carcosa.boardgame.entity.BoardgamePlay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardgamePlayRepository extends JpaRepository<BoardgamePlay, Long> {
}
