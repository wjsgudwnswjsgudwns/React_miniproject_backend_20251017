package com.jhj.miniproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.miniproject.entity.Board;
import com.jhj.miniproject.entity.Comment;
import com.jhj.miniproject.entity.TacticsBoard;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	public List<Comment> findByBoard(Board board);
	
	public List<Comment> findByTacticsBoard(TacticsBoard tacticsBoard);
}
