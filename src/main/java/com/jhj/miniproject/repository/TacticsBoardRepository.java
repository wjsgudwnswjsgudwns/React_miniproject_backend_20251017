package com.jhj.miniproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jhj.miniproject.entity.TacticsBoard;

public interface TacticsBoardRepository extends JpaRepository<TacticsBoard, Long> {
	
	// 제목으로 검색 (대소문자 구분 없이)
    Page<TacticsBoard> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // 작성자 닉네임으로 검색
    @Query("SELECT b FROM Board b WHERE b.author.nickname LIKE %:nickname%")
    Page<TacticsBoard> findByAuthorNickname(@Param("nickname") String nickname, Pageable pageable);
    
    // 제목 + 내용 통합 검색 (선택사항)
    @Query("SELECT b FROM Board b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<TacticsBoard> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
}
