package com.jhj.miniproject.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TacticsBoard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	
	private String content;
	
	@CreationTimestamp
	private LocalDateTime createDate; // 작성일
	
	@ManyToOne
	private SiteUser author; // 작성자
	
	private int view; // 조회수
	
	private int likes; // 추천
	
	private int dislikes; // 비추천
	 
	@OneToMany(mappedBy = "tacticsBoard", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Comment> comments = new ArrayList<>();
	
	// 댓글 개수
	public int getCommentCount() {
		return comments != null ? comments.size() : 0;
	}
}
