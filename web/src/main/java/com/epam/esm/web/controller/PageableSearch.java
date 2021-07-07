package com.epam.esm.web.controller;

import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.util.Paginator;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Interface for HATEOAS {@link java.lang.ModuleLayer.Controller} class, declares contract
 * to process pageable {@link CollectionModel}.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface PageableSearch {

    /**
     * Add next page and previous page links to {@link CollectionModel}.
     *
     * @param params Request parameters map.
     * @param collectionModel {@link CollectionModel} to add links to.
     * @param pageable Pagination parameters.
     */
    default void paginate(MultiValueMap<String, String> params,
                          CollectionModel collectionModel, Pageable pageable)
            throws NotFoundException, BadRequestException {
        Paginator paginator = getPaginator(pageable);

        collectionModel.add(linkTo(methodOn(this.getClass()).find(
                paginator.nextPageParams(params), pageable))
                .withRel("nextPage"));
        collectionModel.add(linkTo(methodOn(this.getClass()).find(
                paginator.previousPageParams(params), pageable))
                .withRel("previousPage"));
    }

    ResponseEntity<?> find(@RequestParam MultiValueMap<String, String> params, Pageable pageable)
            throws BadRequestException, NotFoundException;

     @Lookup
     default Paginator getPaginator(Pageable pageable) {
        return null;
    }
}
