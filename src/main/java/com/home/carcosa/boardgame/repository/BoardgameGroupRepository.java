package com.home.carcosa.boardgame.repository;

import com.home.carcosa.boardgame.entity.BoardgameGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BoardgameGroupRepository extends JpaRepository<BoardgameGroup, Long> {

	@Override
	@EntityGraph(attributePaths = { "boardgame" })
	List<BoardgameGroup> findAll(Sort sort);
}
