package com.jhj.miniproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jhj.miniproject.entity.SiteUser;
import com.jhj.miniproject.repository.SiteUserRepository;


@Service
public class UserSecurityService implements UserDetailsService{

	@Autowired
	private SiteUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		SiteUser siteUser = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
		
		return org.springframework.security.core.userdetails.User.withUsername(siteUser.getUserId()).password(siteUser.getPassword()).authorities("User").build();
	}
	
}
