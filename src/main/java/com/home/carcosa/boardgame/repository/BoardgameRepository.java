package com.home.carcosa.boardgame.repository;

import com.home.carcosa.boardgame.entity.Boardgame;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardgameRepository extends JpaRepository<Boardgame, Long> {

    List<Boardgame> findByNameContainingIgnoreCase(String name, Sort sort);
}
