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
import com.jhj.miniproject.repository.BoardLikeRepository;
import com.jhj.miniproject.repository.BoardRepository;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private BoardLikeRepository boardLikeRepository;
	
	// 전체 게시글 조회 (페이징)
    public Page<Board> getBoardWithPage(int page, int size) {
        if(page < 0) {
            page = 0;
        }

        if(size <= 0) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return boardRepository.findAll(pageable);
    }

    // 게시글 검색 (제목, 닉네임)
    public Page<Board> searchBoards(String searchType, String keyword, int page, int size) {
        if(page < 0) {
            page = 0;
        }
        if(size <= 0) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 검색어가 비어있으면 전체 조회
        if(keyword == null || keyword.trim().isEmpty()) {
            return boardRepository.findAll(pageable);
        }

        // 검색 타입에 따라 다른 메서드 호출
        if("title".equals(searchType)) {
            return boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        } else if("nickname".equals(searchType)) {
            return boardRepository.findByAuthorNickname(keyword, pageable);
        } else {
            // 기본값: 전체 조회
            return boardRepository.findAll(pageable);
        }
    }
	
	
	
	public Board save(Board board) {
		return boardRepository.save(board);
	}
	
	// 특정 글 호출
	public Optional<Board> getBoard(Long id) {
		Optional<Board> _board = boardRepository.findById(id);
		return _board;
	}
	
	// 특정 글 삭제
	public void deleteBoard(Board board) {
		boardRepository.delete(board);
	}
	
	
	// 추천
	public ResponseEntity<?> likeBoard(Long boardId, SiteUser user) {
		Optional<Board> _board = boardRepository.findById(boardId);
		
		if(_board.isEmpty()) {
			return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
		}
		
		Board board = _board.get();
		
		boolean alreadylike = boardLikeRepository.existsByBoardAndUser(board, user);
		
		if(alreadylike) {
			return ResponseEntity.status(400).body("이미 추천한 게시물입니다.");
		}
		
		BoardLike boardLike = new BoardLike();
		boardLike.setBoard(board);
		boardLike.setUser(user);
		
		boardLikeRepository.save(boardLike);
		
		board.setLikes(board.getLikes()+1);
		boardRepository.save(board);
		
		return ResponseEntity.ok(board);
	}
}
