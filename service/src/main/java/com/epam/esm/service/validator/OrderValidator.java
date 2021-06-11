package com.epam.esm.service.validator;

import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.service.constants.ErrorCodes;
import com.epam.esm.service.exceptions.ValidationException;
import org.springframework.stereotype.Service;

/**
 * {@link Order} Validation class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Service
public class OrderValidator implements EntityValidator<Order> {

    @Override
    public void validate(Order order) throws ValidationException {
        validateUser(order);
        validateCertificate(order);
        validatePurchaseDate(order);
        validateAmount(order);
        validateQuantity(order);
        }

    private void validateUser(Order order) throws ValidationException {
        validateNotNullObject(order.getUser(), "User",
                ErrorCodes.ORDER_VALIDATION_EXCEPTION);
        new UserValidator().validate(order.getUser());
    }

    private void validateCertificate(Order order) throws ValidationException {
        validateNotNullObject(order.getCertificate(), "Certificate",
                ErrorCodes.ORDER_VALIDATION_EXCEPTION);
        new CertificateValidator().validate(order.getCertificate());
    }
    private void validatePurchaseDate(Order order) throws ValidationException {
        validateNotNullObject(order.getPurchaseDate(), "Purchase date",
                ErrorCodes.ORDER_VALIDATION_EXCEPTION);
    }

    private void validateQuantity(Order order) throws ValidationException {
        validatePositiveNumber(order.getCertificateQuantity(), "Quantity",
                ErrorCodes.ORDER_VALIDATION_EXCEPTION);
    }

    private void validateAmount(Order order) throws ValidationException {
        validatePositiveNumber(order.getOrderAmount(), "Amount",
                ErrorCodes.ORDER_VALIDATION_EXCEPTION);
    }
}
