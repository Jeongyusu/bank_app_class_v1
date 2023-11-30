package com.tenco.bankapp.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bankapp.dto.SignInFormDto;
import com.tenco.bankapp.repository.entity.User;

@Mapper
public interface UserRepository {

	//사용자 등록
	public int insert(User user);
	//사용자 수정
	public int updateById(User user);
	//사용자 삭제
	public int deleteById(Integer id);
	//사용자 조회
	public User findById(Integer id);
	//사용자 전체 조회
	public List<User> findAll();
	//사용자에 이름과 비번으로 조회
	public User findByUsernameAndPassword(SignInFormDto dto);
	//사용자 이름만 조회
	public User findByUsername(String username);
	
}
