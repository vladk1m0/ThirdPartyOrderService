package ru.gosuslugi.im.ws.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.WSSecurityException;
import org.apache.xml.security.transforms.TransformationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.server.SoapEndpointInterceptor;
import ru.gosuslugi.im.ws.signature.SmevSignatureService;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.security.GeneralSecurityException;

/**
 * Класс интерцептор реализующий функию формирования ЭП-ОВ сообщений-ответов веб-сервиса SID0004152.
 */
@Component
public class TPOSSignatureInterceptor implements SoapEndpointInterceptor {

    private static final Log LOG = LogFactory.getLog(TPOSSignatureInterceptor.class);

    @Autowired
    private SmevSignatureService signatureService;

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) {
        return true;
    }

    /**
     * Метод формирования ЭП-ОВ корректных сообщений-ответов.
     *
     * @param messageContext объект сообщение-ответ.
     * @param endpoint       объект веб-сервис.
     * @return признак успешной обработки сообщений-ответов.
     * @throws Exception
     */
    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        LOG.debug("Outcome response handling");
        signWSS(messageContext);
        return true;
    }

    /**
     * Метод формирования ЭП-ОВ ошибочных сообщений-ответов.
     *
     * @param messageContext объект сообщение-ответ.
     * @param endpoint       объект веб-сервис.
     * @return признак успешной обработки сообщений-ответов.
     */
    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        LOG.debug("Outcome fault handling");
        signWSS(messageContext);
        return true;
    }

    private void signWSS(MessageContext messageContext) throws XMLSignatureException, GeneralSecurityException, WSSecurityException, TransformerException, SOAPException, TransformationException, MarshalException {
        SaajSoapMessage response = (SaajSoapMessage) messageContext.getResponse();
        final SOAPMessage soapMsg = response.getSaajMessage();

        // Формирование ЭП ответа
        signatureService.signWSS(soapMsg);
        soapMsg.saveChanges();
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) {
    }

    @Override
    public boolean understands(SoapHeaderElement header) {
        return false;
    }
}
