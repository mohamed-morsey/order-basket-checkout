package com.elmenus.order.basket.checkout.controller;

import com.elmenus.order.basket.checkout.config.H2;
import com.elmenus.order.basket.checkout.constants.Constants;
import com.elmenus.order.basket.checkout.dto.UserDto;
import com.elmenus.order.basket.checkout.model.User;
import com.elmenus.order.basket.checkout.service.impl.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

/**
 * Test class for {@link UsersController}
 */
//@WebFluxTest(UsersControllerTest.class)
//@RunWith(SpringRunner.class)
//@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class UsersControllerTest {
    // region field values
    private static final int ID = 1;
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Smith";
    private static final String USERNAME = "jsmith";
    private static final String EMAIL = "smith@example.org";
    // endregion

    private static final String USERS_CONTROLLER_PATH = "/" + UsersController.PATH;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private H2 h2;


    @MockBean
    private UsersService usersService;

    private User user;
    private UserDto userDto;

    @Before
    public void setUp() throws Exception {
        user = new User(ID, FIRSTNAME, LASTNAME, USERNAME, EMAIL);
        userDto = new UserDto(FIRSTNAME, LASTNAME, USERNAME, EMAIL);
    }

    @After
    public void close() throws Exception {
        h2.stop();
    }

    /**
     * Test {@link UsersController#getUsers()}
     */
    @Test
    public void testGetUsers() throws Exception {
        // GIVEN
        Mockito.when(usersService.getAll()).thenReturn(List.of(user));

        // WHEN
        webTestClient.get()
                .uri(USERS_CONTROLLER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                })
                .jsonPath("$[0]." + Constants.FIRSTNAME_FIELD, Matchers.equalTo(FIRSTNAME));
    }

    /**
     * Test {@link UsersController#get(Integer)}
     */
    @Test
    public void testGet() {
        // GIVEN
        Mockito.when(usersService.get(ID)).thenReturn(user);

        // WHEN
        webTestClient.get()
                .uri(USERS_CONTROLLER_PATH + Constants.SLASH + ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                })
                .jsonPath("$." + Constants.FIRSTNAME_FIELD, Matchers.equalTo(FIRSTNAME));
    }

    /**
     * Tests {@link UsersController#add(UserDto, ServerHttpRequest)}
     */
    @Test
    public void testAdd() {
        // GIVEN
        Mockito.when(usersService.get(ID)).thenReturn(user);

        // WHEN
        webTestClient.post()
                .uri(USERS_CONTROLLER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isCreated();
    }

    /**
     * Tests {@link UsersController#update(Integer, UserDto)}
     */
    @Test
    public void testUpdate() {
        // GIVEN
        Mockito.when(usersService.get(ID)).thenReturn(user);

        // WHEN
        webTestClient.put()
                .uri(USERS_CONTROLLER_PATH + Constants.SLASH + ID)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isOk();
    }

    /**
     * Tests {@link UsersController#delete(Integer)}
     */
    @Test
    public void testDelete() {
        // GIVEN
        Mockito.when(usersService.get(ID)).thenReturn(user);

        // WHEN
        webTestClient.delete()
                .uri(USERS_CONTROLLER_PATH + Constants.SLASH + ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}