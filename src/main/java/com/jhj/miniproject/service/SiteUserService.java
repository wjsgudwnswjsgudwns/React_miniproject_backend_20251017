package com.jhj.miniproject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.miniproject.entity.SiteUser;
import com.jhj.miniproject.repository.SiteUserRepository;



@Service
public class SiteUserService {
	
	@Autowired
	private SiteUserRepository siteUserRepository;
	
	public boolean isUserIdDuplicated (String userId) {
        Optional<SiteUser> existingUser = siteUserRepository.findByUserId(userId);
        return existingUser.isPresent(); // 존재하면 true (중복), 없으면 false
	}

	public SiteUser signup(SiteUser siteUser) {
		
		return siteUserRepository.save(siteUser);
	}
}
