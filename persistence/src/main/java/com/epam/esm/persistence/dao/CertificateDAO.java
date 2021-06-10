package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.persistence.model.entity.QCertificate;

/**
 * Certificate DAO interface.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
public interface CertificateDAO
        extends EntityRepository<Certificate, QCertificate, Integer> {
}
