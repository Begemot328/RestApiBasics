package com.epam.esm.web.util;

import com.epam.esm.service.constants.PaginationParameters;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

/**
 * Paginator util class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class Paginator {
    private final static int DEFAULT_LIMIT = 20;
    private final int limit;
    private final int offset;

    /**
     * Constructor.
     *
     * @param params Parameters map to paginate.
     */
    public Paginator(MultiValueMap<String, String> params) {
        limit = getLimit(params);
        offset = getOffset(params);
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
    public int getOffset() {
        return offset;
    }

    /*
     * Check whether parameter map contains limit parameters.
     *
     * @return true if parameter map contains limit parameters.
     */
    public boolean isLimited(MultiValueMap<String, String> params) {
        return !CollectionUtils.isEmpty(params.get(PaginationParameters.LIMIT.getParameterName()));
    }

    private int getLimit(MultiValueMap<String, String> params) {
        return CollectionUtils.isEmpty(params.get(PaginationParameters.LIMIT.getParameterName()))
                ? DEFAULT_LIMIT
                : Integer.parseInt(
                        params.get(PaginationParameters.LIMIT.getParameterName()).get(0));
    }

    private int getOffset(MultiValueMap<String, String> params) {
        return CollectionUtils.isEmpty(params.get(PaginationParameters.OFFSET.getParameterName())) ?
                0 : Integer.parseInt(
                        params.get(PaginationParameters.OFFSET.getParameterName()).get(0));
    }

    /*
     * Returns parameters map with limit and offset parameters for next page.
     *
     * @return {@link MultiValueMap} parameters map for next page.
     */
    public MultiValueMap<String, String> nextPage(MultiValueMap<String, String> params) {
        params.put(PaginationParameters.OFFSET.getParameterName(),
                        Collections.singletonList(Integer.toString(offset + limit)));
        return params;
    }

    /*
     * Returns parameters map with limit and offset parameters for previous page.
     *
     * @return {@link MultiValueMap} parameters map for previous page.
     */
    public MultiValueMap<String, String> previousPage(MultiValueMap<String, String> params) {
        int newOffset = Math.max(offset - limit, 0);
        params.put(PaginationParameters.OFFSET.getParameterName(),
                Collections.singletonList(Integer.toString(newOffset)));
        return params;
    }
}
