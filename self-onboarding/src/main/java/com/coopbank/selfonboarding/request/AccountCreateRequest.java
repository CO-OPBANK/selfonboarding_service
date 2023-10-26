package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class AccountCreateRequest {
	public String SchemeCode;
	public String Product;
	public String BranchSortCode;
	public String Currency;
	public String CustomerCode;
	public String SectorCode;
	public String SubsectorCode;
	public String PurposeOfAccount;
	public String WHTTaxIndicator;
	public String AROCode;
	public String DSOCode;
	public String StatementMode;
	public String StatementFrequency;
	public String StatementMedium;
	public String StartDate;
	public String WeekDay;
	public String HolidayStatus;
	public String BusinessEconomicCode;
	public String SourceOfFunds;
	public String ProductSegment;
}
