package ru.gosuslugi.im.ws.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.*;
import ru.gosuslugi.im.ws.TestSpringWSApplication;
import ru.gosuslugi.im.ws.client.TPOSClient;
import ru.gosuslugi.im.ws.service.TPOSFaultException;
import ru.gosuslugi.im.ws.utils.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringWSApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TPOSClientTest {

    @Autowired
    private TPOSClient orderServiceClient;

    @Before
    public void setUp() {
        Assert.notNull(orderServiceClient, "orderResponse can't be null");
    }

    @Test
    public void addUserOrderOk() {
        final AddUserOrderRequest orderRequest = new AddUserOrderRequest();
        final Person person = new Person();

        person.setSnils("000-000-000 00");
        person.setFirstName("Имя");
        person.setSecondName("Отчество");
        person.setLastName("Фамилия");
        person.setBdate(DateUtils.getCalendarNow());
        person.setPhone("+7 (926) 123-45-67");

        final PersonPassport passport = new PersonPassport();
        passport.setSeries("12 34");
        passport.setNum("123456");
        passport.setIssueCode("123-456");
        passport.setIssueDate(DateUtils.getCalendarNow());
        person.setPassport(passport);

        final Draft draft = new Draft();
        draft.setVersion(0);
        draft.setFields(new ListFields());

        orderRequest.setDraft(draft);
        orderRequest.setPerson(person);
        orderRequest.setServiceCode("10000000000");
        orderRequest.setServiceFormCode("10000000000");

        final AddUserOrderResponse orderResponse = orderServiceClient.addUserOrder(orderRequest);
        Assert.isInstanceOf(AddUserOrderResponse.class, orderResponse, "orderResponse must be instance of AddUserOrderResponse");
    }

    @Test(expected = TPOSFaultException.class)
    public void addUserOrderFail() {
        final AddUserOrderRequest orderRequest = new AddUserOrderRequest();
        final Person person = new Person();

        person.setSnils("000-000-000 00");
        person.setFirstName("Имя");
        person.setSecondName("Отчество");
        person.setLastName("Фамилия");
        person.setBdate(DateUtils.getCalendarNow());
        person.setPhone("+7 (926) 123-45-67");

        final PersonPassport passport = new PersonPassport();
        passport.setSeries("12 34");
        passport.setNum("123456");
        passport.setIssueCode("123-456");
        passport.setIssueDate(DateUtils.getCalendarNow());
        person.setPassport(passport);

        orderRequest.setPerson(person);
        orderRequest.setServiceCode("10000000000");
        orderRequest.setServiceFormCode("10000000000");

        orderServiceClient.addUserOrder(orderRequest);
    }

    @Test
    public void getLimitOk() {
        final RequestStatus requestStatus = new RequestStatus();
        requestStatus.setTpRequestId(100);
        requestStatus.setStatusCode("100");
        final GetLimitResponse limit = orderServiceClient.getLimit(requestStatus);
        Assert.isInstanceOf(GetLimitResponse.class, limit, "requestStatus must be instance of GetLimitResponse");
    }

    @Test(expected = TPOSFaultException.class)
    public void getLimitFail() {
        final RequestStatus requestStatus = new RequestStatus();
        requestStatus.setTpRequestId(0);
        requestStatus.setStatusCode("0");
        orderServiceClient.getLimit(requestStatus);
    }
}