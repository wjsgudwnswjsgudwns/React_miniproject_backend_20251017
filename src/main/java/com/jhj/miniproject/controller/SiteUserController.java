package com.jhj.miniproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.miniproject.dto.SiteUserDto;
import com.jhj.miniproject.entity.SiteUser;
import com.jhj.miniproject.service.SiteUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class SiteUserController {
	
	@Autowired
	private SiteUserService siteUserService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	// 로그인 한 사람 정보
	@GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        // 인증되지 않은 경우 null 반환 (200 OK)
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.ok(Map.of("userId", (Object) null));
        }
        // 인증된 경우 사용자 정보 반환
        return ResponseEntity.ok(Map.of("userId", auth.getName()));
    }
	
	// 회원가입
	@PostMapping(value = "/signup")
	public ResponseEntity<?> signup (@Valid @RequestBody SiteUserDto siteUserDto, BindingResult bindingResult) {
		
		// Spring Validation 결과 처리                              
		if(bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
					err -> {
						errors.put(err.getField(), err.getDefaultMessage());
					}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		// 비밀번호, 비밀번호 확인 일치 확인
		if(!siteUserDto.getPassword().equals(siteUserDto.getPasswordCheck())) {
			Map<String, String> error = new HashMap<>();
			error.put("passwordNotSame", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
			return ResponseEntity.badRequest().body(error);
		}
		
		// 아이디 중복 확인
		if(siteUserService.isUserIdDuplicated(siteUserDto.getUserId())) {
			Map<String, String> error = new HashMap<>();
			error.put("userIdDuplicated", "이미 사용중인 아이디입니다.");
			return ResponseEntity.badRequest().body(error);
		}
		
		// 아이디 중복 확인
		if(siteUserService.isNicknameDuplicated(siteUserDto.getNickname())) {
			Map<String, String> error = new HashMap<>();
			error.put("nicknameDuplicated", "이미 사용중인 닉네임입니다.");
			return ResponseEntity.badRequest().body(error);
		}
		
		// 정보 저장
		SiteUser siteUser = new SiteUser();
		siteUser.setUserId(siteUserDto.getUserId());
		siteUser.setPassword(passwordEncoder.encode(siteUserDto.getPassword()));
		siteUser.setNickname(siteUserDto.getNickname());
		siteUser.setEmail(siteUserDto.getEmail());
		siteUser.setPhone(siteUserDto.getPhone());
		
		siteUserService.signup(siteUser);
		
		return ResponseEntity.ok("회원 가입에 성공하셨습니다.");
		
	}
}
