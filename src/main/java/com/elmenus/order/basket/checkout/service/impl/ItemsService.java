package com.elmenus.order.basket.checkout.service.impl;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.ItemDto;
import com.elmenus.order.basket.checkout.exception.InsufficientItemQuantityException;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.repository.ItemRepository;
import com.elmenus.order.basket.checkout.service.CrudService;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Service for handling CRUD operations of {@link Item}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemsService implements CrudService<Item, ItemDto> {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private final ItemRepository itemRepository;

    /**
     * Fetches all {@link Item} entities from database
     *
     * @return List of all {@link Item} entities
     */
    @Async
    public List<Item> getAll() {
        List<Item> itemsList = new ArrayList<>();
        Iterator<Item> items = itemRepository.findAll().iterator();
        items.forEachRemaining(itemsList::add);
        return itemsList;
    }

    /**
     * Fetches a specific {@link Item} by ID
     *
     * @param id The ID of the {@link Item}
     * @return The {@link Item} whose ID matches the specified ID, otherwise a {@link EntityNotFoundException} is thrown
     */
    @Async
    public Item get(@NonNull Integer id) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        return itemRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(Messages.ITEM_NOT_FOUND_ERROR, id);
                    log.warn(errorMessage);
                    throw new EntityNotFoundException(errorMessage);
                });
    }

    /**
     * Adds a new {@link Item} to the system
     *
     * @param ItemDto The {@link ItemDto} to be added
     * @return The ID of the new {@link Item}
     */
    @Async
    public Integer add(@NonNull ItemDto ItemDto) {
        Item Item = MODEL_MAPPER.map(ItemDto, Item.class);
        Item newItem = itemRepository.save(Item);

        log.info(Messages.ITEM_CREATED_MESSAGE);

        return newItem.getId();
    }

    /**
     * Updates an existing {@link Item} to the system if not exists, otherwise
     *
     * @param id      The ID of the {@link Item} to be updated
     * @param ItemDto The {@link ItemDto} to be updated
     */
    @Async
    public void update(@NonNull Integer id, @NonNull ItemDto ItemDto) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        Preconditions.checkNotNull(ItemDto, Messages.ITEM_NULL_ERROR);

        Item Item = itemRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format(Messages.ITEM_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        });

        MODEL_MAPPER.map(ItemDto, Item);
        itemRepository.save(Item);

        log.info(Messages.ITEM_UPDATED_MESSAGE);
    }

    /**
     * Deletes a specific {@link Item} by ID if exists otherwise a {@link EntityNotFoundException} is thrown
     */
    @Async
    public void delete(@NonNull Integer id) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);

        if (!exists(id)) {
            String errorMessage = String.format(Messages.ITEM_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        itemRepository.deleteById(id);

        log.info(Messages.ITEM_DELETED_MESSAGE);
    }

    /**
     * Decreases the quantity of an {@link Item} by the required amount if possible,
     * otherwise an {@link InsufficientItemQuantityException} is thrown
     * or an {@link EntityNotFoundException} is thrown if not found.
     *
     * @param id The ID of the {@link Item} to set its flag.
     */
    @SneakyThrows
    public void decreaseQuantity(@NonNull Integer id, float quantity) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);

        if (!exists(id)) {
            String errorMessage = String.format(Messages.ITEM_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        Item item = this.get(id);

        if (quantity > item.getQuantity()) {
            String errorMessage = String.format(Messages.INSUFFICIENT_ITEM_QUANTITY_ERROR, item.getName());
            log.error(errorMessage);
            throw new InsufficientItemQuantityException(errorMessage);
        }

        // Deduct the quantity in stock, and update it in database
        float newQuantity = item.getQuantity() - quantity;
        item.setQuantity(newQuantity);
        itemRepository.save(item);

        log.info(Messages.ITEM_QUANTITY_UPDATED_MESSAGE);
    }

    /**
     * Checks if specific {@link Item} with the specified ID exists or not
     *
     * @param id The ID of the {@link Item} to be checked
     * @return True if exists, false otherwise
     */
    private boolean exists(Integer id) {
        return itemRepository.existsById(id);
    }
}
