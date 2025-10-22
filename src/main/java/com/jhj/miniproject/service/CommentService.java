package com.jhj.miniproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.miniproject.entity.Board;
import com.jhj.miniproject.entity.Comment;
import com.jhj.miniproject.entity.TacticsBoard;
import com.jhj.miniproject.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	// 댓글 저장
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	// 댓글 조회
	public List<Comment> getCommentList(Board board) {
		return commentRepository.findByBoard(board);
	}
	
	// 특정 댓글 조회
	public Optional<Comment> getComment(Long commentId) {
		return commentRepository.findById(commentId);
	}
	
	// 전술게시판 댓글 조회
	public List<Comment> getCommentListByTacticsBoard(TacticsBoard tacticsBoard) {
		return commentRepository.findByTacticsBoard(tacticsBoard);
	}
	
	// 특정 댓글 삭제
	public void dropComment(Long id) {
		commentRepository.deleteById(id);
	}
	
	
}
