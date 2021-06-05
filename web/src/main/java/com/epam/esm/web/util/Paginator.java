package com.epam.esm.web.util;

import com.epam.esm.service.constants.PaginationParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

/**
 * Paginator util class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Configuration
@PropertySource("classpath:pagination.properties")
public class Paginator {

    @Value("${limit.default}")
    private static int DEFAULT_LIMIT;
    private final int limit;
    private final int page;

    /**
     * Constructor.
     *
     * @param params Parameters map to paginate.
     */
    public Paginator(MultiValueMap<String, String> params) {
        limit = getLimit(params);
        page = getOffset(params);
    }

    public Paginator() {
        limit = DEFAULT_LIMIT;
        page = 0;
    }

    /*
     * Limit value getter.
     *
     * @return Limit value.
     */
    public int getLimit() {
        return limit;
    }

    /*
     * Offset value getter.
     *
     * @return Offset value.
     */
    public int getPage() {
        return page;
    }

    /*
     * Check whether parameter map contains limit parameters.
     *
     * @return true if parameter map contains limit parameters.
     */
    public boolean isNotLimited(MultiValueMap<String, String> params) {
        return CollectionUtils.isEmpty(params.get(PaginationParameters.LIMIT.getParameterName()));
    }

    private int getLimit(MultiValueMap<String, String> params) {
        return isNotLimited(params)
                ? DEFAULT_LIMIT :
                Integer.parseInt(
                params.get(PaginationParameters.LIMIT.getParameterName()).get(0));
    }

    private int getOffset(MultiValueMap<String, String> params) {
        return CollectionUtils.isEmpty(params.get(PaginationParameters.PAGE.getParameterName())) ?
                0 : Integer.parseInt(
                params.get(PaginationParameters.PAGE.getParameterName()).get(0));
    }

    /*
     * Returns parameters map with limit and offset parameters for next page.
     *
     * @param params Parameters to process.
     * @return {@link MultiValueMap} parameters map for next page.
     */
    public MultiValueMap<String, String> nextPage(MultiValueMap<String, String> params) {
        params.put(PaginationParameters.PAGE.getParameterName(),
                Collections.singletonList(Integer.toString(page + 1)));
        return params;
    }

    /*
     * Returns parameters map with limit and offset parameters for previous page.
     *
     * @param params Parameterc to process.
     * @return {@link MultiValueMap} parameters map for previous page.
     */
    public MultiValueMap<String, String> previousPage(MultiValueMap<String, String> params) {
        int newOffset = Math.max(page - 1, 0);
        params.put(PaginationParameters.PAGE.getParameterName(),
                Collections.singletonList(Integer.toString(newOffset)));
        return params;
    }

    /*
     * Returns parameters map with limit and offset parameters for previous page.
     *
     * @param params Parameters to process.
     * @return {@link MultiValueMap} parameters map for previous page.
     */
    public void setDefaultLimit(MultiValueMap<String, String> params) {
        params.put(PaginationParameters.LIMIT.getParameterName(),
                Collections.singletonList(Integer.toString(DEFAULT_LIMIT)));
    }

    /*
     * Set default limit value if it is empty.
     *
     * @param params Parameters to process.
     * @return {@link MultiValueMap} parameters map for previous page.
     */
    public void setDefaultLimitIfNotLimited(MultiValueMap<String, String> params) {
        if (isNotLimited(params)) {
            setDefaultLimit(params);
        }
    }
}
