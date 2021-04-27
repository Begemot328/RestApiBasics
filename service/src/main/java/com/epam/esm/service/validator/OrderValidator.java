package com.epam.esm.service.validator;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.ValidationException;
import org.springframework.stereotype.Service;

/**
 * {@link Tag} Validation class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class OrderValidator implements EntityValidator<Order> {

    @Override
    public void validate(Order order) throws ValidationException {
        validateNotNullObject(order.getUser(), "User", ErrorCodes.USER_VALIDATION_EXCEPTION);
        validateNotNullObject(order.getCertificate(), "Certificate", ErrorCodes.USER_VALIDATION_EXCEPTION);
        validateNotNullObject(order.getPurchaseDate(), "Purchase date", ErrorCodes.USER_VALIDATION_EXCEPTION);
        validatePositiveNumber(order.getOrderAmount(), "Amount", ErrorCodes.USER_VALIDATION_EXCEPTION);
        validatePositiveNumber(order.getCertificateQuantity(), "Quantity", ErrorCodes.USER_VALIDATION_EXCEPTION);
    }

}
