package com.elmenus.order.basket.checkout.service.impl;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.BasketContentDto;
import com.elmenus.order.basket.checkout.model.Basket;
import com.elmenus.order.basket.checkout.model.BasketContent;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.model.User;
import com.elmenus.order.basket.checkout.repository.BasketContentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Test class for {@link BasketContentsService}
 */
@RunWith(MockitoJUnitRunner.class)
public class BasketContentsServiceTest {
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

    private BasketContentsService basketContentsService;

    @Mock
    private BasketContentRepository basketContentRepository;

    @Mock
    private BasketsService basketsService;

    @Mock
    private ItemsService itemsService;

    private BasketContent basketContent;
    private BasketContentDto basketContentDto;
    private BasketContentDto modifiedBasketContentDto;
    private User user;
    private Basket basket;
    private Item item;

    @Before
    public void setUp() throws Exception {
        basketContentsService = Mockito.spy(new BasketContentsService(basketContentRepository, basketsService, itemsService));

        user = new User(USER_ID, USER_FIRSTNAME, USER_LASTNAME, USER_USERNAME, USER_EMAIL);
        basket = new Basket(BASKET_ID, user, NOW, false);
        item = new Item(ITEM_ID, ITEM_NAME, ITEM_PRICE, ITEM_QUANTITY);
        basketContent = new BasketContent(ID, basket, item, QUANTITY);
        basketContentDto = new BasketContentDto(BASKET_ID, ITEM_ID, QUANTITY);
        modifiedBasketContentDto = new BasketContentDto(BASKET_ID, ITEM_ID, MODIFIED_QUANTITY);
    }

    /**
     * Test {@link BasketContentsService#getAll()}
     */
    @Test
    public void testGetAll() {
        // GIVEN
        Mockito.when(basketContentRepository.findAll()).thenReturn(Set.of(basketContent));

        // WHEN
        List<BasketContent> baskets = basketContentsService.getAll();

        // THEN
        Assert.assertNotNull(baskets);
        Assert.assertFalse(baskets.isEmpty());
        Assert.assertEquals(1, baskets.size());
    }

    /**
     * Tests {@link BasketContentsService#get(Integer)}
     */
    @Test
    public void testGet() {
        // GIVEN
        Mockito.when(basketContentRepository.findById(ID)).thenReturn(Optional.of(basketContent));

        // WHEN
        BasketContent returnedBasketContent = basketContentsService.get(ID);

        // THEN
        Assert.assertNotNull(returnedBasketContent);
        Assert.assertEquals(basketContent, returnedBasketContent);
    }

    /**
     * Tests {@link BasketContentsService#get(Integer)} but for a NULL ID
     */
    @Test
    public void testGetForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> basketContentsService.get(null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link BasketContentsService#get(Integer)} but for a a nonexistent {@link BasketContent}
     */
    @Test
    public void testGetForNonexistentBasketContent() {
        // GIVEN
        Mockito.when(basketContentRepository.findById(ID)).thenReturn(Optional.empty());

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> basketContentsService.get(ID));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.BASKET_CONTENT_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests {@link BasketContentsService#add(BasketContentDto)}
     */
    @Test
    public void testAdd() {
        // GIVEN
        Mockito.when(basketContentRepository.save(Mockito.any())).thenReturn(basketContent);

        // WHEN
        Integer id = basketContentsService.add(basketContentDto);

        // THEN
        Assert.assertNotNull(id);
        Assert.assertEquals(ID, id.intValue());
    }

    /**
     * Tests {@link BasketContentsService#update(Integer, BasketContentDto)}
     */
    @Test
    public void testUpdate() {
        // GIVEN
        Mockito.when(basketContentRepository.findById(ID)).thenReturn(Optional.of(basketContent));

        // WHEN
        basketContentsService.update(ID, modifiedBasketContentDto);

        // THEN
        Mockito.verify(basketContentRepository).save(basketContent);
        Assert.assertEquals(MODIFIED_QUANTITY, basketContent.getQuantity(), 0.01F);
    }

    /**
     * Tests {@link BasketContentsService#update(Integer, BasketContentDto)} but for a NULL ID
     */
    @Test
    public void testUpdateForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> basketContentsService.update(null, basketContentDto));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link BasketContentsService#update(Integer, BasketContentDto)} but for a NULL {@link BasketContentDto}
     */
    @Test
    public void testUpdateForNullBasketContentDto() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> basketContentsService.update(ID, null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.BASKET_CONTENT_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link BasketContentsService#update(Integer, BasketContentDto)} but for a a nonexistent {@link BasketContent}
     */
    @Test
    public void testUpdateForNonexistentBasketContent() {
        // GIVEN
        Mockito.when(basketContentRepository.findById(ID)).thenReturn(Optional.empty());

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> basketContentsService.update(ID, basketContentDto));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.BASKET_CONTENT_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests {@link BasketContentsService#delete(Integer)}
     */
    @Test
    public void testDelete() {
        // GIVEN
        Mockito.when(basketContentRepository.existsById(ID)).thenReturn(true);

        // WHEN
        basketContentsService.delete(ID);

        // THEN
        Mockito.verify(basketContentRepository).deleteById(ID);
    }

    /**
     * Tests Tests {@link BasketContentsService#delete(Integer)} but for a NULL ID
     */
    @Test
    public void testDeleteForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> basketContentsService.delete(null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests Tests {@link BasketContentsService#delete(Integer)} but for a a nonexistent {@link BasketContent}
     */
    @Test
    public void testDeleteForNonexistentBasketContent() {
        // GIVEN
        Mockito.when(basketContentRepository.existsById(ID)).thenReturn(false);

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> basketContentsService.delete(ID));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.BASKET_CONTENT_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }
}