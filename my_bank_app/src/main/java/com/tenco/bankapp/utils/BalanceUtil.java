package com.tenco.bankapp.utils;

import java.text.DecimalFormat;

public class BalanceUtil {
	public static String balanceToString(Long balance) {
		DecimalFormat df = new DecimalFormat("###,###");
		return df.format(balance) + "원";
	}
}
