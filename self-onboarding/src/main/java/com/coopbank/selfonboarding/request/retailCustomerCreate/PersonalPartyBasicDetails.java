package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class PersonalPartyBasicDetails {
    private String customerType;
    private String nationalIdentifier;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String preferredName;
    private String nickName;
    private String shortName;
    private String title;
    private String gender;
    private String region;
    private String dateOfBirth;
    private String civilStatus;
    private String seniorCitizen;
    private String residentialStatus;
    private String creditScore;
    private String defaultChannel;
    private String marketingConsentObtained;
    private String ebankingEnabled;
    private String contactPersonId;
    private String nativeLanguageCode;
    private String taxPayerOrIDNumber;
    private String countryOfCitizenship;
    private String countryOfResidence;
    private String placeOfBirth;
    private String nationality;
    private String highRiskCountry;
    private String motherMaidenName;
    private String physicalState;
    private String permanentDisabled;
    // Add fields for other elements...
}
