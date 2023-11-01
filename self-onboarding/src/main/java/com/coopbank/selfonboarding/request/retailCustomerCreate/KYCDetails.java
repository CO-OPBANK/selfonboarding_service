package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class KYCDetails {
    private String KYCStatus;
    private String KYCDate;
    private String SubmittedForKYCIndicator;
    private String KYCRecertificationDate;
    private String MainSourceOfFunds;
    private String OtherSourceOfFunds;
    private String OtherBankName;
    private String PreferredCommunicationLanguage;
    private String CustomerRating;
}