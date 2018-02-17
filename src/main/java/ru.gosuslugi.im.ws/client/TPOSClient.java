package ru.gosuslugi.im.ws.client;

import org.apache.ws.security.util.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.transform.StringResult;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderRequest;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.GetLimitResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.RequestStatus;
import ru.gosuslugi.im.ws.model.DummyTPOSMessageBuilder;
import ru.gosuslugi.im.ws.service.TPOSFaultException;
import ru.gosuslugi.im.ws.signature.SmevSignatureService;
import ru.gosuslugi.smev.rev120315.BaseMessageType;
import ru.gosuslugi.smev.rev120315.ObjectFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.soap.SOAPMessage;

/**
 * Класс - адаптер к сервису SID0004152 СМЭВ 2.х.
 *
 * @see <a target="_blank" href="http://smev.gosuslugi.ru/portal/services.jsp#!/F/IntModulP/1.00/p00smev/SID0004152">Технологический сервис интеграционного модуля.</a>
 * @since 1.0
 */
@Component
public class TPOSClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TPOSClient.class);

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    @Autowired
    private Jaxb2Marshaller marshaller;

    @Autowired
    private SmevSignatureService signatureService;

    /**
     * Метод создания запросов на получение государственной или муниципальной услуги для указанного пользователя по инициативе третьей стороны.
     *
     * @param orderRequest объект с данными запроса.
     * @return объект с данными ответа.
     */
    public AddUserOrderResponse addUserOrder(final AddUserOrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new IllegalArgumentException("Argument [orderRequest] can't be null");
        }

        // Устанавливаем SOAPAction и подписываем сообщение ЭП-ОВ перед отправкой запроса.
        final WebServiceMessageCallback beforeInterceptor = message -> {
            ((SoapMessage) message).setSoapAction("http://im.gosuslugi.ru/ThirdPartyOrderService/addUserOrder");
            signWSS((SaajSoapMessage) message);
        };

        final BaseMessageType responseMsg = sendRequest(orderRequest, beforeInterceptor);
        return (AddUserOrderResponse) responseMsg.getMessageData().getAppData().getAny().get(0);
    }

    /**
     * Метод создания запросов на проверку лимитов обращения к методу addUserOrder.
     *
     * @param requestStatus объект с данными запроса.
     * @return объект с данными ответа.
     */
    public GetLimitResponse getLimit(final RequestStatus requestStatus) {
        if (requestStatus == null) {
            throw new IllegalArgumentException("Argument [requestStatus] can't be null");
        }

        // Устанавливаем SOAPAction и подписываем сообщение ЭП-ОВ перед отправкой запроса.
        final WebServiceMessageCallback beforeInterceptor = message -> {
            ((SoapMessage) message).setSoapAction("http://im.gosuslugi.ru/ThirdPartyOrderService/getLimit");
            signWSS((SaajSoapMessage) message);
        };

        final BaseMessageType responseMsg = sendRequest(requestStatus, beforeInterceptor);
        return (GetLimitResponse) responseMsg.getMessageData().getAppData().getAny().get(0);
    }

    private BaseMessageType sendRequest(final Object payload, final WebServiceMessageCallback beforeInterceptor) {
        if (payload == null) {
            throw new IllegalArgumentException("Argument [payload] can't be null");
        }

        try {
            final ObjectFactory factory = new ObjectFactory();
            final BaseMessageType requestMsg = new DummyTPOSMessageBuilder().addMessage().addMessageData(payload).build();
            final JAXBElement<BaseMessageType> request = factory.createBaseMessage(requestMsg);

            LOGGER.debug("Send request msg -->\n{}\n", element2String(request));

            final JAXBElement<BaseMessageType> response = (JAXBElement<BaseMessageType>) webServiceTemplate
                    .marshalSendAndReceive(request, beforeInterceptor);

            LOGGER.debug("Receive response msg -->\n{}\n", element2String(response));

            return response.getValue();
        } catch (SoapFaultClientException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
            throw new TPOSFaultException(ex.getLocalizedMessage(), ex);
        }
    }

    private void signWSS(SaajSoapMessage message) {
        try {
            final SOAPMessage soapMsg = message.getSaajMessage();
            signatureService.signWSS(soapMsg);
            soapMsg.saveChanges();

            LOGGER.debug("Signed request msg -->\n{}\n", XMLUtils.PrettyDocumentToString(soapMsg.getSOAPPart().getEnvelope().getOwnerDocument()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String element2String(JAXBElement je) {
        final StringResult result = new StringResult();
        marshaller.marshal(je, result);

        return result.toString();
    }
}