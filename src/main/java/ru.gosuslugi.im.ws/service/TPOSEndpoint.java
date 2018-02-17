package ru.gosuslugi.im.ws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.addressing.server.annotation.Address;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderRequest;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.GetLimitResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.RequestStatus;
import ru.gosuslugi.im.ws.model.DummyTPOSMessageBuilder;
import ru.gosuslugi.smev.rev120315.BaseMessageType;
import ru.gosuslugi.smev.rev120315.ObjectFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

/**
 * Класс реализующий веб-сервис в соответствии с требованиями контракта SID0004152 СМЭВ 2.х.
 *
 * @see <a target="_blank" href="http://smev.gosuslugi.ru/portal/services.jsp#!/F/IntModulP/1.00/p00smev/SID0004152">Технологический сервис интеграционного модуля.</a>
 */
@Endpoint
@Address("http://localhost:8080/ws/ThirdPartyOrderService")
public class TPOSEndpoint {

    @Autowired
    private Jaxb2Marshaller marshaller;

    @Autowired
    private ThirdPartyOrderService orderService;

    /**
     * Метод обработки запросов на получение государственной или муниципальной услуги для указанного пользователя по инициативе третьей стороны.
     * SoapAction = "http://im.gosuslugi.ru/ThirdPartyOrderService/addUserOrder".
     *
     * @param request объект с данными запроса
     * @return объект с данными ответа.
     */
    @SoapAction("http://im.gosuslugi.ru/ThirdPartyOrderService/addUserOrder")
    @ResponsePayload
    public Element addUserOrder(@RequestPayload Element request) {
        if (request == null) {
            throw new IllegalArgumentException("Argument request can't be null");
        }

        try {
            final Object payload = unmarshalPayload(request);
            if (!(payload instanceof AddUserOrderRequest)) {
                throw new IllegalStateException(String.format("AppData payload must be an AddUserOrderRequest type, but found is %s", payload.getClass().getTypeName()));
            }

            final AddUserOrderResponse orderResponse = orderService.processAddUserOrder((AddUserOrderRequest) payload);
            if (orderResponse == null) {
                throw new IllegalStateException("Order response can't be null");
            }

            return marshalPayload(orderResponse);
        } catch (Exception ex) {
            throw new TPOSFaultException(ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * Метод обработки запросов на проверку лимитов обращения к методу addUserOrder.
     * SoapAction = "http://im.gosuslugi.ru/ThirdPartyOrderService/getLimit"
     *
     * @param request объект с данными запроса.
     * @return объект с данными ответа.
     */
    @SoapAction("http://im.gosuslugi.ru/ThirdPartyOrderService/getLimit")
    @ResponsePayload
    public Element getLimit(@RequestPayload Element request) {
        if (request == null) {
            throw new IllegalArgumentException("Argument request can't be null");
        }

        try {
            final Object payload = unmarshalPayload(request);
            if (!(payload instanceof RequestStatus)) {
                throw new IllegalStateException(String.format("AppData payload must be an RequestStatus type, but found is %s", payload.getClass().getTypeName()));
            }

            final GetLimitResponse limitResponse = orderService.processRequestStatus((RequestStatus) payload);
            if (limitResponse == null) {
                throw new IllegalStateException("Limit response can't be null");
            }

            return marshalPayload(limitResponse);
        } catch (Exception ex) {
            throw new TPOSFaultException(ex.getLocalizedMessage(), ex);
        }
    }

    private Object unmarshalPayload(final Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Argument [element] can't be null");
        }

        final DOMSource source = new DOMSource();
        source.setNode(element);

        final JAXBElement<BaseMessageType> jaxbElement = (JAXBElement<BaseMessageType>) marshaller.unmarshal(source);
        if (jaxbElement == null) {
            throw new IllegalStateException("Unmarshalling request exception");
        }

        return jaxbElement.getValue().getMessageData().getAppData().getAny().get(0);
    }

    private Element marshalPayload(final Object payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Argument [payload] can't be null");
        }

        final BaseMessageType bmt = new DummyTPOSMessageBuilder().addMessage().addMessageData(payload).build();

        final ObjectFactory factory = new ObjectFactory();
        final JAXBElement<BaseMessageType> jaxbResponse = factory.createBaseMessage(bmt);

        final DOMResult result = new DOMResult();
        marshaller.marshal(jaxbResponse, result);

        return ((Document) result.getNode()).getDocumentElement();
    }
}
