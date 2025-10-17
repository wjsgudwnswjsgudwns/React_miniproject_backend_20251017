package com.jhj.miniproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.miniproject.entity.SiteUser;


public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {

	public Optional<SiteUser> findByUserId(String userId);
}
