package ru.gosuslugi.im.ws.service;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.SERVER)
public class TPOSFaultException extends RuntimeException {

    public TPOSFaultException(String message, Throwable cause) {
        super(message, cause);
    }
}
