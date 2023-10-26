package com.coopbank.selfonboarding.soa.services.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
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

import com.coopbank.selfonboarding.request.AccountCreateRequest;
import com.coopbank.selfonboarding.request.CustomerAccountDetailsInquiryRequest;
import com.coopbank.selfonboarding.util.CommonMethods;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AccountCreate {

	
    public  static SOAPMessage getAccountCreate(SOAPMessage soapRequest,String customerAccDetailsInquiryEndpoint,String soaPassword) throws MalformedURLException,
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
    String url =  customerAccDetailsInquiryEndpoint;
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
    
    public static SOAPMessage getAccountCreateReq(AccountCreateRequest accountCreateReqData,String accountCreateEndpoint,String soaUsername,String soaPassword,String soaSystemCode) {
        SOAPMessage SOAPMessageResponse = null;
//        SOAPMessage response = null;

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String strDate = formatter.format(date);
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage SOAPMessage = messageFactory.createMessage();
            SOAPPart soapPart = SOAPMessage.getSOAPPart();
            String reference = UUID.randomUUID().toString();
            String myNamespace = "soapenv";
            
            String mes = "mes";
            String mesURI = "urn://co-opbank.co.ke/CommonServices/Data/Message/MessageHeader";
            String com = "com";
            String comURI = "urn://co-opbank.co.ke/CommonServices/Data/Common";
            String ns = "ns";
            String nsURI = "urn://co-opbank.co.ke/DataModel/Acccount/AccountCreate/Post/2.0";

            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";
            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();

            envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
            envelope.addNamespaceDeclaration(mes, mesURI);
            envelope.addNamespaceDeclaration(com, comURI);
            envelope.addNamespaceDeclaration(ns, nsURI);
            
            SOAPHeader header = envelope.getHeader();
            SOAPElement soapHeaderElem = header.addChildElement("RequestHeader", mes);
            SOAPElement creationTimestamp = soapHeaderElem.addChildElement("CreationTimestamp", com);
            SOAPElement correlationID = soapHeaderElem.addChildElement("CorrelationID", com);
            SOAPElement faultTO = soapHeaderElem.addChildElement("FaultTO", mes);
            SOAPElement messageID = soapHeaderElem.addChildElement("MessageID", mes);
            SOAPElement replyTO = soapHeaderElem.addChildElement("ReplyTO", mes);
            SOAPElement credentialss = soapHeaderElem.addChildElement("Credentials", mes);

            SOAPElement systemCode = credentialss.addChildElement("SystemCode", mes);
            SOAPElement userName = credentialss.addChildElement("Username", mes);
            SOAPElement password = credentialss.addChildElement("Password", mes);
            SOAPElement realm = credentialss.addChildElement("Realm", mes);

            creationTimestamp.addTextNode(strDate);
            correlationID.addTextNode(reference);
            faultTO.addTextNode("http");
            replyTO.addTextNode("http");
            messageID.addTextNode(reference);
            systemCode.addTextNode(soaSystemCode);
            userName.addTextNode(soaUsername);
            password.addTextNode(soaPassword);
            realm.addTextNode("cc");

            SOAPBody soapBody = envelope.getBody();

            SOAPElement AccountCreateRq = soapBody.addChildElement("AccountCreateRequest", ns);
            
            SOAPElement schemeCodeValue = AccountCreateRq.addChildElement("SchemeCode", ns);
            SOAPElement productValue = AccountCreateRq.addChildElement("Product", ns);
            SOAPElement branchSortCodeValue = AccountCreateRq.addChildElement("BranchSortCode", ns);
            SOAPElement currencyValue = AccountCreateRq.addChildElement("Currency", ns);
            SOAPElement customerCodeValue = AccountCreateRq.addChildElement("CustomerCode", ns);
            
            SOAPElement sectorCodeValue = AccountCreateRq.addChildElement("SectorCode", ns);
            SOAPElement subsectorCodeValue = AccountCreateRq.addChildElement("SubsectorCode", ns);
            SOAPElement purposeOfAccountValue = AccountCreateRq.addChildElement("PurposeOfAccount", ns);
            SOAPElement wHTTaxIndicatorValue = AccountCreateRq.addChildElement("WHTTaxIndicator", ns);
            SOAPElement aROCodeValue = AccountCreateRq.addChildElement("AROCode", ns);
            
            SOAPElement dSOCodeValue = AccountCreateRq.addChildElement("DSOCode", ns);
            SOAPElement statementModeValue = AccountCreateRq.addChildElement("StatementMode", ns);
            SOAPElement statementFrequencyValue = AccountCreateRq.addChildElement("StatementFrequency", ns);
            SOAPElement statementMediumValue = AccountCreateRq.addChildElement("StatementMedium", ns);
            SOAPElement startDateValue = AccountCreateRq.addChildElement("StartDate", ns);
            
            SOAPElement weekDayValue = AccountCreateRq.addChildElement("WeekDay", ns);
            SOAPElement holidayStatusValue = AccountCreateRq.addChildElement("HolidayStatus", ns);
            SOAPElement businessEconomicCodeValue = AccountCreateRq.addChildElement("BusinessEconomicCode", ns);
            SOAPElement sourceOfFundsValue = AccountCreateRq.addChildElement("SourceOfFunds", ns);
            SOAPElement productSegmentValue = AccountCreateRq.addChildElement("ProductSegment", ns);
            
       

            schemeCodeValue.addTextNode(accountCreateReqData.getSchemeCode());
            productValue.addTextNode(accountCreateReqData.getProduct());
            branchSortCodeValue.addTextNode(accountCreateReqData.getBranchSortCode());
            currencyValue.addTextNode(accountCreateReqData.getCurrency());
            customerCodeValue.addTextNode(accountCreateReqData.getCustomerCode());
            
            sectorCodeValue.addTextNode(accountCreateReqData.getSectorCode());
            subsectorCodeValue.addTextNode(accountCreateReqData.getSubsectorCode());
            purposeOfAccountValue.addTextNode(accountCreateReqData.getPurposeOfAccount());
            wHTTaxIndicatorValue.addTextNode(accountCreateReqData.getWHTTaxIndicator());
            aROCodeValue.addTextNode(accountCreateReqData.getAROCode());
            
            dSOCodeValue.addTextNode(accountCreateReqData.getDSOCode());
            statementModeValue.addTextNode(accountCreateReqData.getStatementMode());
            statementFrequencyValue.addTextNode(accountCreateReqData.getStatementFrequency());
            statementMediumValue.addTextNode(accountCreateReqData.getStatementMedium());
            startDateValue.addTextNode(accountCreateReqData.getStartDate());
            
            weekDayValue.addTextNode(accountCreateReqData.getWeekDay());
            holidayStatusValue.addTextNode(accountCreateReqData.getHolidayStatus());
            businessEconomicCodeValue.addTextNode(accountCreateReqData.getHolidayStatus());
            sourceOfFundsValue.addTextNode(accountCreateReqData.getSourceOfFunds());
            productSegmentValue.addTextNode(accountCreateReqData.getProductSegment());
            

            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "AccountCreate" + "\"");

            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
            headers.addHeader("Authorization", "Basic " + authorization);

            SOAPMessage.saveChanges();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                SOAPMessage.writeTo(out);
            } catch (IOException ex) {
                Logger.getLogger(AccountCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
//            String soapEnv = new String(out.toByteArray());
            System.out.println("\n--------------------------------- SOAP Request ---------------------------------");
          
            log.info("request is " + CommonMethods.soapMessageToString(SOAPMessage));

            log.info("\n--------------------------------- SOAP Request ---------------------------------");

            SOAPMessageResponse = getAccountCreate((SOAPMessage),accountCreateEndpoint,soaPassword);

            log.info("\n Response  is  for ID  " + SOAPMessageResponse);

        } catch (Exception ex) {
            Logger.getLogger(AccountCreate.class.getName()).log(Level.SEVERE, null, ex);
            log.info("ERROR while calling FT Webservice:" + ex);
        }

        return SOAPMessageResponse;
    }
    
}

