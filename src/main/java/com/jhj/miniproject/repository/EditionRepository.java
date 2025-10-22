package com.jhj.miniproject.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jhj.miniproject.entity.Edition;

public interface EditionRepository extends JpaRepository<Edition, Long> {
    Optional<Edition> findByType(String type);
}
