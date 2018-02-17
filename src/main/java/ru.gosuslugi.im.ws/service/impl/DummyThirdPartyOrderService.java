package ru.gosuslugi.im.ws.service.impl;

import org.springframework.stereotype.Service;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderRequest;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.GetLimitResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.RequestStatus;
import ru.gosuslugi.im.ws.service.ThirdPartyOrderService;
import ru.gosuslugi.im.ws.utils.ExcludeFromTests;

/**
 * Класс намвная реализация сервиса SID0004152.
 *
 * @since 1.0
 * @deprecated Реализовать корректную обработку запросов!
 */
@Service
@ExcludeFromTests
public class DummyThirdPartyOrderService implements ThirdPartyOrderService {

    @Override
    public AddUserOrderResponse processAddUserOrder(final AddUserOrderRequest orderRequest) {
        return new AddUserOrderResponse();
    }

    @Override
    public GetLimitResponse processRequestStatus(final RequestStatus requestStatus) {
        return new GetLimitResponse();
    }
}
