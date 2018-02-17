package ru.gosuslugi.im.ws.signature.impl;

import org.apache.ws.security.WSSecurityException;
import org.apache.xml.security.transforms.TransformationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.gosuslugi.im.ws.signature.SmevSignatureService;
import ru.gosuslugi.im.ws.utils.ExcludeFromTests;
import ru.smevx.crypto.smev2.wss.SoapWssUtils;

import javax.annotation.PostConstruct;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
@ExcludeFromTests
public class SmevSignatureServiceImpl implements SmevSignatureService {

    @Value("${signature.privateKey.alias}")
    private String pkeyAlias;

    @Value("${signature.certificate.alias}")
    private String certAlias;

    @Value("${signature.privateKey.password}")
    private String password;

    private PrivateKey privateKey;
    private X509Certificate cert;

    @PostConstruct
    public void init() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        final KeyStore keyStore = KeyStore.getInstance("HDImageStore");
        keyStore.load(null, null);

        this.privateKey = (PrivateKey) keyStore.getKey(pkeyAlias, password.toCharArray());
        this.cert = (X509Certificate) keyStore.getCertificate(certAlias);
    }

    @Override
    public void signWSS(SOAPMessage soapMsg) throws XMLSignatureException, GeneralSecurityException, WSSecurityException, TransformerException, SOAPException, TransformationException, MarshalException {
        if (soapMsg == null) {
            throw new IllegalArgumentException("Argument [soapMsg] can't be null");
        }
        SoapWssUtils.sign(privateKey, cert, soapMsg);
    }
}
