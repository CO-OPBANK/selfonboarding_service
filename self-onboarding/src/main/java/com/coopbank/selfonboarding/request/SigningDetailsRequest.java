package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class SigningDetailsRequest {
	public String accountNumber;
	public String bankCode;
	public String customerName;
	public String signature;
	public String photo;
	public String signEffectiveDate;
	public String remarks;
}
