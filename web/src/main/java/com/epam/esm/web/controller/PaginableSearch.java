package com.epam.esm.web.controller;

import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.util.Paginator;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public interface PaginableSearch {
    default void paginate(MultiValueMap<String, String> params, CollectionModel collectionModel)
            throws NotFoundException, BadRequestException {
        Paginator paginator = new Paginator(params);

        paginator.setDefaultLimitIfNotLimited(params);

        collectionModel.add(linkTo(methodOn(this.getClass()).find(
                paginator.nextPage(params))).withRel("nextPage"));
        collectionModel.add(linkTo(methodOn(this.getClass()).find(
                paginator.previousPage(params))).withRel("previousPage"));
    }

    ResponseEntity<?> find(@RequestParam MultiValueMap<String, String> params)
            throws BadRequestException, NotFoundException;
}
