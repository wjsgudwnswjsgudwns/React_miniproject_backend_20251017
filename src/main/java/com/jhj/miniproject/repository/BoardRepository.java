package com.jhj.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.miniproject.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
