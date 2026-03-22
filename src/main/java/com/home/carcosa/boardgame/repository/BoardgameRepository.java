package com.home.carcosa.boardgame.repository;

import com.home.carcosa.boardgame.entity.Boardgame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardgameRepository extends JpaRepository<Boardgame, Long> {
}
