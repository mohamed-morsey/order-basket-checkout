package com.elmenus.order.basket.checkout.service.impl;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.BasketDto;
import com.elmenus.order.basket.checkout.model.Basket;
import com.elmenus.order.basket.checkout.model.BasketContent;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.model.User;
import com.elmenus.order.basket.checkout.repository.BasketRepository;
import com.elmenus.order.basket.checkout.validation.BasketCheckoutValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * Test class for {@link BasketsService}
 */
@RunWith(MockitoJUnitRunner.class)
public class BasketsServiceTest {
    // region field values
    private static final int ID = 1;
    private static final int USER_ID = 1;
    private static final String USER_FIRSTNAME = "John";
    private static final String USER_LASTNAME = "Smith";
    private static final String USER_USERNAME = "jsmith";
    private static final String USER_EMAIL = "smith@example.org";

    private static final int MODIFIED_USER_ID = 2;
    private static final String MODIFIED_USER_FIRSTNAME = "Test";
    private static final String MODIFIED_USER_LASTNAME = "User";
    private static final String MODIFIED_USER_USERNAME = "testuser";
    private static final String MODIFIED_USER_EMAIL = "test@example.org";

    private static final int ITEM_ID = 3;
    private static final String ITEM_NAME = "Cheese";
    private static final float ITEM_PRICE = 10F;
    private static final float ITEM_QUANTITY = 100F;
    private static final Date NOW = new Date();

    private static final float QUANTITY = 10F;
    private static final float TOTAL_COST = QUANTITY * ITEM_PRICE;
    // endregion

    private BasketsService basketsService;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private UsersService usersService;

    @Mock
    private ItemsService itemsService;

    @Mock
    private BasketCheckoutValidator basketCheckoutValidator;

    private Basket basket;
    private Item item;
    private BasketDto basketDto;
    private BasketContent basketContent;
    private User user;
    private User modifiedUser;

    @Before
    public void setUp() throws Exception {
        basketsService = Mockito.spy(new BasketsService(basketRepository, usersService, itemsService, basketCheckoutValidator));

        user = new User(USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_USERNAME, USER_EMAIL);
        modifiedUser = new User(MODIFIED_USER_ID, MODIFIED_USER_FIRSTNAME, MODIFIED_USER_LASTNAME,
                MODIFIED_USER_USERNAME, MODIFIED_USER_EMAIL);
        basket = new Basket(ID, user, NOW, false);
        item = new Item(ITEM_ID, ITEM_NAME, ITEM_PRICE, ITEM_QUANTITY);
        basketContent = new BasketContent(ID, basket, item, QUANTITY);
        basketDto = new BasketDto(USER_ID);
    }

    /**
     * Test {@link BasketsService#getAll()}
     */
    @Test
    public void testGetAll() {
        // GIVEN
        Mockito.when(basketRepository.findAll()).thenReturn(Set.of(basket));

        // WHEN
        List<Basket> baskets = basketsService.getAll();

        // THEN
        Assert.assertNotNull(baskets);
        Assert.assertFalse(baskets.isEmpty());
        Assert.assertEquals(1, baskets.size());
    }

    /**
     * Tests {@link BasketsService#get(Integer)}
     */
    @Test
    public void testGet() {
        // GIVEN
        Mockito.when(basketRepository.findById(ID)).thenReturn(Optional.of(basket));

        // WHEN
        Basket returnedBasket = basketsService.get(ID);

        // THEN
        Assert.assertNotNull(returnedBasket);
        Assert.assertEquals(basket, returnedBasket);
    }

    /**
     * Tests {@link BasketsService#get(Integer)} but for a NULL ID
     */
    @Test
    public void testGetForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> basketsService.get(null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link BasketsService#get(Integer)} but for a a nonexistent {@link Basket}
     */
    @Test
    public void testGetForNonexistentBasket() {
        // GIVEN
        Mockito.when(basketRepository.findById(ID)).thenReturn(Optional.empty());

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> basketsService.get(ID));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.BASKET_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests {@link BasketsService#add(BasketDto)}
     */
    @Test
    public void testAdd() {
        // GIVEN
        Mockito.when(basketRepository.save(Mockito.any())).thenReturn(basket);

        // WHEN
        Integer id = basketsService.add(basketDto);

        // THEN
        Assert.assertNotNull(id);
        Assert.assertEquals(ID, id.intValue());
    }

    /**
     * Tests {@link BasketsService#update(Integer, BasketDto)}
     */
    @Test
    public void testUpdate() {
        // GIVEN
        Mockito.when(basketRepository.findById(ID)).thenReturn(Optional.of(basket));
        Mockito.when(usersService.get(MODIFIED_USER_ID)).thenReturn(modifiedUser);
        basketDto.setUserId(MODIFIED_USER_ID);

        // WHEN
        basketsService.update(ID, basketDto);

        // THEN
        Mockito.verify(basketRepository).save(basket);
        Assert.assertEquals(MODIFIED_USER_ID, basket.getUser().getId().intValue());
    }

    /**
     * Tests {@link BasketsService#update(Integer, BasketDto)} but for a NULL ID
     */
    @Test
    public void testUpdateForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> basketsService.update(null, basketDto));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link BasketsService#update(Integer, BasketDto)} but for a NULL {@link BasketDto}
     */
    @Test
    public void testUpdateForNullBasketDto() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> basketsService.update(ID, null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.BASKET_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link BasketsService#update(Integer, BasketDto)} but for a a nonexistent {@link Basket}
     */
    @Test
    public void testUpdateForNonexistentBasket() {
        // GIVEN
        Mockito.when(basketRepository.findById(ID)).thenReturn(Optional.empty());

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> basketsService.update(ID, basketDto));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.BASKET_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests {@link BasketsService#delete(Integer)}
     */
    @Test
    public void testDelete() {
        // GIVEN
        Mockito.when(basketRepository.existsById(ID)).thenReturn(true);

        // WHEN
        basketsService.delete(ID);

        // THEN
        Mockito.verify(basketRepository).deleteById(ID);
    }

    /**
     * Tests Tests {@link BasketsService#delete(Integer)} but for a NULL ID
     */
    @Test
    public void testDeleteForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> basketsService.delete(null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests Tests {@link BasketsService#delete(Integer)} but for a a nonexistent {@link Basket}
     */
    @Test
    public void testDeleteForNonexistentBasket() {
        // GIVEN
        Mockito.when(basketRepository.existsById(ID)).thenReturn(false);

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> basketsService.delete(ID));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.BASKET_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests Tests {@link BasketsService#checkout(Integer)}
     */
    @Test
    public void testCheckout() {
        // GIVEN
        Mockito.when(basketRepository.existsById(ID)).thenReturn(true);
        BasketCheckoutValidator.BasketCheckoutInfo info = new BasketCheckoutValidator.BasketCheckoutInfo(Map.of(ID, basketContent), TOTAL_COST);
        Mockito.when(basketRepository.findById(ID)).thenReturn(Optional.of(basket));
        Mockito.when(basketCheckoutValidator.validateBasketBeforeCheckout(ID)).
                thenReturn(info);

        // WHEN
        basketsService.checkout(ID);

        // THEN

    }
}