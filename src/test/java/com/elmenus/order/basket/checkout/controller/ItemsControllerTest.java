package com.elmenus.order.basket.checkout.controller;

import com.elmenus.order.basket.checkout.config.H2;
import com.elmenus.order.basket.checkout.constants.Constants;
import com.elmenus.order.basket.checkout.dto.ItemDto;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.service.impl.ItemsService;
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
 * Test class for {@link ItemsController}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class ItemsControllerTest {
    // region field values
    private static final int ID = 1;
    private static final String NAME = "Cheese";
    private static final float PRICE = 10F;
    private static final float QUANTITY = 100F;
    private static final float MODIFIED_QUANTITY = 200F;
    // endregion

    private static final String ITEMS_CONTROLLER_PATH = "/" + ItemsController.PATH;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ItemsService itemsService;

    @Autowired
    private H2 h2;

    private Item item;
    private ItemDto itemDto;

    @Before
    public void setUp() throws Exception {
        item = new Item(ID, NAME, PRICE, QUANTITY);
        itemDto = new ItemDto(NAME, PRICE, QUANTITY);
    }

    @After
    public void close() throws Exception {
        h2.stop();
    }

    /**
     * Test {@link ItemsController#getItems()}
     */
    @Test
    public void testGetItems() throws Exception {
        // GIVEN
        Mockito.when(itemsService.getAll()).thenReturn(List.of(item));

        // WHEN
        webTestClient.get()
                .uri(ITEMS_CONTROLLER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                })
                .jsonPath("$[0]." + Constants.NAME_FIELD, Matchers.equalTo(NAME));
    }

    /**
     * Test {@link ItemsController#get(Integer)}
     */
    @Test
    public void testGet() {
        // GIVEN
        Mockito.when(itemsService.get(ID)).thenReturn(item);

        // WHEN
        webTestClient.get()
                .uri(ITEMS_CONTROLLER_PATH + Constants.SLASH + ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                })
                .jsonPath("$." + Constants.NAME_FIELD, Matchers.equalTo(NAME));
    }

    /**
     * Tests {@link ItemsController#add(ItemDto, ServerHttpRequest)}
     */
    @Test
    public void testAdd() {
        // GIVEN
        Mockito.when(itemsService.get(ID)).thenReturn(item);

        // WHEN
        webTestClient.post()
                .uri(ITEMS_CONTROLLER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(itemDto)
                .exchange()
                .expectStatus().isCreated();
    }

    /**
     * Tests {@link ItemsController#update(Integer, ItemDto)}
     */
    @Test
    public void testUpdate() {
        // GIVEN
        Mockito.when(itemsService.get(ID)).thenReturn(item);

        // WHEN
        webTestClient.put()
                .uri(ITEMS_CONTROLLER_PATH + Constants.SLASH + ID)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(itemDto)
                .exchange()
                .expectStatus().isOk();
    }

    /**
     * Tests {@link ItemsController#delete(Integer)}
     */
    @Test
    public void testDelete() {
        // GIVEN
        Mockito.when(itemsService.get(ID)).thenReturn(item);

        // WHEN
        webTestClient.delete()
                .uri(ITEMS_CONTROLLER_PATH + Constants.SLASH + ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}