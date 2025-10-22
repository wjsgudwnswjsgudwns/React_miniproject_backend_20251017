package com.jhj.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.miniproject.entity.SiteUser;
import com.jhj.miniproject.entity.TacticsBoard;
import com.jhj.miniproject.entity.TacticsDisLike;

public interface TacticsDisLikeRepository extends JpaRepository<TacticsDisLike, Long> {

	public boolean existsByTacticsBoardAndUser(TacticsBoard tacticsBoard, SiteUser user);
}
