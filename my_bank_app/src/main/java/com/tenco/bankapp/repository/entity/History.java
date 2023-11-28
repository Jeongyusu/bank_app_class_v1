package com.tenco.bankapp.repository.entity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.tenco.bankapp.utils.BalanceUtil;
import com.tenco.bankapp.utils.TimeStampUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {
	private Integer id;
	private Long amount;
	private Long wBalance;
	private Long dBalance;
	private Integer wAccountId;
	private Integer dAccountId;
	private Timestamp createdAt;
	
	//거래내역 정보 추가
	private String sender;
	private String receiver;
	private Long balance;
	
	public String formatCreatedAt() {
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd a HH:mm");
//		return simpleDateFormat.format(this.createdAt);
		return TimeStampUtil.timeStampToString(this.createdAt);
	}
	
	public String formatBalance() {
		//data format 천단위에 쉼표 찍는 기능을 구현하시오
		// 1,000원
//		DecimalFormat df = new DecimalFormat("###,###");
//		return df.format(this.balance) + "원";
		return BalanceUtil.balanceToString(this.balance);
	}

}
