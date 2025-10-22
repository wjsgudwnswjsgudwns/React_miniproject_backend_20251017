package com.jhj.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.miniproject.entity.Board;
import com.jhj.miniproject.entity.BoardLike;
import com.jhj.miniproject.entity.SiteUser;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
	
	public boolean existsByBoardAndUser(Board board, SiteUser user);
	
}
