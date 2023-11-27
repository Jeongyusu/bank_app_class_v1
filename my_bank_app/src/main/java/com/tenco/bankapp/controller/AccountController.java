package com.tenco.bankapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysql.cj.Session;
import com.tenco.bankapp.dto.DepositFormDto;
import com.tenco.bankapp.dto.SaveFormDto;
import com.tenco.bankapp.dto.TransferFormDto;
import com.tenco.bankapp.dto.WithdrawFormDto;
import com.tenco.bankapp.handler.exception.CustomPageException;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.handler.exception.UnAuthorizedException;
import com.tenco.bankapp.repository.entity.Account;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.service.AccountService;
import com.tenco.bankapp.utils.Define;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private AccountService accountService;

	@GetMapping({"/list", "/"})
	public String list(Model model) {
		//인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다", HttpStatus.UNAUTHORIZED);
		}
		List<Account> accountList = accountService.readAccountList(principal.getId());
		
		if(accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		} else {
			model.addAttribute("accountList", accountList);
		}
		
		return "account/list";
	}
	
	@GetMapping("/save")
	public String save() {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		return "account/save";
	}
	
	@PostMapping("/save")
	public String saveProc(SaveFormDto dto) {
		//1. 인증검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		//2. 유효성 검사
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("계좌비밀번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getBalance() == null || dto.getBalance() <= 0) {
			throw new CustomRestfullException("잘못된 입력입니다", HttpStatus.BAD_REQUEST);
		}
		accountService.createdAccount(dto, principal.getId());
		return "account/list";
	}
	
	//출금 페이지 요청
	@GetMapping("/withdraw")
	public String withdraw() {
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		// 2.
		return "account/withdraw";
	}
	
	@GetMapping("/deposit")
	public String deposit() {
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		// 2.
		return "account/deposit";
	}
	
	@GetMapping("/transfer")
	public String transfer() {
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		// 2.
		return "account/transfer";
	}
	
	@PostMapping("/withdraw")
	public String withdrawProc(WithdrawFormDto dto) {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount().longValue() <=0) {
			throw new CustomRestfullException("출금 금액이 0원 이하 일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력하시오.", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("비밀번호를 입력하시오.", HttpStatus.BAD_REQUEST);
		}
		//서비스 호출
		accountService.updateAccountWithdraw(dto, principal.getId());
		return "redirect:/account/list";
	}
	
	@PostMapping("/deposit")
	public String depositProc(DepositFormDto dto) {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount().longValue() <=0) {
			throw new CustomRestfullException("입금 금액이 0원 이하 일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if(dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력하시오.", HttpStatus.BAD_REQUEST);
		}
		//서비스 호출
		accountService.updateAccountDeposit(dto);
		return "redirect:/account/list";
	}
	
	@PostMapping("/transfer")
	public String transferProc(TransferFormDto dto) {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount().longValue() <=0) {
			throw new CustomRestfullException("이체 금액이 0원 이하 일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if(dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("상대방 계좌번호를 입력하시오.", HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("본인의 계좌번호를 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		//서비스 호출
		accountService.updateAccountTransfer(dto, principal.getId());
		return "redirect:/account/list";
	}
}


