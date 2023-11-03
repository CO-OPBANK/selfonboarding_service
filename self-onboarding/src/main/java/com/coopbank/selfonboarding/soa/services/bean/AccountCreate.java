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
            
            AccountCreateRq.addChildElement("SchemeCode", ns).addTextNode(accountCreateReqData.getSchemeCode());
            AccountCreateRq.addChildElement("Product", ns).addTextNode(accountCreateReqData.getProduct());
            AccountCreateRq.addChildElement("BranchSortCode", ns).addTextNode(accountCreateReqData.getBranchSortCode());
            AccountCreateRq.addChildElement("Currency", ns).addTextNode(accountCreateReqData.getCurrency());
            AccountCreateRq.addChildElement("CustomerCode", ns).addTextNode(accountCreateReqData.getCustomerCode());
            AccountCreateRq.addChildElement("SectorCode", ns).addTextNode(accountCreateReqData.getSectorCode());
            AccountCreateRq.addChildElement("SubsectorCode", ns).addTextNode(accountCreateReqData.getSubsectorCode());
            AccountCreateRq.addChildElement("PurposeOfAccount", ns).addTextNode(accountCreateReqData.getPurposeOfAccount());
            AccountCreateRq.addChildElement("WHTTaxIndicator", ns).addTextNode(accountCreateReqData.getWHTTaxIndicator());
            AccountCreateRq.addChildElement("AROCode", ns).addTextNode(accountCreateReqData.getAROCode());
            AccountCreateRq.addChildElement("DSOCode", ns).addTextNode(accountCreateReqData.getDSOCode());
            AccountCreateRq.addChildElement("StatementMode", ns).addTextNode(accountCreateReqData.getStatementMode());
            AccountCreateRq.addChildElement("StatementFrequency", ns).addTextNode(accountCreateReqData.getStatementFrequency());
            AccountCreateRq.addChildElement("StatementMedium", ns).addTextNode("");
            AccountCreateRq.addChildElement("StartDate", ns).addTextNode(accountCreateReqData.getStartDate());
            AccountCreateRq.addChildElement("WeekDay", ns).addTextNode(accountCreateReqData.getWeekDay());
            AccountCreateRq.addChildElement("HolidayStatus", ns).addTextNode(accountCreateReqData.getHolidayStatus());
            AccountCreateRq.addChildElement("BusinessEconomicCode", ns).addTextNode(accountCreateReqData.getBusinessEconomicCode());
            AccountCreateRq.addChildElement("SourceOfFunds", ns).addTextNode(accountCreateReqData.getSourceOfFunds());
            AccountCreateRq.addChildElement("ProductSegment", ns).addTextNode(accountCreateReqData.getProductSegment());


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

