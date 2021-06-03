package com.elmenus.order.basket.checkout.controller;

import com.elmenus.order.basket.checkout.constants.Constants;
import com.elmenus.order.basket.checkout.constants.SwaggerDocumentation;
import com.elmenus.order.basket.checkout.dto.BasketContentDto;
import com.elmenus.order.basket.checkout.model.BasketContent;
import com.elmenus.order.basket.checkout.service.impl.BasketContentsService;
import com.elmenus.order.basket.checkout.validation.BasketContentsValidator;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * REST controller for {@link BasketContent}
 */
@RestController
@RequestMapping(BasketContentsController.PATH)
@Tag(name = "BasketContentsController", description = SwaggerDocumentation.BASKET_CONTENTS_CONTROLLER_SUMMARY)
@RequiredArgsConstructor
public class BasketContentsController {
    //region REST path
    public static final String PATH = "basket-contents";
    //endregion

    private final BasketContentsService basketContentsService;
    private final BasketContentsValidator basketContentsValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(basketContentsValidator);
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.GET_ALL_BASKET_CONTENTS_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BasketContent.class))})})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<List<BasketContent>> getBaskets() {
        return Flux.just(basketContentsService.getAll());
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.GET_BASKET_CONTENTS_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BasketContent.class))})})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BasketContent>> get(@PathVariable Integer id) {
        return Mono.just(ResponseEntity.ok(basketContentsService.get(id)));
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.ADD_BASKET_CONTENTS_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_CREATED)})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> add(@Validated @RequestBody BasketContentDto basketContentDto, ServerHttpRequest request) {
        Integer basketContentId = basketContentsService.add(basketContentDto);
        String basketContentUri = StringUtils.appendIfMissing(request.getURI().toString(), Constants.SLASH) + basketContentId;
        return Mono.just(ResponseEntity.created(URI.create(basketContentUri)).build());
    }

    @Operation(summary = SwaggerDocumentation.UPDATE_BASKET_CONTENTS_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK),
            @ApiResponse(responseCode = SwaggerDocumentation.HTTP_NOT_FOUND)})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> update(@PathVariable(name = Constants.ID_PARAMETER) Integer id,
                                             @RequestBody @Validated BasketContentDto basketContentDto) {
        basketContentsService.update(id, basketContentDto);
        return Mono.just(ResponseEntity.ok().build());
    }

    @Operation(summary = SwaggerDocumentation.DELETE_BASKET_CONTENTS_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK),
            @ApiResponse(responseCode = SwaggerDocumentation.HTTP_NOT_FOUND)})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> delete(@PathVariable(name = Constants.ID_PARAMETER) Integer id) {
        basketContentsService.delete(id);
        return Mono.just(ResponseEntity.ok().build());
    }
}
