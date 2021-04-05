package com.epam.esm.persistence.dao;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.exceptions.DAOSQLException;
import com.epam.esm.persistence.util.EntityFinder;

import java.util.List;

public interface CertificateDAO extends EntityDAO<Certificate> {
    List<Certificate> findBy(EntityFinder<Certificate> finder) throws DAOSQLException;

    void addCertificateTag(int certificateId, int tagId);

    void deleteCertificateTag(int certificateId, int tagId);

    boolean iStagCertificateTied(int certificateId, int tagId);
}
