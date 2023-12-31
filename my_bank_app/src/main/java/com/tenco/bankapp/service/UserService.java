package com.tenco.bankapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bankapp.dto.SignInFormDto;
import com.tenco.bankapp.dto.SignUpFormDto;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.repository.interfaces.UserRepository;

@Service
public class UserService {
	
	@Autowired // 의존주입(생성자, 메서드)
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public int signUp(SignUpFormDto dto) {
		String rawPwd = dto.getPassword();
		
		//User, //SignUpFormDto
		User user = User.builder()
					.username(dto.getUsername())
					.password(passwordEncoder.encode(rawPwd))
					.fullname(dto.getFullname())
					.originFileName(dto.getOriginFileName())
					.uploadFileName(dto.getUploadFileName())
					.build(); // build() 반드시 호출
		int resultRowCount = userRepository.insert(user);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("회원 가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resultRowCount;
	}

	public User signIn(SignInFormDto dto) {
		
		// 1. username 아이디 존재 여부 확인
		User userEntity = userRepository.findByUsername(dto.getUsername());
		if(userEntity == null) {
			throw new CustomRestfullException("존재하지 않는 계정입니다.", HttpStatus.BAD_REQUEST);
		}
		boolean isPwdMatched = passwordEncoder.matches(dto.getPassword(), userEntity.getPassword());
		if(isPwdMatched == false) {
			throw new CustomRestfullException("비밀번호가 잘못 되었습니다.", HttpStatus.BAD_REQUEST);
		}
		return userEntity;
	}

	public User searchUsername(String username) {
		
		return userRepository.findByUsername(username);
	}

}
