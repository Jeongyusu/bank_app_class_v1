package com.tenco.bankapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // IOC의 대상
@RequestMapping("/temp")
public class TestController {

	// 주소설계
	// http://localhost:80/temp-test

	@GetMapping("/temp-test")
	public String tempTest() {
		return "temp";
	}
	
	@GetMapping("/main-page")
	public String tempMainPage() {
		return "main";
	}
}
