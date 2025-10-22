package com.jhj.miniproject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhj.miniproject.entity.Board;
import com.jhj.miniproject.entity.BoardLike;
import com.jhj.miniproject.entity.SiteUser;
import com.jhj.miniproject.entity.TacticsBoard;
import com.jhj.miniproject.entity.TacticsDisLike;
import com.jhj.miniproject.entity.TacticsLike;
import com.jhj.miniproject.repository.TacticsBoardRepository;
import com.jhj.miniproject.repository.TacticsDisLikeRepository;
import com.jhj.miniproject.repository.TacticsLikeRepository;

@Service
public class TacticsBoardService {

	@Autowired
	private TacticsBoardRepository tacticsBoardRepository;
	
	@Autowired
	private TacticsLikeRepository tacticsLikeRepository;
	
	@Autowired
	private TacticsDisLikeRepository tacticsDisLikeRepository;
	
	public Page<TacticsBoard> getTacticsBoardWithPage(int page, int size) {
        if(page < 0) {
            page = 0;
        }

        if(size <= 0) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return tacticsBoardRepository.findAll(pageable);
    }
	
	// 게시판
	public Page<TacticsBoard> searchTacticsBoard(String searchType, String keyword, int page, int size) {
		
		if(page < 0) {
            page = 0;
        }
        if(size <= 0) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 검색어가 비어있으면 전체 조회
        if(keyword == null || keyword.trim().isEmpty()) {
            return tacticsBoardRepository.findAll(pageable);
        }
        
        if("title".equals(searchType)) {
            return tacticsBoardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        } else if("nickname".equals(searchType)) {
            return tacticsBoardRepository.findByAuthorNickname(keyword, pageable);
        } else {
            // 기본값: 전체 조회
            return tacticsBoardRepository.findAll(pageable);
        }
	}
	
	// 추천
	public ResponseEntity<?> likeTacticsBoard(Long tacticsBoardId, SiteUser user) {
		Optional<TacticsBoard> _tacticsBoard = tacticsBoardRepository.findById(tacticsBoardId);
		
		if(_tacticsBoard.isEmpty()) {
			return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
		}
		
		TacticsBoard tacticsBoard = _tacticsBoard.get();
		
		boolean alreadylike = tacticsLikeRepository.existsByTacticsBoardAndUser(tacticsBoard, user);
		
		if(alreadylike) {
			return ResponseEntity.status(400).body("이미 추천한 게시물입니다.");
		}
		
		TacticsLike tacticsLike = new TacticsLike();
		tacticsLike.setTacticsBoard(tacticsBoard);
		tacticsLike.setUser(user);
		
		tacticsLikeRepository.save(tacticsLike);
		
		tacticsBoard.setLikes(tacticsBoard.getLikes()+1);
		tacticsBoardRepository.save(tacticsBoard);
		
		return ResponseEntity.ok(tacticsBoard);
	}
	
	// 비추천
	public ResponseEntity<?> dislikeTacticsBoard(Long tacticsBoardId, SiteUser user) {
		Optional<TacticsBoard> _tacticsBoard = tacticsBoardRepository.findById(tacticsBoardId);
		
		if(_tacticsBoard.isEmpty()) {
			return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
		}
		
		TacticsBoard tacticsBoard = _tacticsBoard.get();
		
		boolean alreadylike = tacticsDisLikeRepository.existsByTacticsBoardAndUser(tacticsBoard, user);
		
		if(alreadylike) {
			return ResponseEntity.status(400).body("이미 추천한 게시물입니다.");
		}
		
		TacticsDisLike tacticsDisLike = new TacticsDisLike();
		tacticsDisLike.setTacticsBoard(tacticsBoard);
		tacticsDisLike.setUser(user);
		
		tacticsDisLikeRepository.save(tacticsDisLike);
		
		tacticsBoard.setDislikes(tacticsBoard.getDislikes()+1);
		tacticsBoardRepository.save(tacticsBoard);
		
		return ResponseEntity.ok(tacticsBoard);
	}
	
	// 저장
	public TacticsBoard save(TacticsBoard tacticsBoard) {
		return tacticsBoardRepository.save(tacticsBoard);
	}
	
	// 특정 글 호출
	public Optional<TacticsBoard> getTacticsBoard(Long id) {
		Optional<TacticsBoard> _tacticsBoard = tacticsBoardRepository.findById(id);
		return _tacticsBoard;
	}
	
	// 특정 글 삭제
	public void deleteTacticsBoard(TacticsBoard tacticsBoard) {
		tacticsBoardRepository.delete(tacticsBoard);
	}
}
