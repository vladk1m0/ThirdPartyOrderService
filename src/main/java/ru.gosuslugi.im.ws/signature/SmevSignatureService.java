package ru.gosuslugi.im.ws.signature;

import org.apache.ws.security.WSSecurityException;
import org.apache.xml.security.transforms.TransformationException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.security.GeneralSecurityException;

/**
 * Итерфейс сервиса формирования и проверки ЭП-ОВ в соответствии с МР СМЭВ 2.х.
 */
public interface SmevSignatureService {

    /**
     * Медод формирование ЭП-ОВ в формате WS-Security для элемента DOM дерева /Envelope/Body.
     *
     * @param soapMsg
     * @throws XMLSignatureException
     * @throws GeneralSecurityException
     * @throws WSSecurityException
     * @throws TransformerException
     * @throws SOAPException
     * @throws TransformationException
     * @throws MarshalException
     */
    void signWSS(SOAPMessage soapMsg) throws XMLSignatureException, GeneralSecurityException, WSSecurityException, TransformerException, SOAPException, TransformationException, MarshalException;
}
