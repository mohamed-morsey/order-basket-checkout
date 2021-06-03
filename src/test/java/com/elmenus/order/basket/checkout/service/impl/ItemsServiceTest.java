package com.elmenus.order.basket.checkout.service.impl;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.ItemDto;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Test class for {@link ItemsService}
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemsServiceTest {
    // region field values
    private static final int ID = 1;
    private static final String NAME = "Cheese";
    private static final float PRICE = 10F;
    private static final float QUANTITY = 100F;
    private static final float MODIFIED_QUANTITY = 200F;
    // endregion

    private ItemsService itemsService;

    @Mock
    private ItemRepository itemRepository;

    private Item item;
    private ItemDto itemDto;

    @Before
    public void setUp() throws Exception {
        itemsService = Mockito.spy(new ItemsService(itemRepository));

        item = new Item(ID, NAME, PRICE, QUANTITY);
        itemDto = new ItemDto(NAME, PRICE, QUANTITY);
    }

    /**
     * Test {@link ItemsService#getAll()}
     */
    @Test
    public void testGetAll() {
        // GIVEN
        Mockito.when(itemRepository.findAll()).thenReturn(Set.of(item));

        // WHEN
        List<Item> items = itemsService.getAll();

        // THEN
        Assert.assertNotNull(items);
        Assert.assertFalse(items.isEmpty());
        Assert.assertEquals(1, items.size());
    }

    /**
     * Tests {@link ItemsService#get(Integer)}
     */
    @Test
    public void testGet() {
        // GIVEN
        Mockito.when(itemRepository.findById(ID)).thenReturn(Optional.of(item));

        // WHEN
        Item returnedItem = itemsService.get(ID);

        // THEN
        Assert.assertNotNull(returnedItem);
        Assert.assertEquals(item, returnedItem);
    }

    /**
     * Tests {@link ItemsService#get(Integer)} but for a NULL ID
     */
    @Test
    public void testGetForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> itemsService.get(null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link ItemsService#get(Integer)} but for a a nonexistent {@link Item}
     */
    @Test
    public void testGetForNonexistentItem() {
        // GIVEN
        Mockito.when(itemRepository.findById(ID)).thenReturn(Optional.empty());

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> itemsService.get(ID));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.ITEM_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests {@link ItemsService#add(ItemDto)}
     */
    @Test
    public void testAdd() {
        // GIVEN
        Mockito.when(itemRepository.save(Mockito.any())).thenReturn(item);

        // WHEN
        Integer id = itemsService.add(itemDto);

        // THEN
        Assert.assertNotNull(id);
        Assert.assertEquals(ID, id.intValue());
    }

    /**
     * Tests {@link ItemsService#update(Integer, ItemDto)}
     */
    @Test
    public void testUpdate() {
        // GIVEN
        Mockito.when(itemRepository.findById(ID)).thenReturn(Optional.of(item));
        itemDto.setQuantity(MODIFIED_QUANTITY);

        // WHEN
        itemsService.update(ID, itemDto);

        // THEN
        Mockito.verify(itemRepository).save(item);
        Assert.assertEquals(MODIFIED_QUANTITY, item.getQuantity(), 0.01);
    }

    /**
     * Tests {@link ItemsService#update(Integer, ItemDto)} but for a NULL ID
     */
    @Test
    public void testUpdateForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> itemsService.update(null, itemDto));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link ItemsService#update(Integer, ItemDto)} but for a NULL {@link ItemDto}
     */
    @Test
    public void testUpdateForNullItemDto() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> itemsService.update(ID, null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ITEM_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link ItemsService#update(Integer, ItemDto)} but for a a nonexistent {@link Item}
     */
    @Test
    public void testUpdateForNonexistentItem() {
        // GIVEN
        Mockito.when(itemRepository.findById(ID)).thenReturn(Optional.empty());

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> itemsService.update(ID, itemDto));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.ITEM_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests {@link ItemsService#delete(Integer)}
     */
    @Test
    public void testDelete() {
        // GIVEN
        Mockito.when(itemRepository.existsById(ID)).thenReturn(true);

        // WHEN
        itemsService.delete(ID);

        // THEN
        Mockito.verify(itemRepository).deleteById(ID);
    }

    /**
     * Tests Tests {@link ItemsService#delete(Integer)} but for a NULL ID
     */
    @Test
    public void testDeleteForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> itemsService.delete(null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests Tests {@link ItemsService#delete(Integer)} but for a a nonexistent {@link Item}
     */
    @Test
    public void testDeleteForNonexistentItem() {
        // GIVEN
        Mockito.when(itemRepository.existsById(ID)).thenReturn(false);

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> itemsService.delete(ID));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.ITEM_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }
}