package com.elmenus.order.basket.checkout.controller;

import com.elmenus.order.basket.checkout.constants.Constants;
import com.elmenus.order.basket.checkout.constants.SwaggerDocumentation;
import com.elmenus.order.basket.checkout.dto.BasketDto;
import com.elmenus.order.basket.checkout.model.Basket;
import com.elmenus.order.basket.checkout.service.impl.BasketsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * REST controller for {@link Basket}
 */
@RestController
@RequestMapping(BasketsController.PATH)
@Tag(name = "BasketsController", description = SwaggerDocumentation.BASKET_CONTENTS_CONTROLLER_SUMMARY)
@RequiredArgsConstructor
public class BasketsController {
    //region REST path
    public static final String PATH = "baskets";
    public static final String CHECKOUT_PATH = "checkout";
    //endregion

    private final BasketsService basketsService;

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.GET_ALL_BASKETS_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Basket.class))})})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<List<Basket>> getBaskets() {
        return Flux.just(basketsService.getAll());
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.GET_BASKET_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Basket.class))})})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Basket>> get(@PathVariable Integer id) {
        return Mono.just(ResponseEntity.ok(basketsService.get(id)));
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.ADD_BASKET_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_CREATED)})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> add(@Validated @RequestBody BasketDto basketDto, ServerHttpRequest request) {
        Integer basketId = basketsService.add(basketDto);
        String basketUri = StringUtils.appendIfMissing(request.getURI().toString(), Constants.SLASH) + basketId;
        return Mono.just(ResponseEntity.created(URI.create(basketUri)).build());
    }

    @Operation(summary = SwaggerDocumentation.UPDATE_BASKET_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK),
            @ApiResponse(responseCode = SwaggerDocumentation.HTTP_NOT_FOUND)})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> update(@PathVariable(name = Constants.ID_PARAMETER) Integer id,
                                             @RequestBody @Validated BasketDto basketDto) {
        basketsService.update(id, basketDto);
        return Mono.just(ResponseEntity.ok().build());
    }

    @Operation(summary = SwaggerDocumentation.DELETE_BASKET_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK),
            @ApiResponse(responseCode = SwaggerDocumentation.HTTP_NOT_FOUND)})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> delete(@PathVariable(name = Constants.ID_PARAMETER) Integer id) {
        basketsService.delete(id);
        return Mono.just(ResponseEntity.ok().build());
    }

    @Operation(summary = SwaggerDocumentation.CHECKOUT_BASKET_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK),
            @ApiResponse(responseCode = SwaggerDocumentation.HTTP_NOT_FOUND)})
    @PostMapping(value = CHECKOUT_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> checkout(@PathVariable(name = Constants.ID_PARAMETER) Integer id) {
        basketsService.checkout(id);
        return Mono.just(ResponseEntity.ok().build());
    }

}
