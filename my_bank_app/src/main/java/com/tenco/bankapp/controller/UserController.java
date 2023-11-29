package com.tenco.bankapp.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.tenco.bankapp.dto.SignInFormDto;
import com.tenco.bankapp.dto.SignUpFormDto;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.service.UserService;
import com.tenco.bankapp.utils.Define;

import lombok.extern.slf4j.Slf4j;
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
   
   @Autowired // DI처리
   private UserService userService;
   @Autowired
   private HttpSession session;
   
   // DI처리
//   public UserController(UserService userService) {
//      this.userService = userService;
//   }

   // 회원가입 페이지 요청
   // http://localhost:80/user/sign-up

   @GetMapping("/sign-up")
   public String signUp() {
      return "user/signUp";
   }

   // 로그인 페이지 요청
   // http://localhost:80/user/sign-in

   @GetMapping("/sign-in")
   public String signIn() {
      return "user/signIn";
   }

   /**
    * 
    * @param dto
    * @return 리다이렉트 로그인 페이지
    */
   // DTO - Object Mapper
   @PostMapping("/sign-up")
   public String signUpProc(SignUpFormDto dto) {
   
      // 1. 유효성 검사
      if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
         throw new CustomRestfullException("username을 입력하세요", 
               HttpStatus.BAD_REQUEST);
      }
      if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
         throw new CustomRestfullException("Password을 입력하세요", 
               HttpStatus.BAD_REQUEST);
      }
      if (dto.getFullname() == null || dto.getFullname().isEmpty()) {
         throw new CustomRestfullException("Fullname을 입력하세요", 
               HttpStatus.BAD_REQUEST);
      }
      //사용자 프로필 이미지 등록처리
      MultipartFile file = dto.getFile();
      if(file.isEmpty() == false) {
    	  //파일 사이즈 체크
    	  if(file.getSize() > Define.MAX_FILE_SIZE) {
    		  throw new CustomRestfullException("파일 크기는 20MB 이상 클 수 없어요", HttpStatus.BAD_REQUEST);
    	  }
    	  
    	  try {
    		  String saveDirectory = Define.UPLOAD_DIRECTORY;
    		  // 폴더가 없다면 오류 발생
    		  File dir = new File(saveDirectory);
    		  if(dir.exists() == false) {
    			  dir.mkdir(); //폴더가 없다면 생성
    		  }
    		  // 파일 이름(중복 처리 예방)
    		  UUID uuid = UUID.randomUUID();
    		  String fileName = uuid + "_" + file.getOriginalFilename();
    		  // 전체 경로 지정
    		  String uploadPath = Define.UPLOAD_DIRECTORY + File.separator + fileName;
    		  System.out.println("uploadPath" + uploadPath);
    		  File destination = new File(uploadPath);
    		  
    		  //반드시 사용
    		  file.transferTo(destination); //실제 생성
    		  
    		  //객체 상태 변경(insert처리하기 위함)
    		  dto.setOriginFileName(file.getOriginalFilename()); // 사용자가 기존에 입력한 파일명
    		  dto.setUploadFileName(fileName); 
    		  log.info("나요기요" + dto.getUploadFileName(), dto.getOriginFileName());
    	  } catch(Exception e) {
    		  
    	  }
      
      }
      
      userService.signUp(dto);
      return "redirect:/user/sign-in";
   }
   
   @PostMapping("/sign-in")
   public String signInProc(SignInFormDto dto) {
	   // 1. 유효성 검사
	   if(dto.getUsername() == null || dto.getUsername().isEmpty()){
		   throw new CustomRestfullException("username을 입력하세요", HttpStatus.BAD_REQUEST);
	   }
	   if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
	         throw new CustomRestfullException("Password을 입력하세요", HttpStatus.BAD_REQUEST);
	      }
	   //서비스 호출
	   User principal =userService.signIn(dto);
	   session.setAttribute(Define.PRINCIPAL, principal); //세션에 사용자 정보 저장
	   return "redirect:/account/list";
	   }
   
   @GetMapping("/logout")
   public String logout() {
	   session.invalidate();
	   return "redirect:/user/sign-in";
   }
 }