package com.elmenus.order.basket.checkout.controller;

import com.elmenus.order.basket.checkout.constants.Constants;
import com.elmenus.order.basket.checkout.constants.SwaggerDocumentation;
import com.elmenus.order.basket.checkout.dto.ItemDto;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.service.impl.ItemsService;
import com.elmenus.order.basket.checkout.validation.ItemValidator;
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
 * REST controller for {@link Item}
 */
@RestController
@RequestMapping(ItemsController.PATH)
@Tag(name = "ItemsController", description = SwaggerDocumentation.ITEMS_CONTROLLER_SUMMARY)
@RequiredArgsConstructor
public class ItemsController {
    //region REST path
    public static final String PATH = "items";
    //endregion

    private final ItemsService itemsService;
    private final ItemValidator itemValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(itemValidator);
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.GET_ALL_ITEMS_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Item.class))})})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<List<Item>> getItems() {
        return Flux.just(itemsService.getAll());
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.GET_ITEM_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Item.class))})})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Item>> get(@PathVariable Integer id) {
        return Mono.just(ResponseEntity.ok(itemsService.get(id)));
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.ADD_ITEM_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_CREATED)})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> add(@Validated @RequestBody ItemDto itemDto, ServerHttpRequest request) {
        Integer userId = itemsService.add(itemDto);
        String userUri = StringUtils.appendIfMissing(request.getURI().toString(), Constants.SLASH) + userId;
        return Mono.just(ResponseEntity.created(URI.create(userUri)).build());
    }

    @Operation(summary = SwaggerDocumentation.UPDATE_ITEM_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK),
            @ApiResponse(responseCode = SwaggerDocumentation.HTTP_NOT_FOUND)})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> update(@PathVariable(name = Constants.ID_PARAMETER) Integer id,
                                             @RequestBody @Validated ItemDto itemDto) {
        itemsService.update(id, itemDto);
        return Mono.just(ResponseEntity.ok().build());
    }

    @Operation(summary = SwaggerDocumentation.DELETE_ITEM_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK),
            @ApiResponse(responseCode = SwaggerDocumentation.HTTP_NOT_FOUND)})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> delete(@PathVariable(name = Constants.ID_PARAMETER) Integer id) {
        itemsService.delete(id);
        return Mono.just(ResponseEntity.ok().build());
    }


}
