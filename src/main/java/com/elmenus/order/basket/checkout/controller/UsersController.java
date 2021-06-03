package com.elmenus.order.basket.checkout.controller;

import com.elmenus.order.basket.checkout.constants.Constants;
import com.elmenus.order.basket.checkout.constants.SwaggerDocumentation;
import com.elmenus.order.basket.checkout.dto.UserDto;
import com.elmenus.order.basket.checkout.model.User;
import com.elmenus.order.basket.checkout.service.impl.UsersService;
import com.elmenus.order.basket.checkout.validation.UserValidator;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for {@link User}
 */
@RestController
@RequestMapping(UsersController.PATH)
@Tag(name = "UsersController", description = SwaggerDocumentation.USERS_CONTROLLER_SUMMARY)
@RequiredArgsConstructor
public class UsersController {
    //region REST path
    public static final String PATH = "users";
    //endregion

    private final UsersService usersService;
    private final UserValidator userValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.GET_ALL_USERS_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = User.class))})})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<List<User>> getUsers() {
        return Flux.just(usersService.getAll());
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.GET_USER_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK,
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = User.class))})})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<User>> get(@PathVariable Integer id) {
        //return ResponseEntity.ok(usersService.get(id, true));
        return Mono.just(ResponseEntity.ok(usersService.get(id)));
    }

    @SneakyThrows
    @Operation(summary = SwaggerDocumentation.ADD_USER_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_CREATED)})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> add(@Validated @RequestBody UserDto userDto, ServerHttpRequest request) {
        Integer userId = usersService.add(userDto);
        String userUri = StringUtils.appendIfMissing(request.getURI().toString(), Constants.SLASH) + userId;
        return Mono.just(ResponseEntity.created(URI.create(userUri)).build());
    }

    @Operation(summary = SwaggerDocumentation.UPDATE_USER_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK),
            @ApiResponse(responseCode = SwaggerDocumentation.HTTP_NOT_FOUND)})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> update(@PathVariable(name = Constants.ID_PARAMETER) Integer id,
                                       @RequestBody @Validated UserDto userDto) {
        usersService.update(id, userDto);
        return Mono.just(ResponseEntity.ok().build());
    }

    @Operation(summary = SwaggerDocumentation.DELETE_USER_SUMMARY)
    @ApiResponses(value = {@ApiResponse(responseCode = SwaggerDocumentation.HTTP_OK),
            @ApiResponse(responseCode = SwaggerDocumentation.HTTP_NOT_FOUND)})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> delete(@PathVariable(name = Constants.ID_PARAMETER) Integer id) {
        usersService.delete(id);
        return Mono.just(ResponseEntity.ok().build());
    }


}
