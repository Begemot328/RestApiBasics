package com.epam.esm.persistence.dao.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.QCertificate;
import com.epam.esm.persistence.dao.EntityRepository;

/**
 * Certificate DAO interface.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
public interface CertificateDAO
        extends EntityRepository<Certificate, QCertificate, Integer> {
}
