package com.epam.esm.services.service;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.services.exceptions.ServiceException;
import java.util.List;
import java.util.Map;

public interface TagService extends EntityService<Tag> {

    List<Tag> findByCertificate(int certificateId) throws ServiceException;

    List<Tag> find(Map<String, String> params) throws ServiceException;
}
