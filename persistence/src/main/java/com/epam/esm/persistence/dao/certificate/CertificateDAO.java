package com.epam.esm.persistence.dao.certificate;

import com.epam.esm.model.entity.Certificate;
import org.springframework.data.repository.CrudRepository;

/**
 * Certificate DAO interface.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
public interface CertificateDAO extends CrudRepository<Certificate, Integer>, CertificateDAOCustom {
}
