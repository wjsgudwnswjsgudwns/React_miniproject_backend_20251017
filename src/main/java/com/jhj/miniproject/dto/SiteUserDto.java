package com.jhj.miniproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteUserDto {
	
	@NotBlank(message = "아이디를 입력해주세요")
	private String userId;
	
	@NotBlank(message = "비밀번호를 입력해주세요")
	@Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
	private String password;
	
	@NotBlank(message = "비밀번호 확인을 입력해주세요")
	private String passwordCheck;
	
	@NotBlank(message = "이메일을 입력해주세요")
	private String email;
	
	@NotBlank(message = "핸드폰 번호를 입력해주세요")
	private String phone;
}