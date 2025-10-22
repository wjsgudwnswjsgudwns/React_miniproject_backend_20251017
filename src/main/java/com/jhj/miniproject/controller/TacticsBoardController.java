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

import com.jhj.miniproject.dto.TacticsBoardDto;
import com.jhj.miniproject.entity.SiteUser;
import com.jhj.miniproject.entity.TacticsBoard;
import com.jhj.miniproject.service.SiteUserService;
import com.jhj.miniproject.service.TacticsBoardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tactics")
public class TacticsBoardController {
	
	@Autowired
	private TacticsBoardService tacticsBoardService;
	
	@Autowired
	private SiteUserService siteUserService;
	
	// 게시글 목록 조회 (검색 기능 포함)
    @GetMapping
    public ResponseEntity<?> pageList(
        @RequestParam(name = "page", defaultValue = "0") int page, 
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "searchType", required = false) String searchType,
        @RequestParam(name = "keyword", required = false) String keyword) {

    Page<TacticsBoard> tacticsBoardPage;

    // 검색어가 있으면 검색, 없으면 전체 조회
    if(keyword != null && !keyword.trim().isEmpty()) {
    	tacticsBoardPage = tacticsBoardService.searchTacticsBoard(searchType, keyword, page, size);
    } else {
    	tacticsBoardPage = tacticsBoardService.getTacticsBoardWithPage(page, size);
    }

    Map<String, Object> pagingResponse = new HashMap<>();
    pagingResponse.put("posts", tacticsBoardPage.getContent());
    pagingResponse.put("currentPage", tacticsBoardPage.getNumber());
    pagingResponse.put("totalPages", tacticsBoardPage.getTotalPages());
    pagingResponse.put("totalItems", tacticsBoardPage.getTotalElements());

    return ResponseEntity.ok(pagingResponse);
    }
    
    // 글쓰기
 	@PostMapping
 	public ResponseEntity<?> write(@Valid @RequestBody TacticsBoardDto tacticsBoardDto,BindingResult bindingResult, Authentication auth) {
 		
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
 		 
 		TacticsBoard tacticsBoard = new TacticsBoard();
 		tacticsBoard.setTitle(tacticsBoardDto.getTitle());
 		tacticsBoard.setContent(tacticsBoardDto.getContent());
 		tacticsBoard.setAuthor(siteUser);
 		tacticsBoard.setView(0);
 		tacticsBoard.setLikes(0);
 		
 		TacticsBoard savedTacticsBoard = tacticsBoardService.save(tacticsBoard);
 		
 		return ResponseEntity.ok(savedTacticsBoard);
 	}
    
    // 특정 글 가져오기
 	@GetMapping("/{id}")
 	public ResponseEntity<?> getPost(@PathVariable("id") Long id) {
 	
 		Optional<TacticsBoard> _tacticsBoard = tacticsBoardService.getTacticsBoard(id);
 		
 		if(_tacticsBoard.isEmpty()) {
 			return ResponseEntity.status(404).body("존재하지 않는 게시글입니다.");
 		}
 		
 		TacticsBoard tacticsBoard = _tacticsBoard.get();
 		tacticsBoard.setView(tacticsBoard.getView()+1);
 		
 		tacticsBoardService.save(tacticsBoard);
 		
 		return ResponseEntity.ok(tacticsBoard);
 	}
 	
 	// 삭제
 	@DeleteMapping("/{id}")
 	public ResponseEntity<?> deletePost(@PathVariable("id") Long id, Authentication auth) {
 		Optional<TacticsBoard> _tacticsBoard = tacticsBoardService.getTacticsBoard(id);
 		
 		if(_tacticsBoard.isEmpty()) {
 			return ResponseEntity.status(404).body("존재하지 않는 게시글입니다.");
 		}
 		
 		TacticsBoard tacticsBoard = _tacticsBoard.get();
 		
 		if (auth == null || !auth.getName().equals(tacticsBoard.getAuthor().getUserId())) {
 	        return ResponseEntity.status(403).body("삭제 권한이 없습니다.");
 	    }
 		
 		tacticsBoardService.deleteTacticsBoard(tacticsBoard);
 		
 		return ResponseEntity.ok(null);
 	}
 	
 	// 수정
 	@PutMapping("/{id}")
 	public ResponseEntity<?> updatePost(@PathVariable("id") Long id, @RequestBody TacticsBoard updateTacticsBoard, Authentication auth) {
 		
 		Optional<TacticsBoard> _tacticsBoard = tacticsBoardService.getTacticsBoard(id);
 		
 		if(_tacticsBoard.isEmpty()) {
 			return ResponseEntity.status(404).body("존재하지 않는 게시글입니다.");
 		}
 		
 		TacticsBoard tacticsBoard = _tacticsBoard.get();
 		
 		if (auth == null || !auth.getName().equals(tacticsBoard.getAuthor().getUserId())) {
 	        return ResponseEntity.status(403).body("삭제 권한이 없습니다.");
 	    }
 		
 		tacticsBoard.setTitle(updateTacticsBoard.getTitle());
 		tacticsBoard.setContent(updateTacticsBoard.getContent());
 		
 		tacticsBoardService.save(tacticsBoard);
 		
 		return ResponseEntity.ok(tacticsBoard);
 	}

 	// 추천
 	@PutMapping("/{id}/like")
 	public ResponseEntity<?> updateLikes(@PathVariable("id") Long id, Authentication auth) {
 		
 		if (auth == null) {
 	        return ResponseEntity.status(401).body("로그인이 필요합니다.");
 	    }
 		
 		SiteUser siteUser = siteUserService.getUser(auth.getName()).orElseThrow(() -> new RuntimeException("사용자 없음"));
 		
 		return tacticsBoardService.likeTacticsBoard(id, siteUser);

 	}
 	
 	// 비추천
  	@PutMapping("/{id}/dislike")
  	public ResponseEntity<?> updateDisLikes(@PathVariable("id") Long id, Authentication auth) {
  		
  		if (auth == null) {
  	        return ResponseEntity.status(401).body("로그인이 필요합니다.");
  	    }
  		
  		SiteUser siteUser = siteUserService.getUser(auth.getName()).orElseThrow(() -> new RuntimeException("사용자 없음"));
  		
  		return tacticsBoardService.dislikeTacticsBoard(id, siteUser);

  	}
}
