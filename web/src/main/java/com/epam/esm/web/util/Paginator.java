package com.epam.esm.web.util;

import com.epam.esm.service.constants.PaginationParameters;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

public class Paginator {
    private final static int DEFAULT_LIMIT = 20;
    private final int limit;
    private final int offset;

    public Paginator(MultiValueMap<String, String> params) {
        limit = getLimit(params);
        offset = getOffset(params);
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

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

    public MultiValueMap<String, String> nextPage(MultiValueMap<String, String> params) {
        params.put(PaginationParameters.OFFSET.getParameterName(),
                        Collections.singletonList(Integer.toString(offset + limit)));
        return params;
    }

    public MultiValueMap<String, String> previousPage(MultiValueMap<String, String> params) {
        int newOffset = Math.max(offset - limit, 0);
        params.put(PaginationParameters.OFFSET.getParameterName(),
                Collections.singletonList(Integer.toString(newOffset)));
        return params;
    }
}
