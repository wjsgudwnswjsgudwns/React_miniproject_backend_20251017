package com.jhj.miniproject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.miniproject.dto.CommentDto;
import com.jhj.miniproject.entity.Board;
import com.jhj.miniproject.entity.Comment;
import com.jhj.miniproject.entity.SiteUser;
import com.jhj.miniproject.service.BoardService;
import com.jhj.miniproject.service.CommentService;
import com.jhj.miniproject.service.SiteUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private SiteUserService siteUserService;
	
	@Autowired
	private BoardService boardService;
	
	// 댓글 등록
	@PostMapping("/{id}")
	public ResponseEntity<?> writeComment(@PathVariable("id") Long id,@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult, Authentication auth) {
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
					err -> {
						errors.put(err.getField(), err.getDefaultMessage());
					}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		Optional<Board> _board = boardService.getBoard(id);
		if(_board.isEmpty()) {
			return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
		}
		
		SiteUser user = siteUserService.getUser(auth.getName()).orElseThrow();
		
		Comment comment = new Comment();
		comment.setBoard(_board.get());
		comment.setContent(commentDto.getContent());
		comment.setAuthor(user);
		
		commentService.save(comment);
		
		return ResponseEntity.ok(comment);
	}
	
	// 댓글 조회
	@GetMapping("/{boardId}")
	public ResponseEntity<?> loadComment(@PathVariable("boardId") Long boardId) {
		
		Optional<Board> _board = boardService.getBoard(boardId);
		if(_board.isEmpty()) {
			return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
		}
		
		Board board = _board.get();
		
		List<Comment> comments = commentService.getCommentList(board);
		
		return ResponseEntity.ok(comments);
	}
	
	// 댓글 수정
	@PutMapping("/{commentId}")
	public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentDto commentDto, Authentication auth) {
		Optional<Comment> _comment = commentService.getComment(commentId);
		
		if(_comment.isEmpty()) {
			return ResponseEntity.status(404).body("해당 댓글이 존재하지 않습니다.");
		}
		
		Comment comment = _comment.get();
		
		if(!comment.getAuthor().getUserId().equals(auth.getName())) {
			return ResponseEntity.status(403).body("수정 권한이 없습니다");
		}
		 
		comment.setContent(commentDto.getContent());
		commentService.save(comment);
		 
		return ResponseEntity.ok(comment);
	}
	
	// 댓글 삭제
	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable("commnetId") Long commentId, Authentication auth) {
		Optional<Comment> _comment = commentService.getComment(commentId);
		
		if(_comment.isEmpty()) {
			return ResponseEntity.status(404).body("해당 댓글이 존재하지 않습니다.");
		}
		
		Comment comment = _comment.get();
		
		if(!comment.getAuthor().getUserId().equals(auth.getName())) {
			return ResponseEntity.status(403).body("수정 권한이 없습니다");
		}
		
		commentService.dropComment(commentId);
		
		return ResponseEntity.ok(null);
	}
}
