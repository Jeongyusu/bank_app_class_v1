package com.tenco.bankapp.dto;

import lombok.Data;
import lombok.Getter;


@Getter
@Data
public class SignUpFormDto {

	private String username;
	private String password;
	private String fullname;
	
}
