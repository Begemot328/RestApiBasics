package com.epam.esm.web.util;

import com.epam.esm.service.constants.PageableParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * Paginator util class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
@Scope("prototype")
@PropertySource("classpath:application.properties")
public class Paginator {

    @Value("${spring.data.web.pageable.one-indexed-parameters}")
    private boolean isStartedFromOne;
    private int addition = 0;

    private Pageable pageable;

    /**
     * Constructor.
     *
     * @param pageable Parameters to paginate.
     */
    public Paginator(Pageable pageable) {
        this.pageable = pageable;
    }

    /**
     * Constructor for Spring bean creation.
     */
    public Paginator() {
        //Empty constructor for Spring bean creation.
    }

    /*
     * Returns parameters map with limit and offset parameters for next page.
     *
     * @param params Parameters to process.
     * @return {@link MultiValueMap} parameters map for next page.
     */
    public MultiValueMap<String, String> nextPageParams(MultiValueMap<String, String> params) {
        MultiValueMap<String, String> resultParams = new LinkedMultiValueMap<>();
        resultParams.addAll(params);
        resultParams.put(PageableParameters.PAGE.getParameterName(),
                Collections.singletonList(Integer.toString(
                        pageable.next().getPageNumber() + addition)));
        return resultParams;
    }

    /*
     * Returns parameters map with limit and offset parameters for previous page.
     *
     * @param params Parameterc to process.
     * @return {@link MultiValueMap} parameters map for previous page.
     */
    public MultiValueMap<String, String> previousPageParams(MultiValueMap<String, String> params) {
        MultiValueMap<String, String> resultParams = new LinkedMultiValueMap<>();
        resultParams.addAll(params);
        resultParams.put(PageableParameters.PAGE.getParameterName(),
                Collections.singletonList(Integer.toString(
                        pageable.previousOrFirst().getPageNumber() + addition)));
        return resultParams;
    }

    @PostConstruct
    private void calculateAddition() {
        if (isStartedFromOne) {
            addition = 1;
        }
    }
}
