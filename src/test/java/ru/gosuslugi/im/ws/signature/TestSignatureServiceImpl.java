package ru.gosuslugi.im.ws.signature;

import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPMessage;

@Component
public class TestSignatureServiceImpl implements SmevSignatureService {

    @Override
    public void signWSS(SOAPMessage soapMsg) {
        if (soapMsg == null) {
            throw new IllegalArgumentException("Argument [soapMsg] can't be null");
        }
    }
}
