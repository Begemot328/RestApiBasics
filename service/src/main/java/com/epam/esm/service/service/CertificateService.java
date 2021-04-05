package com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.ServiceException;
import com.epam.esm.service.exceptions.ValidationException;
import java.util.List;
import java.util.Map;

public interface CertificateService extends EntityService<Certificate> {

    List<Certificate> findByTag(int tagId) throws ServiceException;

    void addCertificateTag(Certificate certificate, int tagId) throws ServiceException, ValidationException;

    void addCertificateTag(int certificateId, Tag tag) throws ServiceException, ValidationException;

    void deleteCertificateTag(int certificateId, int tagId) throws ServiceException;

    List<Certificate> find(Map<String, String> params) throws ServiceException;
}
