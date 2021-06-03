package com.elmenus.order.basket.checkout.service.impl;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.BasketContentDto;
import com.elmenus.order.basket.checkout.model.Basket;
import com.elmenus.order.basket.checkout.model.BasketContent;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.repository.BasketContentRepository;
import com.elmenus.order.basket.checkout.service.CrudService;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Service for handling CRUD operations of {@link BasketContent}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BasketContentsService implements CrudService<BasketContent, BasketContentDto> {
    private final BasketContentRepository basketContentRepository;
    private final BasketsService basketsService;
    private final ItemsService itemsService;

    /**
     * Fetches all {@link BasketContent} entities from database
     *
     * @return List of all {@link BasketContent} entities
     */
    @Override
    public List<BasketContent> getAll() {
        List<BasketContent> basketContentList = new ArrayList<>();
        Iterator<BasketContent> basketContents = basketContentRepository.findAll().iterator();
        basketContents.forEachRemaining(basketContentList::add);
        return basketContentList;
    }

    /**
     * Fetches a specific {@link BasketContent} by ID
     *
     * @param id The ID of the {@link BasketContent}
     * @return The {@link BasketContent} whose ID matches the specified ID, otherwise a {@link EntityNotFoundException} is thrown
     */
    @Override
    public BasketContent get(@NonNull Integer id) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        return basketContentRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(Messages.BASKET_CONTENT_NOT_FOUND_ERROR, id);
                    log.warn(errorMessage);
                    throw new EntityNotFoundException(errorMessage);
                });
    }

    /**
     * Adds a new {@link BasketContent} to the system
     *
     * @param basketContentDto The {@link BasketContentDto} to be added
     * @return The ID of the new {@link BasketContent}
     */
    @SneakyThrows
    @Override
    public Integer add(@NonNull BasketContentDto basketContentDto) {
//        BasketContent basketContent = MODEL_MAPPER.map(basketContentDto, BasketContent.class);
        Basket basket = basketsService.get(basketContentDto.getBasketId());
        Item item = itemsService.get(basketContentDto.getItemId());
        BasketContent basketContent = new BasketContent();
        basketContent.setQuantity(basketContentDto.getQuantity());
        basketContent.setBasket(basket);
        basketContent.setItem(item);
        BasketContent newBasketContent = basketContentRepository.save(basketContent);

        log.info(Messages.BASKET_CONTENT_CREATED_MESSAGE);

        return newBasketContent.getId();
    }

    /**
     * Updates an existing {@link BasketContent} to the system if not exists, otherwise
     *
     * @param id               The ID of the {@link BasketContent} to be updated
     * @param basketContentDto The {@link BasketContentDto} to be updated
     */
    @SneakyThrows
    @Override
    public void update(@NonNull Integer id, @NonNull BasketContentDto basketContentDto) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        Preconditions.checkNotNull(basketContentDto, Messages.BASKET_CONTENT_NULL_ERROR);

        Basket basket = basketsService.get(basketContentDto.getBasketId());
        Item item = itemsService.get(basketContentDto.getItemId());

        BasketContent basketContent = basketContentRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format(Messages.BASKET_CONTENT_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        });

        basketContent.setQuantity(basketContentDto.getQuantity());
        basketContent.setBasket(basket);
        basketContent.setItem(item);
        basketContentRepository.save(basketContent);

        log.info(Messages.BASKET_CONTENT_UPDATED_MESSAGE);
    }

    /**
     * Deletes a specific {@link BasketContent} by ID if exists otherwise a {@link EntityNotFoundException} is thrown
     */
    @Override
    public void delete(@NonNull Integer id) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);

        if (!exists(id)) {
            String errorMessage = String.format(Messages.BASKET_CONTENT_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        basketContentRepository.deleteById(id);

        log.info(Messages.BASKET_CONTENT_DELETED_MESSAGE);
    }

    /**
     * Checks if specific {@link BasketContent} with the specified ID exists or not
     *
     * @param id The ID of the {@link BasketContent} to be checked
     * @return True if exists, false otherwise
     */
    private boolean exists(Integer id) {
        return basketContentRepository.existsById(id);
    }

    public List<BasketContent> getByBasketId(Integer basketId) {
        return basketContentRepository.findByBasketId(basketId);
    }
}
