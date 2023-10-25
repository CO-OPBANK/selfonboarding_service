package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class AccountDetailsRequest {
	
	public String AccountNumber;
    public String AccountSchemeType;
    public String AccountOpeningDate;
    public String AccountDocIndex;

}
