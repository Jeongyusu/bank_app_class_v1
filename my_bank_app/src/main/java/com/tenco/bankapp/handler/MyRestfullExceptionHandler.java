package com.tenco.bankapp.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tenco.bankapp.handler.exception.CustomRestfullException;

/*
 * 예외 발생시 데이터를 낼줄 수 있다.
 * 
 */
@RestControllerAdvice //IOC 대상 + AOP 기반
public class MyRestfullExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public void exception(Exception e) {
		System.out.println("-------------------------------");
		System.out.println("e.getClass().getName()");
		System.out.println("e.getMessage()");
		System.out.println("-------------------------------");
	}
	
	//사용자 정의 예외 클래스 활용
	@ExceptionHandler(CustomRestfullException.class)
	public String basicException(CustomRestfullException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert('"+ e.getMessage() +"');");
		sb.append("history.back();");
		sb.append("</script>");
		return sb.toString();
	}
}