package ru.gosuslugi.im.ws.service;

import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderRequest;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.AddUserOrderResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.GetLimitResponse;
import ru.gosuslugi.im.types.thirdpartyorderservicetypes.RequestStatus;

/**
 * Интерыейс сервиса SID0004152 СМЭВ 2.х.
 *
 * @see <a target="_blank" href="http://smev.gosuslugi.ru/portal/services.jsp#!/F/IntModulP/1.00/p00smev/SID0004152">Технологический сервис интеграционного модуля.</a>
 */
public interface ThirdPartyOrderService {

    /**
     * Метод обработки запросов на получение государственной или муниципальной услуги для указанного пользователя по инициативе третьей стороны.
     *
     * @param orderRequest объект с данными запроса.
     * @return объект с данными ответа.
     */
    AddUserOrderResponse processAddUserOrder(AddUserOrderRequest orderRequest);

    /**
     * Метод обработки запросов на проверку лимитов обращения к методу addUserOrder.
     *
     * @param requestStatus объект с данными запроса.
     * @return объект с данными ответа.
     */
    GetLimitResponse processRequestStatus(RequestStatus requestStatus);
}
