package com.elmenus.order.basket.checkout.controller.integration;

import com.elmenus.order.basket.checkout.constants.Constants;
import com.elmenus.order.basket.checkout.controller.BasketContentsController;
import com.elmenus.order.basket.checkout.controller.BasketsController;
import com.elmenus.order.basket.checkout.controller.ItemsController;
import com.elmenus.order.basket.checkout.controller.UsersController;
import com.elmenus.order.basket.checkout.dto.BasketContentDto;
import com.elmenus.order.basket.checkout.dto.BasketDto;
import com.elmenus.order.basket.checkout.dto.ItemDto;
import com.elmenus.order.basket.checkout.dto.UserDto;
import com.elmenus.order.basket.checkout.model.Basket;
import com.elmenus.order.basket.checkout.model.BasketContent;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.model.User;
import com.elmenus.order.basket.checkout.repository.BasketContentRepository;
import com.elmenus.order.basket.checkout.repository.BasketRepository;
import com.elmenus.order.basket.checkout.repository.ItemRepository;
import com.elmenus.order.basket.checkout.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Integration test class for {@link BasketsController}
 * that tests the whole basket checkout scenario
 **/
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasketControllerIT {

    // region field values
    private static final int USER_ID = 1;
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Smith";
    private static final String USERNAME = "jsmith";
    private static final String EMAIL = "smith@example.org";

    private static final int ITEM_ID = 1;
    private static final String NAME = "Cheese";
    private static final float PRICE = 10F;
    private static final float QUANTITY = 100F;

    private static final int BASKET_ID = 1;
    private static final Date NOW = new Date();

    private static final int BASKET_CONTENT_ID = 1;
    // endregion

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketContentRepository basketContentRepository;

    @Autowired
    private WebTestClient webTestClient;

    private UriComponentsBuilder builder;

    private User user;
    private UserDto userDto;
    private Item item;
    private ItemDto itemDto;
    private Basket basket;
    private BasketDto basketDto;
    private BasketContent basketContent;
    private BasketContentDto basketContentDto;

    @Before
    public void setUp() throws Exception {
        user = new User(USER_ID, FIRSTNAME, LASTNAME, USERNAME, EMAIL);
        userDto = new UserDto(FIRSTNAME, LASTNAME, USERNAME, EMAIL);

        item = new Item(ITEM_ID, NAME, PRICE, QUANTITY);
        itemDto = new ItemDto(NAME, PRICE, QUANTITY);

        basket = new Basket(BASKET_ID, user, NOW, false);
        basketDto = new BasketDto(USER_ID);

        basketContentDto = new BasketContentDto(BASKET_ID, ITEM_ID, QUANTITY);
        basketContent = new BasketContent(BASKET_CONTENT_ID, basket, item, QUANTITY);
    }

    @After
    public void teardown() {
        // Cleat all repositories
        basketContentRepository.deleteAll();
        basketRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    /**
     * Tests the following scenario:
     * <ul>
     * <li>Create a user</li>
     * <li>Create an item</li>
     * <li>Create a basket</li>
     * <li>Create a basket content</li>
     * <li>Checkout basket content</li>
     * </ul>
     */
    @Test
    public void testCreateAndCheckoutBasket() throws IOException {
        // Add user
        webTestClient.post()
                .uri(Constants.SLASH + UsersController.PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isCreated();

        // Check that the user has been added
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        Assert.assertEquals(1, users.size());

        // Add item
        webTestClient.post()
                .uri(Constants.SLASH + ItemsController.PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(itemDto)
                .exchange()
                .expectStatus().isCreated();

        // Check that the item has been added
        List<Item> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        Assert.assertEquals(1, items.size());

        // Add basket
        webTestClient.post()
                .uri(Constants.SLASH + BasketsController.PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(basketDto)
                .exchange()
                .expectStatus().isCreated();

        // Check that the basket has been added
        List<Basket> baskets = new ArrayList<>();
        basketRepository.findAll().forEach(baskets::add);
        Assert.assertEquals(1, baskets.size());

        // Add basket contents
        webTestClient.post()
                .uri(Constants.SLASH + BasketContentsController.PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(basketContentDto)
                .exchange()
                .expectStatus().isCreated();

        // Check that the basket has been added
        List<BasketContent> basketContents = new ArrayList<>();
        basketContentRepository.findAll().forEach(basketContents::add);
        Assert.assertEquals(1, basketContents.size());

        // Do basket checkout
        webTestClient.post()
                .uri(Constants.SLASH + BasketsController.PATH +
                        Constants.SLASH + BasketsController.CHECKOUT_PATH + Constants.SLASH + BASKET_CONTENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        // Check that the basket's checked-out flag is TRUE
        baskets = new ArrayList<>();
        basketRepository.findAll().forEach(baskets::add);
        Assert.assertEquals(1, baskets.size());
        Assert.assertEquals(true, baskets.get(0).isCheckedOut());
    }
}