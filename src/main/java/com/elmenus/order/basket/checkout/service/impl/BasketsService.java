package com.elmenus.order.basket.checkout.service.impl;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.BasketDto;
import com.elmenus.order.basket.checkout.model.Basket;
import com.elmenus.order.basket.checkout.model.BasketContent;
import com.elmenus.order.basket.checkout.model.User;
import com.elmenus.order.basket.checkout.repository.BasketRepository;
import com.elmenus.order.basket.checkout.service.CrudService;
import com.elmenus.order.basket.checkout.validation.BasketCheckoutValidator;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Service for handling CRUD operations of {@link Basket}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BasketsService implements CrudService<Basket, BasketDto> {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private final BasketRepository basketRepository;
    private final UsersService usersService;
    private final ItemsService itemsService;
    private final BasketCheckoutValidator basketCheckoutValidator;

    /**
     * Fetches all {@link Basket} entities from database
     *
     * @return List of all {@link Basket} entities
     */
    @Override
    public List<Basket> getAll() {
        List<Basket> basketList = new ArrayList<>();
        Iterator<Basket> baskets = basketRepository.findAll().iterator();
        baskets.forEachRemaining(basketList::add);
        return basketList;
    }

    /**
     * Fetches a specific {@link Basket} by ID
     *
     * @param id The ID of the {@link Basket}
     * @return The {@link Basket} whose ID matches the specified ID, otherwise a {@link EntityNotFoundException} is thrown
     */
    @Override
    public Basket get(@NonNull Integer id) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        return basketRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(Messages.BASKET_NOT_FOUND_ERROR, id);
                    log.warn(errorMessage);
                    throw new EntityNotFoundException(errorMessage);
                });
    }

    /**
     * Adds a new {@link Basket} to the system
     *
     * @param basketDto The {@link BasketDto} to be added
     * @return The ID of the new {@link Basket}
     */
    @SneakyThrows
    @Override
    public Integer add(@NonNull BasketDto basketDto) {
        User user = usersService.get(basketDto.getUserId());
        Basket basket = MODEL_MAPPER.map(basketDto, Basket.class);
        basket.setUser(user);
        // Set creation date to now
        basket.setCreationDate(new Date());
        basket.setCheckedOut(false);

        Basket newBasket = basketRepository.save(basket);

        log.info(Messages.BASKET_CREATED_MESSAGE);
        return newBasket.getId();
    }

    /**
     * Updates an existing {@link Basket} to the system if not exists, otherwise
     *
     * @param id        The ID of the {@link Basket} to be updated
     * @param basketDto The {@link BasketDto} to be updated
     */
    @SneakyThrows
    @Override
    public void update(@NonNull Integer id, @NonNull BasketDto basketDto) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        Preconditions.checkNotNull(basketDto, Messages.BASKET_NULL_ERROR);

        User user = usersService.get(basketDto.getUserId());

        Basket basket = basketRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format(Messages.BASKET_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        });

        MODEL_MAPPER.map(basketDto, basket);
        basket.setUser(user);
        basketRepository.save(basket);

        log.info(Messages.BASKET_UPDATED_MESSAGE);
    }

    /**
     * Deletes a specific {@link Basket} by ID if exists otherwise a {@link EntityNotFoundException} is thrown
     */
    @Override
    public void delete(@NonNull Integer id) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);

        if (!exists(id)) {
            String errorMessage = String.format(Messages.BASKET_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        basketRepository.deleteById(id);

        log.info(Messages.BASKET_DELETED_MESSAGE);

    }

    /**
     * Checks out a specific {@link Basket} contents by ID if exists otherwise a {@link EntityNotFoundException} is thrown
     *
     * @param id The ID of the {@link Basket}
     * @return The {@link Basket} whose ID matches the specified ID, otherwise a {@link EntityNotFoundException} is thrown
     */
    @SneakyThrows
    public void checkout(@NonNull Integer id) {
        /*Basket requiredBasket = get(id).get();

        return CompletableFuture.runAsync(() -> {
            Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
            if (requiredBasket == null) {
                String errorMessage = String.format(Messages.BASKET_NOT_FOUND_ERROR, id);
                log.warn(errorMessage);
                throw new EntityNotFoundException(errorMessage);
            }

            // Make sure the basket is not checked out before
            if (requiredBasket.isCheckedOut()) {
                String errorMessage = String.format(Messages.BASKET_CHECKED_OUT_ALREADY_ERROR, id);
                log.warn(errorMessage);
                throw new IllegalStateException(errorMessage);
            }

            // Do all validations
            BasketCheckoutValidator.BasketCheckoutInfo info = basketCheckoutValidator.validateBasketBeforeCheckout(id);
            log.info(Messages.BASKET_CONTENT_VALIDATED_MESSAGE);

            finalizeBasketCheckout(id, info.getContents(), info.getTotalCost());
            log.info(Messages.BASKET_CHECKED_OUT_MESSAGE);
        });*/

        Basket requiredBasket = get(id);

        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        if (requiredBasket == null) {
            String errorMessage = String.format(Messages.BASKET_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        // Make sure the basket is not checked out before
        if (requiredBasket.isCheckedOut()) {
            String errorMessage = String.format(Messages.BASKET_CHECKED_OUT_ALREADY_ERROR, id);
            log.warn(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        // Do all validations
        BasketCheckoutValidator.BasketCheckoutInfo info = basketCheckoutValidator.validateBasketBeforeCheckout(id);
        log.info(Messages.BASKET_CONTENT_VALIDATED_MESSAGE);

        finalizeBasketCheckout(id, info.getContents(), info.getTotalCost());
        log.info(Messages.BASKET_CHECKED_OUT_MESSAGE);
    }

    @SneakyThrows
    @Transactional
    private void finalizeBasketCheckout(int basketId, Map<Integer, BasketContent> contents, float totalCost) {
        for (Map.Entry<Integer, BasketContent> basketContentEntry : contents.entrySet()) {
            // Update item quantity if possible
            itemsService.decreaseQuantity(basketContentEntry.getKey(), basketContentEntry.getValue().getQuantity());
        }

        // Update basket status to mark it as checked out
        Basket basket = get(basketId);
        basket.setCheckedOut(true);
        setBasketCheckedOut(basketId);

        // Call Credit card API
    }

    /**
     * Sets the Basket's checkedOut flag to TRUE of the {@link Basket} to mark
     * the {@link Basket} as checked out already before if exists, otherwise a {@link EntityNotFoundException} is thrown.
     * This prevents it from being checked out more than once
     *
     * @param id The ID of the {@link Basket} to set its flag.
     */
    @SneakyThrows
    private void setBasketCheckedOut(@NonNull Integer id) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        if (!exists(id)) {
            String errorMessage = String.format(Messages.BASKET_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        Basket basket = this.get(id);
        basket.setCheckedOut(true);
        basketRepository.save(basket);
    }

    /**
     * Checks if specific {@link Basket} with the specified ID exists or not
     *
     * @param id The ID of the {@link Basket} to be checked
     * @return True if exists, false otherwise
     */
    private boolean exists(Integer id) {
        return basketRepository.existsById(id);
    }
}
