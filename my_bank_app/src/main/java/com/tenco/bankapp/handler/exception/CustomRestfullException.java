package com.tenco.bankapp.handler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomRestfullException extends RuntimeException{
	
	private HttpStatus status;
	
	public CustomRestfullException(String message, HttpStatus httpStatus) {
		super(message);
		this.status = httpStatus;
		
	}

}
