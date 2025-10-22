package com.jhj.miniproject.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.miniproject.dto.BoardDto;
import com.jhj.miniproject.entity.Board;
import com.jhj.miniproject.entity.SiteUser;
import com.jhj.miniproject.service.BoardService;
import com.jhj.miniproject.service.SiteUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private SiteUserService siteUserService;

	
	// 게시글 목록 조회 (검색 기능 포함)
    @GetMapping
    public ResponseEntity<?> pageList(
            @RequestParam(name = "page", defaultValue = "0") int page, 
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "searchType", required = false) String searchType,
            @RequestParam(name = "keyword", required = false) String keyword) {

        Page<Board> boardPage;

        // 검색어가 있으면 검색, 없으면 전체 조회
        if(keyword != null && !keyword.trim().isEmpty()) {
            boardPage = boardService.searchBoards(searchType, keyword, page, size);
        } else {
            boardPage = boardService.getBoardWithPage(page, size);
        }

        Map<String, Object> pagingResponse = new HashMap<>();
        pagingResponse.put("posts", boardPage.getContent());
        pagingResponse.put("currentPage", boardPage.getNumber());
        pagingResponse.put("totalPages", boardPage.getTotalPages());
        pagingResponse.put("totalItems", boardPage.getTotalElements());

        return ResponseEntity.ok(pagingResponse);
    }
	
	// 글쓰기
	@PostMapping
	public ResponseEntity<?> write(@Valid @RequestBody BoardDto boardDto,BindingResult bindingResult, Authentication auth) {
		
		if (auth == null) {  
			return ResponseEntity.status(401).body("로그인 후 글쓰기 가능합니다.");
		}
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
					err -> {
						errors.put(err.getField(), err.getDefaultMessage());
					}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		SiteUser siteUser = siteUserService.getUser(auth.getName()).orElseThrow(() -> new RuntimeException("사용자 없음"));
		 
		Board board = new Board();
		board.setTitle(boardDto.getTitle());
		board.setContent(boardDto.getContent());
		board.setAuthor(siteUser);
		board.setView(0);
		board.setLikes(0);
		
		Board savedBoard = boardService.save(board);
		
		return ResponseEntity.ok(savedBoard);
	}
	
	// 특정 글 가져오기
	@GetMapping("/{id}")
	public ResponseEntity<?> getPost(@PathVariable("id") Long id) {
	
		Optional<Board> _board = boardService.getBoard(id);
		
		if(_board.isEmpty()) {
			return ResponseEntity.status(404).body("존재하지 않는 게시글입니다.");
		}
		
		Board board = _board.get();
		board.setView(board.getView()+1);
		
		boardService.save(board);
		
		return ResponseEntity.ok(board);
	}
	
	// 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id, Authentication auth) {
		Optional<Board> _board = boardService.getBoard(id);
		
		if(_board.isEmpty()) {
			return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
		}
		
		Board board = _board.get();
		
		if (auth == null || !auth.getName().equals(board.getAuthor().getUserId())) {
	        return ResponseEntity.status(403).body("삭제 권한이 없습니다.");
	    }
		
		boardService.deleteBoard(board);
		
		return ResponseEntity.ok(null);
	}
	
	// 수정
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(@PathVariable("id") Long id, @RequestBody Board updateBoard, Authentication auth) {
		
		Optional<Board> _board = boardService.getBoard(id);
		
		if(_board.isEmpty()) {
			return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
		}
		
		Board board = _board.get();
		
		if (auth == null || !auth.getName().equals(board.getAuthor().getUserId())) {
	        return ResponseEntity.status(403).body("수정 권한이 없습니다.");
	    }
		
		board.setTitle(updateBoard.getTitle());
		board.setContent(updateBoard.getContent());
		
		boardService.save(board);
		
		return ResponseEntity.ok(board);
	}
	
	// 추천
	@PutMapping("/{id}/recommand")
	public ResponseEntity<?> updateLikes(@PathVariable("id") Long id, Authentication auth) {
		
		if (auth == null) {
	        return ResponseEntity.status(401).body("로그인이 필요합니다.");
	    }
		
		SiteUser siteUser = siteUserService.getUser(auth.getName()).orElseThrow(() -> new RuntimeException("사용자 없음"));
		
		return boardService.likeBoard(id, siteUser);

	}
	
	
	
}
