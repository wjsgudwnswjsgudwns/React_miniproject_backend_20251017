package com.jhj.miniproject.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jhj.miniproject.entity.Platform;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Optional<Platform> findByCode(String code);
}
