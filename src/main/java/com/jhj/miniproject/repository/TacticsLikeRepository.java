package com.jhj.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.miniproject.entity.SiteUser;
import com.jhj.miniproject.entity.TacticsBoard;
import com.jhj.miniproject.entity.TacticsLike;

public interface TacticsLikeRepository extends JpaRepository<TacticsLike, Long> {

	public boolean existsByTacticsBoardAndUser(TacticsBoard tacticsBoard, SiteUser user);

}
