package com.tenco.bankapp.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;


@Getter
@Data
public class SignUpFormDto {

	private String username;
	private String password;
	private String fullname;
	private MultipartFile file; // name 속성과 일치시켜야함. 여러개 올릴 땐 배열처리 private MultipartFile[] file;
	private String originFileName;
	private String uploadFileName;
	
	
}
