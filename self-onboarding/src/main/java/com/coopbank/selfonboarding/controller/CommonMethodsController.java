package com.coopbank.selfonboarding.controller;


import java.util.HashMap;
import java.util.Map;

//
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.NodeList;

import com.coopbank.selfonboarding.request.AccountCreateRequest;
import com.coopbank.selfonboarding.request.AccountDetailsRequest;
import com.coopbank.selfonboarding.request.CreateDocumentRequest;
import com.coopbank.selfonboarding.request.CustomerAccountDetailsInquiryRequest;
import com.coopbank.selfonboarding.request.CustomerBlacklistRequest;
import com.coopbank.selfonboarding.request.CustomerDetailsSummaryRequest;
import com.coopbank.selfonboarding.request.CustomerIDRequest;
import com.coopbank.selfonboarding.request.IprsRequest;
import com.coopbank.selfonboarding.request.SanctionDetailsRequest;
import com.coopbank.selfonboarding.request.SendEmailRequest;
import com.coopbank.selfonboarding.request.SendSMSRequest;
import com.coopbank.selfonboarding.request.SigningDetailsRequest;
import com.coopbank.selfonboarding.request.ValidatePinRequest;
import com.coopbank.selfonboarding.soa.services.bean.AccountCreate;
import com.coopbank.selfonboarding.soa.services.bean.AccountDetails;
import com.coopbank.selfonboarding.soa.services.bean.ConnectCabinet;
import com.coopbank.selfonboarding.soa.services.bean.CreateDocument;
import com.coopbank.selfonboarding.soa.services.bean.CustomerAccountDetailsInquiry;
import com.coopbank.selfonboarding.soa.services.bean.CustomerBlacklist;
import com.coopbank.selfonboarding.soa.services.bean.CustomerDetailsSummary;
import com.coopbank.selfonboarding.soa.services.bean.CustomerID;
import com.coopbank.selfonboarding.soa.services.bean.IprsApis;
import com.coopbank.selfonboarding.soa.services.bean.SanctionDetails;
import com.coopbank.selfonboarding.soa.services.bean.SendEmail;
import com.coopbank.selfonboarding.soa.services.bean.SendSMS;
import com.coopbank.selfonboarding.soa.services.bean.SigningDetails;
import com.coopbank.selfonboarding.soa.services.bean.ValidatePin;
import com.coopbank.selfonboarding.util.CommonMethods;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonMethodsController {

	@Value("${api.IPRS.ENDPOINT_URL}")
	String iprsEndpoint;
	
	@Value("${api.IPRS.USERNAME}")
	String iprsUserName;
	
	@Value("${api.IPRS.PASSWORD}")
	String iprsPassword;
	
	@Value("${api.SANCTION.USERNAME}")
	String sanctionUserName;
	
	@Value("${api.SANCTION.PASSWORD}")
	String sanctionPassword;
	
	@Value("${api.BLACKLIST.ENDPOINT_URL}")
	String blacklistEndpoint;
	
	@Value("${api.CUSTOMERID.ENDPOINT_URL}")
	String customerIDEndpoint;
	
	@Value("${api.SMS.ENDPOINT_URL}")
	String smsEndpoint;
	@Value("${api.SMS.USERNAME}")
	String smsUsername;
	@Value("${api.SMS.PASSWORD}")
	String smsPassword;
	@Value("${api.SMS.ENCRYPED}")
	String smsEncryped;
	@Value("${api.SMS.CLIENTID}")
	String smsClientID;
	
	@Value("${api.SOA.USERNAME}")
	String soaUsername;
	
	@Value("${api.SOA.PASSWORD}")
	String soaPassword;
	
	@Value("${api.SOA.SYSTEMCODE}")
	String soaSystemCode;
	
	@Value("${api.KRA.ENDPOINT_URL}")
	String kraEndpoint;
	
	@Value("${api.EMAIL.ENDPOINT_URL}")
	String emailEndpoint;
	
	@Value("${api.ACCDETAILS.ENDPOINT_URL}")
	String accDetailsEndpoint;
	
	@Value("${api.SANCTIONDETAILS.ENDPOINT_URL}")
	String sanctionDetailsEndpoint;
	
	@Value("${api.CUSTOMERACCDETAILSINQUIRY.ENDPOINT_URL}")
	String customerDetailsInquiryEndpoint;
	
	@Value("${api.CUSTOMERACCDETAILSSUMMARY.ENDPOINT_URL}")
	String custDetailsSummaryEndpoint;
	
	@Value("${api.ACCOUNTCREATE.ENDPOINT_URL}")
	String accountCreateEndpoint;
	
	@Value("${api.SIGNINGDETAILS.ENDPOINT_URL}")
	String signingDetailsEndpoint;
	
	@Value("${api.CONNECTCABINET.ENDPOINT_URL}")
	String connectCabinetEndpoint;
	
	@Value("${api.CREATEDOCUMENT.ENDPOINT_URL}")
	String createDocumentEndpoint;
	
	@Value("${api.CONNECTCABINET.CABINETNAME}")
	String connectCabinetName;
	
	@Value("${api.CONNECTCABINET.USERNAME}")
	String connectCabinetUserName;
	
	@Value("${api.CONNECTCABINET.PASSWORD}")
	String connectCabinetPassword;
	
	

//	----------------------- Start IPRS Details ----------------------- //
	public static int INDENTATION = 4;
	@PostMapping("/iprsDetails")
	public ResponseEntity<Object> postIprsDetails(@RequestBody IprsRequest iprsData) throws Exception {
		log.info("Request IPRS " + iprsData.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		SOAPMessage soapResponse = IprsApis.getKslIprsDetails(iprsUserName,iprsPassword,iprsData.getDoc_type(), iprsData.getDoc_number(), iprsData.getSerial_number(),iprsEndpoint,soaUsername,soaPassword,soaSystemCode);
		String status = "";

        String Description = "";
        if (soapResponse == null) {
            status = "3";
            Description = "Failed to get response From Core Banking ";
            log.debug("We got a Null SoapResponse from Balance API Call");
            
            map.put("status", status);
            map.put("Description", Description);
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	log.info("--------------------------------- Response Header ---------------------------------");
        	log.info("Response Header " + header.toString());
        	log.info("--------------------------------- Response Header ---------------------------------");
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		log.info("Log resp"+ innerResultList.item(4).getNodeName());
        		 if (innerResultList.item(4).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(4).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
        		           String xmlString = CommonMethods.convertToString(sb);
        		          
        		            xmlString=xmlString.replace("http://schemas.datacontract.org/2004/07/IPRSManager/CommonData","");
        		            xmlString=xmlString.replace(" xmlns:ns012=\"urn://co-opbank.co.ke/Banking/External/IPRS/1.0\"","");
        		            xmlString=xmlString.replace("ns012:","");        		            
        		            xmlString=xmlString.replace("xmlns:ns6","");
        		            xmlString=xmlString.replace("=","");
        		            xmlString=xmlString.replace("ns6:","");
        		            xmlString=xmlString.replaceAll("\"", "");
        		            xmlString=xmlString.replaceAll(" >", ">");
        		            
        		            log.info(xmlString);
          		           try {
                           JSONObject jsonObj = XML.toJSONObject(xmlString);
                           String json = jsonObj.toString(INDENTATION); 
                              map.put("Status", "true");
                              map.put("StatusDescription", statusDesc);
                              map.put("Response", json); 
                                                            
                           } catch (JSONException ex) {
                               ex.printStackTrace();
                           }
        		            
                           
        			 } else {
        				 map.put("status", statusDesc);
        				 map.put("Description", Description);
                     }
        		 } else {
        			 Description = "We got an invalid Response";
        			 map.put("Description", Description);
                     log.info("We got an invalid Response");
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End IPRS Details ----------------------- //
	
//	----------------------- Start Blacklist Details ----------------------- //
	@GetMapping("/blacklistDetails")
	public ResponseEntity<Object> getblacklistDetails(@RequestBody CustomerBlacklistRequest blacklistData) throws Exception {
		log.info("Request blacklistData " + blacklistData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = CustomerBlacklist.getCustomerBlacklistDetails(blacklistData.getIdentificationType(), blacklistData.getIdentificationNumber(),blacklistEndpoint,soaUsername,soaPassword,soaSystemCode);
		String status = "";
        String Description = "";
        
        if (soapResponse == null) {
            status = "Failed";
            Description = "Failed to get response for customer Blacklist";
            log.debug("Failed to get response for customer Blacklist");
            
            map.put("status", status);
            map.put("Description", Description);
        } 
        else {

        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	xmlStringHeader = xmlStringHeader.replace("head:",""); 
        	xmlStringHeader = xmlStringHeader.replace("tns3:",""); 
        	
        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];

        	log.info(xmlStringHeader);
        	
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		log.info("Log resp"+ innerResultList.item(3).getNodeName());
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Not found/Not negated")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
        		           String xmlString = CommonMethods.convertToString(sb);


        		           map.put("Status", "true");
        		           map.put("StatusDescription", statusDesc);
        		           map.put("MessageDescription", messageDescriptionRes);
 
        			 }else if (statusDesc.equals("Failed")) {
        				 map.put("Status", "false");
      		             map.put("StatusDescription", statusDesc);
      		             map.put("MessageDescription", messageDescriptionRes);
        			 } 
        			 else {
        				 map.put("status", "false");
        				 map.put("StatusDescription", Description);
        				 map.put("MessageDescription", messageDescriptionRes);
                     }
        		 } else {
        			 
        			 map.put("status", "false");
    				 map.put("StatusDescription", "We got an invalid Response");
    				 map.put("MessageDescription", "Failed to get response for customer Blacklist");
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End Blacklist Details ----------------------- //
	
	
//	----------------------- Start CustomerID Details ----------------------- //
	@GetMapping("/CustomerIDData")
	public ResponseEntity<Object> getCustomerIDData(@RequestBody CustomerIDRequest ncustomerIDData) throws Exception {
		log.info("Request customerIDData " + ncustomerIDData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = CustomerID.getCustomerIDDetails(ncustomerIDData.getUniqueIdentifierType(),ncustomerIDData.getUniqueIdentifierValue(),customerIDEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace("\"urn://co-opbank.co.ke/BS/Customer/CustomerId.Get.3.0\"","");
  		            xmlString=xmlString.replace("xmlns:tns29","");
  		            xmlString=xmlString.replace("=","");
  		            xmlString=xmlString.replace("tns29:","");
      		        log.info(xmlString);
      		            
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", "We got an invalid Response");
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", "We got an invalid Response");
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End CustomerID Details ----------------------- //

	
//	----------------------- Start SendSMS Details ----------------------- //
	@PostMapping("/Sendsms")
	public ResponseEntity<Object> postSendSMSReq(@RequestBody SendSMSRequest sendSMSData) throws Exception {
		log.info("Request sendSMSData " + sendSMSData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = SendSMS.postSendSMS(smsUsername,smsPassword,smsEncryped,smsClientID,sendSMSData.getReqmessage(),sendSMSData.getReqmsisdn(),smsEndpoint,soaUsername,soaPassword,soaSystemCode);
        
        if (soapResponse == null) {
      	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
        		            String xmlString = CommonMethods.convertToString(sb);             		        
        		            String descriptionTag = "description";
        		            String description = xmlString.split("<"+ descriptionTag +">")[1].split("</"+ descriptionTag+">")[0];
        		            
	        		           map.put("Status", statusDesc);
	                           map.put("Description", description);

        			 } else {
        				 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", "We got an invalid Response");
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", "We got an invalid Response");
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End SendSMS Details ----------------------- //
	
//	----------------------- Start ValidatePin Details ----------------------- //
	@GetMapping("/validatepin")
	public ResponseEntity<Object> getValidatePinData(@RequestBody ValidatePinRequest validatePinData) throws Exception {
		log.info("Request validatePinData " + validatePinData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = ValidatePin.getKslValidatePin(validatePinData.getIdNumber(),validatePinData.getTaxPayerType(),kraEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(2).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(2).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace("\"urn://co-opbank.co.ke/BS/Customer/CustomerId.Get.3.0\"","");
  		            xmlString=xmlString.replace("xmlns:tns29","");
  		            xmlString=xmlString.replace("=","");
  		            xmlString=xmlString.replace("tns29:","");
      		        log.info(xmlString);
      		            
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", "We got an invalid Response");
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", "We got an invalid Response");
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End ValidatePin Details ----------------------- //
	
//	----------------------- Start SendEmail Details ----------------------- //
	@PostMapping("/sendemail")
	public ResponseEntity<Object> postSendEmailReq(@RequestBody SendEmailRequest sendEmailData) throws Exception {
		log.info("Request sendEmailData " + sendEmailData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = SendEmail.postKslSendEmail(sendEmailData.getFrom(),sendEmailData.getTo(),sendEmailData.getMessage(),sendEmailData.getSubject(),emailEndpoint,soaUsername,soaPassword,soaSystemCode);
		
        if (soapResponse == null) {
      	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	NodeList returnList = (NodeList) header.getElementsByTagName("tns1:HeaderReply");

        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(4).getNodeName().equalsIgnoreCase("tns1:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(4).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
        		            String xmlString = CommonMethods.convertToString(sb);  
//        		            log.info("Send Email "+xmlString);
        		            xmlString=xmlString.replace("xmlns:ns1=\"urn://co-opbank.co.ke/Banking/Common/DataModel/CommonEmail/Send/1.0/CommonEmail.send","");
          		            xmlString=xmlString.replace("ns1:","");
          		            
        		            String operationDateTag = "OperationDate";
        		            String operationDateRes = xmlString.split("<"+ operationDateTag +">")[1].split("</"+ operationDateTag+">")[0];
	        		           map.put("Status", statusDesc);
	        		           map.put("Response", "Email Sent!");
	                           map.put("OperationDate", operationDateRes);

        			 } else {
        				 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", "We got an invalid Response");
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", "We got an invalid Response");
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End SendEmail Details ----------------------- //
	
//	----------------------- Start AccountDetails Details ----------------------- //
	@GetMapping("/accountDetails")
	public ResponseEntity<Object> getAccountDetailsData(@RequestBody AccountDetailsRequest AccountDetailsData) throws Exception {
		log.info("Request AccountDetailsData " + AccountDetailsData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = AccountDetails.getAccountDetailsDetails(AccountDetailsData.getAccountNumber(),accDetailsEndpoint,soaUsername,soaPassword,soaSystemCode);
		map.put("StatusDescription", "Service timeout");
        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
		            xmlString=xmlString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
		            xmlString=xmlString.replace("xmlns:tns25=\"urn://co-opbank.co.ke/BS/Account/BSAccountDetails.3.0\"","");
      		        xmlString=xmlString.replace("xmlns:tns25\"urn://co-opbank.co.ke/BS/Account/BSAccountDetails.3.0\"","");
  		            xmlString=xmlString.replace("tns25:","");
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
      		        log.info(json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", "We got an invalid Response");
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", "We got an invalid Response");
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End AccountDetails Details ----------------------- //
	
	
//	----------------------- Start SanctionDetails Details ----------------------- //
	@PostMapping("/sanctionDetails")
	public ResponseEntity<Object> postSanctionDetails(@RequestBody SanctionDetailsRequest sanctionDetailsData) throws Exception {
		log.info("Request sanctionDetailsData " + sanctionDetailsData.toString());
//		log.debug("Start Account Balance " + iprsData.getPass());
//		HashMap<String, Object> map = new HashMap<>();
		Map<String, Object> map = new HashMap<String, Object>();
		SOAPMessage soapResponse = SanctionDetails.getCustomerSanctionDetails(sanctionDetailsData.getMinMatchScore(),sanctionDetailsData.getCustomerType(),sanctionDetailsData.getFullName(),sanctionDetailsData.getFirstName(),sanctionDetailsData.getMiddleName(),sanctionDetailsData.getLastName(),sanctionDetailsData.getIdentificationDocType(),sanctionDetailsData.getIdentificationDocNo(),sanctionDetailsData.getNationality(),sanctionDetailsData.getDateOfBirth(),sanctionDetailsData.getCountryOfBirth(),sanctionDetailsEndpoint,sanctionUserName,sanctionPassword,soaSystemCode);
		String status = "";

        String Description = "";
        if (soapResponse == null) {
            status = "3";
            Description = "Failed to get response From Core Banking ";
            log.debug("We got a Null SoapResponse from Sanction Details API Call");
            
            map.put("status", status);
            map.put("Description", Description);
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	SOAPBody sb = soapResponse.getSOAPBody();
        	
        	String xmlString = CommonMethods.convertToString(sb);
	           
        	SOAPHeader hd = soapResponse.getSOAPHeader();
        	String xmlStringHeader = CommonMethods.convertHeaderString(hd);
	           
	           log.info(xmlString);
	           log.info(xmlStringHeader);
	           xmlStringHeader = xmlStringHeader.replace("head:",""); 
	           
	            xmlString=xmlString.replace(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"","");
	            xmlString=xmlString.replace(" xmlns:ns7=\"urn://co-opbank.co.ke/DataModel/Customer/SanctionDetails/Get/1.0\"","");
	            xmlString=xmlString.replace("ns7:","");        		            
	            xmlString=xmlString.replace(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"","");
	            xmlString=xmlString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
	            
	            log.info(xmlString);
	            log.info(xmlStringHeader);      	
        	
	            String messageDescriptionTag = "MessageDescription";
	            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];

	            JSONObject jsonObj = XML.toJSONObject(xmlString);
                String json = jsonObj.toString(INDENTATION);
        	log.info("--------------------------------- Response Header ---------------------------------");
        	log.info("Response Header " + header.toString());
        	log.info("--------------------------------- Response Header ---------------------------------");
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		log.info("Log resp"+ innerResultList.item(4).getNodeName());
        		 if (innerResultList.item(4).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(4).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
          		           try {
                              map.put("Status", "true");
                              map.put("StatusDescription", statusDesc);
                              map.put("MessageDescription", messageDescriptionRes);
                              map.put("Response", json); 
                              
                                                            
                           } catch (JSONException ex) {
                               ex.printStackTrace();
                           }
        		            
                           
        			 } else {
        				 
        				 map.put("Status", "false");
                         map.put("StatusDescription", statusDesc);
                         map.put("MessageDescription", messageDescriptionRes);
                         map.put("Response", json);
                     }
        		 } else {
        			 map.put("Status", "false");
                     map.put("StatusDescription", "Failed");
                     map.put("MessageDescription", Description);
                     map.put("Response", json);
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End SanctionDetails Details ----------------------- //
	
	
//	----------------------- Start Customer Account Details Inquiry ----------------------- //
	@GetMapping("/CustomerAccountDetailsInquiry")
	public ResponseEntity<Object> getCustomerAccountDetailsInquiryData(@RequestBody CustomerAccountDetailsInquiryRequest custAccDetailsInqReqData) throws Exception {
		log.info("Request CustomerAccountDetailsInquiryData " + custAccDetailsInqReqData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = CustomerAccountDetailsInquiry.getCustomerAccountDetailsInquiry(custAccDetailsInqReqData.getCustomerId(),customerDetailsInquiryEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
         	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	xmlStringHeader = xmlStringHeader.replace("head:",""); 
        	xmlStringHeader = xmlStringHeader.replace("tns3:",""); 
        	
        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];

            
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace("\"urn://co-opbank.co.ke/BS/Customer/CustomerAccount.Get.3.0\"","");
  		            xmlString=xmlString.replace("xmlns:tns28","");
  		            xmlString=xmlString.replace("=","");
  		            xmlString=xmlString.replace("tns28:","");
  		            xmlString=xmlString.replace("<?xml version\"1.0\" encoding\"UTF-8\" standalone\"no\"?>","");
      		        log.info(xmlString);
      		            
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End Customer Account Details Inquiry ----------------------- //

	
//	----------------------- Start Customer Details Summary ----------------------- //
	@GetMapping("/CustomerDetailsSummary")
	public ResponseEntity<Object> getCustomerAccountSummaryData(@RequestBody CustomerDetailsSummaryRequest custAccSummaryReqData) throws Exception {
		log.info("Request CustomerAccountDetailsInquiryData " + custAccSummaryReqData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = CustomerDetailsSummary.getcustomerDetailsSummaryReq(custAccSummaryReqData.getCustomerId(),custDetailsSummaryEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	xmlStringHeader = xmlStringHeader.replace("head:",""); 
        	xmlStringHeader = xmlStringHeader.replace("tns3:",""); 
        	
        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];

            
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace(" xmlns:tns28=\"urn://co-opbank.co.ke/BS/Customer/CustomerDetailsSummary.1.0\"","");
  		            xmlString=xmlString.replace(" xmlns:tns30=\"urn://co-opbank.co.ke/TS/Finacle/CustomerDetailsSummary.Get.1.0\"","");
  		            xmlString=xmlString.replace("tns28:","");
  		            xmlString=xmlString.replace("<?xml version\"1.0\" encoding\"UTF-8\" standalone\"no\"?>","");
      		        log.info(xmlString);
      		            
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End Customer Details Summary ----------------------- //
	
//	----------------------- Start Account Create ----------------------- //
	@PostMapping("/AccountCreate")
	public ResponseEntity<Object> getAccountCreateData(@RequestBody AccountCreateRequest accountCreateReqData) throws Exception {
		log.info("Request AccountCreate " + accountCreateReqData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = AccountCreate.getAccountCreateReq(accountCreateReqData,accountCreateEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	xmlStringHeader = xmlStringHeader.replace("head:",""); 
        	xmlStringHeader = xmlStringHeader.replace("tns3:",""); 
        	
        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];


        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        			SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace(" xmlns:tns28=\"urn://co-opbank.co.ke/BS/Customer/CustomerDetailsSummary.1.0\"","");
  		            xmlString=xmlString.replace(" xmlns:tns30=\"urn://co-opbank.co.ke/TS/Finacle/CustomerDetailsSummary.Get.1.0\"","");
  		            xmlString=xmlString.replace("tns28:","");
  		            xmlString=xmlString.replace("<?xml version\"1.0\" encoding\"UTF-8\" standalone\"no\"?>","");
      		        log.info(xmlString);
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);

                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }
		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
  //	----------------------- End Account Create----------------------- //
	
//	----------------------- Start Signing Details ----------------------- //
	@PostMapping("/SigningDetails")
	public ResponseEntity<Object> getAccountCreateData(@RequestBody SigningDetailsRequest signingDetailsReqData) throws Exception {
		log.info("Request SigningDetails " + signingDetailsReqData.toString());
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = SigningDetails.getSigningDetailsReq(signingDetailsReqData,signingDetailsEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	xmlStringHeader = xmlStringHeader.replace("head:",""); 
        	xmlStringHeader = xmlStringHeader.replace("tns3:",""); 
        	
        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];


        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        			SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace(" xmlns:ns8=\"urn://co-opbank.co.ke/BS/DataModel/Account/SigningDetails/Post/2.0\"","");
  		            xmlString=xmlString.replace("ns8:","");
  		            xmlString=xmlString.replace(" <?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
      		        log.info(xmlString);
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
	//	----------------------- End Signing Details ----------------------- //
	
	
	//	----------------------- Start Connect Cabinet----------------------- //
	public String getConnectCabinetData() throws Exception {
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = ConnectCabinet.postConnectCabinetReq(connectCabinetName,connectCabinetUserName,connectCabinetPassword,connectCabinetEndpoint,soaUsername,soaPassword,soaSystemCode);
		String userDBIdTagRes = null;
        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	
        	xmlStringHeader = xmlStringHeader.replace("tns33:",""); 

        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];


        	NodeList returnList = (NodeList) header.getElementsByTagName("tns33:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("tns33:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        			SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace(" xmlns:ns12=\"urn://co-opbank.co.ke/DataModel/DocMgt/ConnectCabinet/Post/1.0/Body\"","");
  		            xmlString=xmlString.replace("ns12:","");
  		            xmlString=xmlString.replace(" <?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
      		        log.info(xmlString);
      		        
      		      String userDBIdTag = "UserDBId";
                  userDBIdTagRes = xmlString.split("<"+ userDBIdTag +">")[1].split("</"+ userDBIdTag+">")[0];

     		            
     		        try {
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("UserDBId", userDBIdTagRes);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return userDBIdTagRes;

	}
	//	----------------------- End Connect Cabinet ----------------------- //
	
	
//	----------------------- Start Create Document----------------------- //
	@PostMapping("/CreateDocument")
	public ResponseEntity<Object> getCreateDocumentData(@RequestBody CreateDocumentRequest createDocumentReqData) throws Exception {
		log.info("Request CreateDocument " + createDocumentReqData.toString());
		HashMap<String, String> map = new HashMap<>();
		String userId = getConnectCabinetData();
		if (userId == null) {            
	     	   map.put("Status", "false");
	           map.put("StatusDescription", "Failed");
	           map.put("Description", "Connect Cabinet Service error");
	        } 
		SOAPMessage soapResponse = CreateDocument.postCreateDocumentReq(createDocumentReqData,userId,createDocumentEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	
        	xmlStringHeader = xmlStringHeader.replace("tns33:",""); 
        	
        	String statusDescriptionTag = "StatusDescription";
            String statusDescriptionRes = xmlStringHeader.split("<"+ statusDescriptionTag +">")[1].split("</"+ statusDescriptionTag+">")[0];
            String messageDescriptionRes = null;
            
            if(statusDescriptionRes.equals("Failed")) {
	        	String messageDescriptionTag = "MessageDescription";
	            messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];
			}


        	NodeList returnList = (NodeList) header.getElementsByTagName("tns33:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("tns33:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        			SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace("xmlns:ns12=\"urn://co-opbank.co.ke/DataModel/DocMgt/CreateDocument/Post/1.0/Body\"","");
  		            xmlString=xmlString.replace("ns12:","");
  		            xmlString=xmlString.replace(" <?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
      		        log.info(xmlString);
      		                 
     		        try {
     		        	JSONObject jsonObj = XML.toJSONObject(xmlString);
                        String json = jsonObj.toString(INDENTATION);
               
                       map.put("Status", "true");
                       map.put("StatusDescription", statusDesc);
                       map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
	//	----------------------- End CreateDocument ----------------------- //
	
}


