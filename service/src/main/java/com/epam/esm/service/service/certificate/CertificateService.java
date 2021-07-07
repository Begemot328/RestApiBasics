package com.epam.esm.service.service.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.EntityService;

/**
 * {@link Certificate} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface CertificateService extends EntityService<Certificate> {

    /**
     * Patch {@link Certificate} object method.
     *
     * @param certificate {@link Certificate} to patch.
     */
    Certificate patch(Certificate certificate) throws ValidationException, BadRequestException, NotFoundException;
}
