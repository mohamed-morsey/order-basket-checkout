package com.elmenus.order.basket.checkout.controller;

import com.elmenus.order.basket.checkout.config.H2;
import com.elmenus.order.basket.checkout.constants.Constants;
import com.elmenus.order.basket.checkout.dto.BasketContentDto;
import com.elmenus.order.basket.checkout.model.Basket;
import com.elmenus.order.basket.checkout.model.BasketContent;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.model.User;
import com.elmenus.order.basket.checkout.service.impl.BasketContentsService;
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

import java.util.Date;
import java.util.List;

/**
 * Test class for {@link BasketsController}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class BasketContentsControllerTest {
    // region field values
    private static final int ID = 1;
    private static final float QUANTITY = 10F;
    private static final float MODIFIED_QUANTITY = 20F;
    private static final int USER_ID = 1;
    private static final String USER_FIRSTNAME = "John";
    private static final String USER_LASTNAME = "Smith";
    private static final String USER_USERNAME = "jsmith";
    private static final String USER_EMAIL = "smith@example.org";

    private static final int BASKET_ID = 2;
    private static final int ITEM_ID = 3;
    private static final String ITEM_NAME = "Cheese";
    private static final float ITEM_PRICE = 10F;
    private static final float ITEM_QUANTITY = 100F;
    private static final Date NOW = new Date();
    // endregion

    private static final String BASKET_CONTENTS_CONTROLLER_PATH = "/" + BasketContentsController.PATH;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private H2 h2;

    @MockBean
    private BasketContentsService basketContentsService;

    private Item item;
    private User user;
    private Basket basket;
    private BasketContent basketContent;
    private BasketContentDto basketContentDto;


    @Before
    public void setUp() throws Exception {
        user = new User(USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_USERNAME, USER_EMAIL);
        basket = new Basket(BASKET_ID, user, NOW, false);
        basketContentDto = new BasketContentDto(BASKET_ID, ITEM_ID, QUANTITY);
        basketContent = new BasketContent(ID, basket, item, QUANTITY);
        item = new Item(ITEM_ID, ITEM_NAME, ITEM_PRICE, ITEM_QUANTITY);
        basketContentDto = new BasketContentDto(BASKET_ID, ITEM_ID, QUANTITY);
    }

    @After
    public void close() throws Exception {
        h2.stop();
    }

    /**
     * Test {@link BasketContentsController#getBaskets()}
     */
    @Test
    public void testGetBaskets() throws Exception {
        // GIVEN
        Mockito.when(basketContentsService.getAll()).thenReturn(List.of(basketContent));

        // WHEN
        webTestClient.get()
                .uri(BASKET_CONTENTS_CONTROLLER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                })
                .jsonPath("$[0]." + Constants.ID_FIELD, Matchers.equalTo(ID));
    }

    /**
     * Test {@link BasketContentsController#get(Integer)}
     */
    @Test
    public void testGet() {
        // GIVEN
        Mockito.when(basketContentsService.get(ID)).thenReturn(basketContent);

        // WHEN
        webTestClient.get()
                .uri(BASKET_CONTENTS_CONTROLLER_PATH + Constants.SLASH + ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                })
                .jsonPath("$." + Constants.ID_FIELD, Matchers.equalTo(ID));
    }

    /**
     * Tests {@link BasketContentsController#add(BasketContentDto, ServerHttpRequest)}
     */
    @Test
    public void testAdd() {
        // GIVEN
        Mockito.when(basketContentsService.get(ID)).thenReturn(basketContent);

        // WHEN
        webTestClient.post()
                .uri(BASKET_CONTENTS_CONTROLLER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(basketContentDto)
                .exchange()
                .expectStatus().isCreated();
    }

    /**
     * Tests {@link BasketContentsController#update(Integer, BasketContentDto)}
     */
    @Test
    public void testUpdate() {
        // GIVEN
        Mockito.when(basketContentsService.get(ID)).thenReturn(basketContent);

        // WHEN
        webTestClient.put()
                .uri(BASKET_CONTENTS_CONTROLLER_PATH + Constants.SLASH + ID)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(basketContentDto)
                .exchange()
                .expectStatus().isOk();
    }

    /**
     * Tests {@link BasketContentsController#delete(Integer)}
     */
    @Test
    public void testDelete() {
        // GIVEN
        Mockito.when(basketContentsService.get(ID)).thenReturn(basketContent);

        // WHEN
        webTestClient.delete()
                .uri(BASKET_CONTENTS_CONTROLLER_PATH + Constants.SLASH + ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

}