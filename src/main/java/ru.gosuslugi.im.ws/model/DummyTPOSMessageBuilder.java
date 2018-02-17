package ru.gosuslugi.im.ws.model;

import ru.gosuslugi.im.ws.utils.DateUtils;
import ru.gosuslugi.smev.rev120315.*;

/**
 * Класс конструктор конвертов для сообщений в соответствии с Методическими рекомендациями по работе со СМЭВ 2.х.
 *
 * @since 1.0
 * @deprecated Реализовать корректное заполнение служебных блоков конверта!
 */
public class DummyTPOSMessageBuilder {

    private BaseMessageType baseMessage = new BaseMessageType();

    public DummyTPOSMessageBuilder addMessage() {

        final MessageType message = new MessageType();

        final OrgExternalType sender = new OrgExternalType();
        sender.setCode("BANK01001");
        sender.setName("Банк");
        message.setSender(sender);

        final OrgExternalType recipient = new OrgExternalType();
        recipient.setCode("IPGU01001");
        recipient.setName("ЕПГУ");
        message.setRecipient(recipient);

        message.setOriginator(message.getSender());
        message.setServiceName("IM");
        message.setTypeCode(TypeCodeType.GSRV);
        message.setStatus(StatusType.REQUEST);
        message.setExchangeType("1");
        message.setServiceCode("111222");
        message.setTestMsg("Test");

        message.setDate(DateUtils.getCalendarNow());

        baseMessage.setMessage(message);

        return this;
    }

    public DummyTPOSMessageBuilder addMessageData(Object requestData) {
        if (requestData == null) {
            throw new IllegalArgumentException("Argument [requestData] can't be null");
        }

        baseMessage.setMessageData(new MessageDataType());

        final AppDataType appData = new AppDataType();
        appData.getAny().add(requestData);
        baseMessage.getMessageData().setAppData(appData);

        return this;
    }

    public BaseMessageType build() {
        return baseMessage;
    }
}
