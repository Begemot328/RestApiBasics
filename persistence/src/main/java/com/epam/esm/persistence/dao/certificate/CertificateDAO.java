package com.epam.esm.persistence.dao.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.dao.EntityDAO;
import com.epam.esm.persistence.util.finder.EntityFinder;

import java.util.List;

/**
 * Certificate DAO interface.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
public interface CertificateDAO extends EntityDAO<Certificate> {

    /**
     * Find using {@link EntityFinder} method.
     *
     * @param query {@link String} query to obtain entities.
     * @return {@link java.util.ArrayList}  of {@link Certificate} objects.
     */
    List<Certificate> finadByQuery(String query);
}
