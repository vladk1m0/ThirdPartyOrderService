package ru.gosuslugi.im.ws.service;

import org.springframework.stereotype.Service;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderRequest;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.GetLimitResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.RequestStatus;

@Service
public class TestThirdPartyOrderServiceImpl implements ThirdPartyOrderService {

    @Override
    public AddUserOrderResponse processAddUserOrder(final AddUserOrderRequest orderRequest) {
        return orderRequest.getDraft() == null ? null : new AddUserOrderResponse();
    }

    @Override
    public GetLimitResponse processRequestStatus(final RequestStatus requestStatus) {
        return requestStatus.getTpRequestId() == 0 ? null : new GetLimitResponse();
    }
}
