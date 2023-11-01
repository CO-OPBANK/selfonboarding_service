package com.coopbank.selfonboarding.soa.services.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import com.coopbank.selfonboarding.request.RetailCustomerCreate;
import com.coopbank.selfonboarding.request.retailCustomerCreate.AddressDetail;
import com.coopbank.selfonboarding.request.retailCustomerCreate.AddressDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.ContactDetail;
import com.coopbank.selfonboarding.request.retailCustomerCreate.ContactDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.DemographicInfo;
import com.coopbank.selfonboarding.request.retailCustomerCreate.DocumentDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.EmploymentDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.InstituteDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.KYCDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.PersonalPartyBasicDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.RelatedBankDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.RelationshipDetail;
import com.coopbank.selfonboarding.request.retailCustomerCreate.RelationshipDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.TaxDetails;
import com.coopbank.selfonboarding.util.CommonMethods;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateRetailCustomer {
	
	
    public  static SOAPMessage postCreateRetailCustomer(SOAPMessage soapRequest,String retailCustomerCreateEndpoint,String soaPassword) throws MalformedURLException,
    IOException {
SOAPMessage soapResponse = null;
try {
    System.setProperty("java.protocol.handler.pkgs", "sun.net.www.protocol");
    System.setProperty("javax.net.ssl.trustStore", "KeyStore.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", soaPassword);
    System.setProperty("javax.net.ssl.keyStore", "KeyStore.jks");
    System.setProperty("javax.net.ssl.keyStorePassword", soaPassword);
    System.setProperty("javax.net.ssl.keyStoreType", "JKS");

    SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
    SOAPConnection soapConnection = soapConnectionFactory.createConnection();
    CommonMethods.doTrustToCertificates();
    String url =  retailCustomerCreateEndpoint;
    //System.out.println("\n--------------------------------- SOAP Response & URL ---------------------------------");
    log.info("request is == " + soapRequest +" url == "+url);
    //System.out.println("\n--------------------------------- SOAP Response & URL ---------------------------------");
    
   // System.out.println("\n--------------------------------- SOAP Response ---------------------------------");
    soapResponse = soapConnection.call(soapRequest, url);
    CommonMethods.createSoapResponse(soapResponse);
    //System.out.println("\n--------------------------------- SOAP Response ---------------------------------");
    soapConnection.close();
} catch (Exception e) {
    e.printStackTrace();
}
return soapResponse;

}
    
    public static SOAPMessage createRetailCustomerSOAPRequest(RetailCustomerCreate retailCustomerCreate, String userId, String retailCustomerCreateEndpoint, String soaUsername, String soaPassword, String soaSystemCode) {
        SOAPMessage SOAPMessageResponse = null;

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String strDate = formatter.format(date);

        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage SOAPMessage = messageFactory.createMessage();
            SOAPPart soapPart = SOAPMessage.getSOAPPart();
            String reference = UUID.randomUUID().toString();

            String soapenv = "soapenv";
            String mes = "mes";
            String com = "com";
            String post = "post";

            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();

            envelope.addNamespaceDeclaration(soapenv, myNamespaceURI);
            envelope.addNamespaceDeclaration(mes, "urn://co-opbank.co.ke/CommonServices/Data/Message/MessageHeader");
            envelope.addNamespaceDeclaration(com, "urn://co-opbank.co.ke/CommonServices/Data/Common");
            envelope.addNamespaceDeclaration(post, "urn://co-opbank.co.ke/BS/Customer/RetailCustomerCreate/Post.1.0");

            SOAPHeader header = envelope.getHeader();
            SOAPElement requestHeader = header.addChildElement("RequestHeader", mes);
            SOAPElement creationTimestamp = requestHeader.addChildElement("CreationTimestamp", com);
            SOAPElement correlationID = requestHeader.addChildElement("CorrelationID", com);
            SOAPElement messageID = requestHeader.addChildElement("MessageID", com);
            SOAPElement credentials = requestHeader.addChildElement("Credentials", com);
            SOAPElement systemCode = credentials.addChildElement("SystemCode", com);

            creationTimestamp.addTextNode(strDate);
            correlationID.addTextNode(reference);
            messageID.addTextNode(reference);
            systemCode.addTextNode(soaSystemCode);

            SOAPBody soapBody = envelope.getBody();
         // ...
            SOAPElement retailCustomerCreateRq = soapBody.addChildElement("RetailCustomerCreateRq", post);

            PersonalPartyBasicDetails personalPartyBasicDetails = retailCustomerCreate.getPersonalPartyBasicDetails();
            SOAPElement personalPartyBasicDetailsElem = retailCustomerCreateRq.addChildElement("PersonalPartyBasicDetails", post);

            // Create and add child elements for PersonalPartyBasicDetails
            personalPartyBasicDetailsElem.addChildElement("CustomerType", post).addTextNode(personalPartyBasicDetails.getCustomerType());
            personalPartyBasicDetailsElem.addChildElement("NationalIdentifier", post).addTextNode(personalPartyBasicDetails.getNationalIdentifier());
            personalPartyBasicDetailsElem.addChildElement("FirstName", post).addTextNode(personalPartyBasicDetails.getFirstName());
            personalPartyBasicDetailsElem.addChildElement("MiddleName", post).addTextNode(personalPartyBasicDetails.getMiddleName());
            personalPartyBasicDetailsElem.addChildElement("LastName", post).addTextNode(personalPartyBasicDetails.getLastName());
            personalPartyBasicDetailsElem.addChildElement("FullName", post).addTextNode(personalPartyBasicDetails.getFullName());
            personalPartyBasicDetailsElem.addChildElement("PreferredName", post).addTextNode(personalPartyBasicDetails.getPreferredName());
            personalPartyBasicDetailsElem.addChildElement("NickName", post).addTextNode(personalPartyBasicDetails.getNickName());
            personalPartyBasicDetailsElem.addChildElement("ShortName", post).addTextNode(personalPartyBasicDetails.getShortName());
            personalPartyBasicDetailsElem.addChildElement("Title", post).addTextNode(personalPartyBasicDetails.getTitle());
            personalPartyBasicDetailsElem.addChildElement("Gender", post).addTextNode(personalPartyBasicDetails.getGender());
            personalPartyBasicDetailsElem.addChildElement("Region", post).addTextNode(personalPartyBasicDetails.getRegion());
            personalPartyBasicDetailsElem.addChildElement("DateOfBirth", post).addTextNode(personalPartyBasicDetails.getDateOfBirth());
            personalPartyBasicDetailsElem.addChildElement("CivilStatus", post).addTextNode(personalPartyBasicDetails.getCivilStatus());
            personalPartyBasicDetailsElem.addChildElement("SeniorCitizen", post).addTextNode(personalPartyBasicDetails.getSeniorCitizen());
            personalPartyBasicDetailsElem.addChildElement("ResidentialStatus", post).addTextNode(personalPartyBasicDetails.getResidentialStatus());
            personalPartyBasicDetailsElem.addChildElement("CreditScore", post).addTextNode(personalPartyBasicDetails.getCreditScore());
            personalPartyBasicDetailsElem.addChildElement("DefaultChannel", post).addTextNode(personalPartyBasicDetails.getDefaultChannel());
            personalPartyBasicDetailsElem.addChildElement("MarketingConsentObtained", post).addTextNode(personalPartyBasicDetails.getMarketingConsentObtained());
            personalPartyBasicDetailsElem.addChildElement("EbankingEnabled", post).addTextNode(personalPartyBasicDetails.getEbankingEnabled());
            personalPartyBasicDetailsElem.addChildElement("ContactPersonId", post).addTextNode(personalPartyBasicDetails.getContactPersonId());
            personalPartyBasicDetailsElem.addChildElement("NativeLanguageCode", post).addTextNode(personalPartyBasicDetails.getNativeLanguageCode());
            personalPartyBasicDetailsElem.addChildElement("TaxPayerOrIDNumber", post).addTextNode(personalPartyBasicDetails.getTaxPayerOrIDNumber());
            personalPartyBasicDetailsElem.addChildElement("CountryOfCitizenship", post).addTextNode(personalPartyBasicDetails.getCountryOfCitizenship());
            personalPartyBasicDetailsElem.addChildElement("CountryOfResidence", post).addTextNode(personalPartyBasicDetails.getCountryOfResidence());
            personalPartyBasicDetailsElem.addChildElement("PlaceOfBirth", post).addTextNode(personalPartyBasicDetails.getPlaceOfBirth());
            personalPartyBasicDetailsElem.addChildElement("Nationality", post).addTextNode(personalPartyBasicDetails.getNationality());
            personalPartyBasicDetailsElem.addChildElement("HighRiskCountry", post).addTextNode(personalPartyBasicDetails.getHighRiskCountry());
            personalPartyBasicDetailsElem.addChildElement("MotherMaidenName", post).addTextNode(personalPartyBasicDetails.getMotherMaidenName());
            personalPartyBasicDetailsElem.addChildElement("PhysicalState", post).addTextNode(personalPartyBasicDetails.getPhysicalState());
            personalPartyBasicDetailsElem.addChildElement("PermanentDisabled", post).addTextNode(personalPartyBasicDetails.getPermanentDisabled());


            // ...
            AddressDetails addressDetails = retailCustomerCreate.getAddressDetails();

            // Create AddressDetails element
            SOAPElement addressDetailsElem = retailCustomerCreateRq.addChildElement("AddressDetails", post);

            // Add elements for AddressDetails
            addressDetailsElem.addChildElement("PrefferedAddressType", post).addTextNode(addressDetails.getPrefferedAddressType());

            List<AddressDetail> addressDetailList = addressDetails.getAddressDetail();

            for (AddressDetail addressDetail : addressDetailList) {
                // Create AddressDetail element
                SOAPElement addressDetailElem = addressDetailsElem.addChildElement("AddressDetail", post);

                addressDetailElem.addChildElement("AddressType", post).addTextNode(addressDetail.getAddressType());
                addressDetailElem.addChildElement("AddressLine1", post).addTextNode(addressDetail.getAddressLine1());
                addressDetailElem.addChildElement("AddressLine2", post).addTextNode(addressDetail.getAddressLine2());
                addressDetailElem.addChildElement("AddressLine3", post).addTextNode(addressDetail.getAddressLine3());
                addressDetailElem.addChildElement("County", post).addTextNode(addressDetail.getCounty());
                addressDetailElem.addChildElement("TownOrCity", post).addTextNode(addressDetail.getTownOrCity());
                addressDetailElem.addChildElement("CountryCode", post).addTextNode(addressDetail.getCountryCode());
                addressDetailElem.addChildElement("PostalCode", post).addTextNode(addressDetail.getPostalCode());
                addressDetailElem.addChildElement("StartDate", post).addTextNode(addressDetail.getStartDate());
                addressDetailElem.addChildElement("EndDate", post).addTextNode(addressDetail.getEndDate());
                addressDetailElem.addChildElement("AddressProofIndicator", post).addTextNode(addressDetail.getAddressProofIndicator());
                addressDetailElem.addChildElement("AddressVerifiedInd", post).addTextNode(addressDetail.getAddressVerifiedInd());
            
            }


         // ...
            ContactDetails contactDetails = retailCustomerCreate.getContactDetails();

            // Create ContactDetails element
            SOAPElement contactDetailsElem = retailCustomerCreateRq.addChildElement("ContactDetails", post);

            // Add elements for ContactDetails
            contactDetailsElem.addChildElement("PreferredEmail", post).addTextNode(contactDetails.getPreferredEmail());
            contactDetailsElem.addChildElement("PreferredPhone", post).addTextNode(contactDetails.getPreferredPhone());

            List<ContactDetail> contactDetailList = contactDetails.getContactDetail();

            for (ContactDetail contactDetail : contactDetailList) {
                // Create ContactDetail element
                SOAPElement contactDetailElem = contactDetailsElem.addChildElement("ContactDetail", post);

                // Add elements for ContactDetail
                contactDetailElem.addChildElement("ContactMethod", post).addTextNode(contactDetail.getContactMethod());
                contactDetailElem.addChildElement("PhoneNumberCountryCode", post).addTextNode(contactDetail.getPhoneNumberCountryCode());
                contactDetailElem.addChildElement("ContactType", post).addTextNode(contactDetail.getContactType());
                contactDetailElem.addChildElement("EmailID", post).addTextNode(contactDetail.getEmailID());
                contactDetailElem.addChildElement("PhoneNo", post).addTextNode(contactDetail.getPhoneNumberCountryCode());
              
            }
            // ...

         // ...
            RelationshipDetails relationshipDetails = retailCustomerCreate.getRelationshipDetails();

            // Create RelationshipDetails element
            SOAPElement relationshipDetailsElem = retailCustomerCreateRq.addChildElement("RelationshipDetails", post);

            RelationshipDetail relationshipDetail = relationshipDetails.getRelationshipDetail();

            // Create RelationshipDetail element
            SOAPElement relationshipDetailElem = relationshipDetailsElem.addChildElement("RelationshipDetail");

            // Add elements for RelationshipDetail
            relationshipDetailElem.addChildElement("RelatedEntityType", post).addTextNode(relationshipDetail.getRelatedEntityType());
            relationshipDetailElem.addChildElement("RelatedEntity", post).addTextNode(relationshipDetail.getRelatedEntity());
            relationshipDetailElem.addChildElement("RelatedIternalPartyID", post).addTextNode(relationshipDetail.getRelatedIternalPartyID());
            relationshipDetailElem.addChildElement("RelationshipType", post).addTextNode(relationshipDetail.getRelationshipType());
            relationshipDetailElem.addChildElement("RelationshipCategory", post).addTextNode(relationshipDetail.getRelationshipCategory());
            relationshipDetailElem.addChildElement("PercentageOfShareHolding", post).addTextNode(relationshipDetail.getPercentageOfShareHolding());
            relationshipDetailElem.addChildElement("GuardCode", post).addTextNode(relationshipDetail.getGuardCode());
            relationshipDetailElem.addChildElement("ShareHolderType", post).addTextNode(relationshipDetail.getShareHolderType());
          
            // ...

            // ...
            RelatedBankDetails relatedBankDetails = retailCustomerCreate.getRelatedBankDetails();

            // Create RelatedBankDetails element
            SOAPElement relatedBankDetailsElem = retailCustomerCreateRq.addChildElement("RelatedBankDetails", post);

            // Add elements for RelatedBankDetails (include optional elements as needed)
            relatedBankDetailsElem.addChildElement("RelationshipType", post).addTextNode(relatedBankDetails.getRelationshipType());
            relatedBankDetailsElem.addChildElement("BankID", post).addTextNode(relatedBankDetails.getBankID());
            relatedBankDetailsElem.addChildElement("BranchID", post).addTextNode(relatedBankDetails.getBranchID());
            relatedBankDetailsElem.addChildElement("ProductType", post).addTextNode(relatedBankDetails.getProductType());
            relatedBankDetailsElem.addChildElement("AccountNumber", post).addTextNode(relatedBankDetails.getAccountNumber());
            relatedBankDetailsElem.addChildElement("RelationshipStartdate", post).addTextNode(relatedBankDetails.getRelationshipStartdate());
            relatedBankDetailsElem.addChildElement("ReasonForContinuing", post).addTextNode(relatedBankDetails.getReasonForContinuing());
          
            // ...

         // ...
            InstituteDetails instituteDetails = retailCustomerCreate.getInstituteDetails();

            // Create InstituteDetails element
            SOAPElement instituteDetailsElem = retailCustomerCreateRq.addChildElement("InstituteDetails", post);

            // Add elements for InstituteDetails (include all elements)
            instituteDetailsElem.addChildElement("InstituteUniversity", post).addTextNode(instituteDetails.getInstituteUniversity());
            instituteDetailsElem.addChildElement("Qualification", post).addTextNode(instituteDetails.getQualification());
            instituteDetailsElem.addChildElement("RegistrationNo", post).addTextNode(instituteDetails.getRegistrationNo());
            instituteDetailsElem.addChildElement("EnrolmentStatus", post).addTextNode(instituteDetails.getEnrolmentStatus());
            instituteDetailsElem.addChildElement("CourseStartDate", post).addTextNode(instituteDetails.getCourseStartDate());
            instituteDetailsElem.addChildElement("CourseEndDate", post).addTextNode(instituteDetails.getCourseEndDate());
            instituteDetailsElem.addChildElement("CertificationDate", post).addTextNode(instituteDetails.getCertificationDate());
        
            // ...

            DemographicInfo demographicInfo = retailCustomerCreate.getDemographicInfo();

	         // Create DemographicInfo element
	         SOAPElement demographicInfoElem = retailCustomerCreateRq.addChildElement("DemographicInfo", post);
	
	         // Create EduDtlsInfo element as child
	         SOAPElement eduDtlsInfoElem = demographicInfoElem.addChildElement("EduDtlsInfo" , post);
	
	         // Add elements for EduDtlsInfo (include all elements)
	         eduDtlsInfoElem.addChildElement("EnrollmentStatusStartDate" , post, post).addTextNode(demographicInfo.getEduDtlsInfo().getEnrollmentStatusStartDate());
	         eduDtlsInfoElem.addChildElement("SeparationDate", post).addTextNode(demographicInfo.getEduDtlsInfo().getSeparationDate());
	         eduDtlsInfoElem.addChildElement("Qualification", post).addTextNode(demographicInfo.getEduDtlsInfo().getQualification());
	         eduDtlsInfoElem.addChildElement("SchoolCode", post).addTextNode(demographicInfo.getEduDtlsInfo().getSchoolCode());
	         eduDtlsInfoElem.addChildElement("CampusCode", post).addTextNode(demographicInfo.getEduDtlsInfo().getCampusCode());
	         eduDtlsInfoElem.addChildElement("InstituteUniversity", post).addTextNode(demographicInfo.getEduDtlsInfo().getInstituteUniversity());
	         eduDtlsInfoElem.addChildElement("EnrolmentStatus", post).addTextNode(demographicInfo.getEduDtlsInfo().getEnrolmentStatus());
	         eduDtlsInfoElem.addChildElement("CertificationDate", post).addTextNode(demographicInfo.getEduDtlsInfo().getCertificationDate());
	         eduDtlsInfoElem.addChildElement("StudentRegistrationNo", post).addTextNode(demographicInfo.getEduDtlsInfo().getStudentRegistrationNo());
	
	         // ...
	
	         EmploymentDetails employmentDetails = retailCustomerCreate.getEmploymentDetails();

	      // Create EmploymentDetails element
	      SOAPElement employmentDetailsElem = retailCustomerCreateRq.addChildElement("EmploymentDetails", post);
	
	      // Add elements for EmploymentDetails (include all elements)
	      employmentDetailsElem.addChildElement("EmploymentType", post).addTextNode(employmentDetails.getEmploymentType());
	      employmentDetailsElem.addChildElement("EmploymentStatus", post).addTextNode(employmentDetails.getEmploymentStatus());
	      employmentDetailsElem.addChildElement("EmpType", post).addTextNode(employmentDetails.getEmpType());
	      employmentDetailsElem.addChildElement("IncomeNature", post).addTextNode(employmentDetails.getIncomeNature());
	      employmentDetailsElem.addChildElement("IndustryType", post).addTextNode(employmentDetails.getIndustryType());
	      employmentDetailsElem.addChildElement("PaymentMode", post).addTextNode(employmentDetails.getPaymentMode());
	      employmentDetailsElem.addChildElement("EmployerID", post).addTextNode(employmentDetails.getEmployerID());
	      employmentDetailsElem.addChildElement("Occupation", post).addTextNode(employmentDetails.getOccupation());
	      employmentDetailsElem.addChildElement("Designation", post).addTextNode(employmentDetails.getDesignation());
	      employmentDetailsElem.addChildElement("EmploymentStartDate", post).addTextNode(employmentDetails.getEmploymentStartDate());
	      employmentDetailsElem.addChildElement("Pensioner", post).addTextNode(employmentDetails.getPensioner());
	      employmentDetailsElem.addChildElement("BankRelationType", post).addTextNode(employmentDetails.getBankRelationType());
	      employmentDetailsElem.addChildElement("EmployerName", post).addTextNode(employmentDetails.getEmployerName());
	      employmentDetailsElem.addChildElement("PhoneNumber", post).addTextNode(employmentDetails.getPhoneNumber());
	      employmentDetailsElem.addChildElement("EmployerCode", post).addTextNode(employmentDetails.getEmployerCode());

     
	      // ...

	      KYCDetails kycDetails = retailCustomerCreate.getKycDetails();

		   // Create KYCDetails element
		   SOAPElement kycDetailsElem = retailCustomerCreateRq.addChildElement("KYCDetails", post);
	
		   // Add elements for KYCDetails (include all elements)
		   kycDetailsElem.addChildElement("KYCStatus", post).addTextNode(kycDetails.getKYCStatus());
		   kycDetailsElem.addChildElement("KYCDate", post).addTextNode(kycDetails.getKYCDate());
		   kycDetailsElem.addChildElement("SubmittedForKYCIndicator", post).addTextNode(kycDetails.getSubmittedForKYCIndicator());
		   kycDetailsElem.addChildElement("KYCRecertificationDate", post).addTextNode(kycDetails.getKYCRecertificationDate());
		   kycDetailsElem.addChildElement("MainSourceOfFunds", post).addTextNode(kycDetails.getMainSourceOfFunds());
		   kycDetailsElem.addChildElement("OtherSourceOfFunds", post).addTextNode(kycDetails.getOtherSourceOfFunds());
		   kycDetailsElem.addChildElement("OtherBankName", post).addTextNode(kycDetails.getOtherBankName());
		   kycDetailsElem.addChildElement("PreferredCommunicationLanguage", post).addTextNode(kycDetails.getPreferredCommunicationLanguage());
		   kycDetailsElem.addChildElement("CustomerRating", post).addTextNode(kycDetails.getCustomerRating());

		   // ...

		   TaxDetails taxDetails = retailCustomerCreate.getTaxDetails();

			// Create TaxDetails element
			SOAPElement taxDetailsElem = retailCustomerCreateRq.addChildElement("TaxDetails", post);
	
			// Add elements for TaxDetails (include all elements)
			taxDetailsElem.addChildElement("TaxDeductionTable", post).addTextNode(taxDetails.getTaxDeductionTable());
			taxDetailsElem.addChildElement("TaxCountry", post).addTextNode(taxDetails.getTaxCountry());
			taxDetailsElem.addChildElement("TaxExempt", post).addTextNode(taxDetails.getTaxExempt());
			taxDetailsElem.addChildElement("ForeignTaxReportingIndicator", post).addTextNode(taxDetails.getForeignTaxReportingIndicator());
			taxDetailsElem.addChildElement("ForeignTaxReportinStatus", post).addTextNode(taxDetails.getForeignTaxReportinStatus());
			taxDetailsElem.addChildElement("ForeignTaxReportingCountry", post).addTextNode(taxDetails.getForeignTaxReportingCountry());
			taxDetailsElem.addChildElement("ForeignTaxReportingReviewDate", post).addTextNode(taxDetails.getForeignTaxReportingReviewDate());
	
			// ...

			DocumentDetails documentDetails = retailCustomerCreate.getDocumentDetails();

			// Create DocumentDetails element
			SOAPElement documentDetailsElem = retailCustomerCreateRq.addChildElement("DocumentDetails", post);

			// Create DocumentDetail element
			SOAPElement documentDetailElem = documentDetailsElem.addChildElement("DocumentDetail", post);

			// Add elements for DocumentDetail (include all elements)
			documentDetailElem.addChildElement("PreferredUniqueIdIndicator", post).addTextNode(documentDetails.getDocumentDetail().getPreferredUniqueIdIndicator());
			documentDetailElem.addChildElement("DocumentTypeCode", post).addTextNode(documentDetails.getDocumentDetail().getDocumentTypeCode());
			documentDetailElem.addChildElement("DocumentCategory", post).addTextNode(documentDetails.getDocumentDetail().getDocumentCategory());
			documentDetailElem.addChildElement("CountryOfIssue", post).addTextNode(documentDetails.getDocumentDetail().getCountryOfIssue());
			documentDetailElem.addChildElement("IssueAuthority", post).addTextNode(documentDetails.getDocumentDetail().getIssueAuthority());
			documentDetailElem.addChildElement("DocumentReferenceNumber", post).addTextNode(documentDetails.getDocumentDetail().getDocumentReferenceNumber());
			documentDetailElem.addChildElement("IsDocumentVerified", post).addTextNode(documentDetails.getDocumentDetail().getIsDocumentVerified());
			documentDetailElem.addChildElement("DocumentIssuedDate", post).addTextNode(documentDetails.getDocumentDetail().getDocumentIssuedDate());
			documentDetailElem.addChildElement("DocumentExpiryDate", post).addTextNode(documentDetails.getDocumentDetail().getDocumentExpiryDate());
			documentDetailElem.addChildElement("PlaceOfIssue", post).addTextNode(documentDetails.getDocumentDetail().getPlaceOfIssue());
			documentDetailElem.addChildElement("IDIssuedOrganisation", post).addTextNode(documentDetails.getDocumentDetail().getIdIssuedOrganisation());


			// ...


            // Add more elements as necessary

            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "Post" + "\"");

            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
            headers.addHeader("Authorization", "Basic " + authorization);

            SOAPMessage.saveChanges();

            // Send the SOAP request to the desired endpoint
            SOAPMessageResponse = postCreateRetailCustomer(SOAPMessage, retailCustomerCreateEndpoint, soaPassword);

            // Handle the response

        } catch (Exception ex) {
            // Handle exceptions
            Logger.getLogger(CreateRetailCustomer.class.getName()).log(Level.SEVERE, null, ex);
            log.info("ERROR while calling the SOAP service: " + ex);
        }

        return SOAPMessageResponse;
    }


}
