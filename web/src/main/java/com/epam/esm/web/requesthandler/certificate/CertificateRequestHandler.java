package com.epam.esm.web.requesthandler.certificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.service.CertificateService;
import com.epam.esm.services.service.TagService;
import com.epam.esm.web.exceptions.WebLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CertificateRequestHandler {

    private final CertificateService service;
    private final TagService tagService;

    @Autowired
    public CertificateRequestHandler(CertificateService service, TagService tagService) {
        this.service = service;
        this.tagService = tagService;
    }


    public Collection<Certificate> find(Map<String, String> params) throws WebLayerException {
        Map<String, String> sorting = new HashMap<>();
        transfer(CertificateRequestParameters.SORT_BY_DATE, sorting,
                CertificateRequestParameters.SORT_BY_DATE, params);
        transfer(CertificateRequestParameters.SORT_BY_NAME, sorting,
                CertificateRequestParameters.SORT_BY_NAME, params);

        try {
            Integer tagId = null;
            Collection<Tag> tags = tagService.find(params.get(CertificateRequestParameters.TAG_NAME), null);
            if(!tags.isEmpty()) {
                tagId = ((ArrayList<Tag>) tags).get(0).getId();
            }
            return  service.find(tagId,
                    params.get(CertificateRequestParameters.NAME),
                    params.get(CertificateRequestParameters.DESCRIPTION),
                    sorting);
        } catch (ServiceException e) {
            throw new WebLayerException(e);
        }
    }

    private void transfer  (String parameterName, Map<String, String> sorting,
                       String requestParameterName, Map<String, String> params) {
        if(params.containsKey(requestParameterName)) {
            sorting.put(parameterName, params.get(requestParameterName));
        }
    }
}
