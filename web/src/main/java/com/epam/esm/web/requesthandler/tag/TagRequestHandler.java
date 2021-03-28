package com.epam.esm.web.requesthandler.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.services.exceptions.ServiceException;
import com.epam.esm.services.service.CertificateService;
import com.epam.esm.services.service.TagService;
import com.epam.esm.web.exceptions.WebLayerException;
import com.epam.esm.web.requesthandler.certificate.CertificateRequestParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class TagRequestHandler {

    private TagService service;
    private CertificateService certificateService;

    @Autowired
    public TagRequestHandler(TagService service, CertificateService certificateService) {
        this.service = service;
        this.certificateService = certificateService;
    }

    public Collection<Tag> find(Map<String, String> params) throws WebLayerException {
        Map<String, String> sorting = new HashMap<>();
        transfer(TagRequestParameters.SORT_BY_NAME, sorting,
                TagRequestParameters.SORT_BY_NAME, params);
        try {
            return service.find(params.get(CertificateRequestParameters.NAME), sorting);
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
