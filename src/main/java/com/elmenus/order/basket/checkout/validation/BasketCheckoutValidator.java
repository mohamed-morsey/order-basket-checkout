package com.elmenus.order.basket.checkout.validation;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.exception.InsufficientItemQuantityException;
import com.elmenus.order.basket.checkout.exception.MoneyValueException;
import com.elmenus.order.basket.checkout.model.Basket;
import com.elmenus.order.basket.checkout.model.BasketContent;
import com.elmenus.order.basket.checkout.model.Item;
import com.elmenus.order.basket.checkout.repository.BasketContentRepository;
import com.elmenus.order.basket.checkout.service.impl.BasketsService;
import com.elmenus.order.basket.checkout.service.impl.ItemsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a basket checkout validation service to validate that everything is correct, e.g. item
 * availability, before the checkout operation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BasketCheckoutValidator {
    private final BasketContentRepository basketContentRepository;
    private final ItemsService itemsService;

    /**
     * Validates the contents of {@link Basket} with the given ID.
     * This validator is independent of basket logic which enables adding extra checks without
     * impacting the {@link BasketsService}
     *
     * @param basketId the ID of {@link Basket}
     * @return {@link BasketCheckoutInfo} with a {@link Map} of {@link BasketContent} and total cost if successful
     */
    public BasketCheckoutInfo validateBasketBeforeCheckout(@NonNull Integer basketId) {
        // Do all validations
        Map<Integer, BasketContent> contents = getContentsByBasketId(basketId);
        checkItemsAvailability(contents);
        float totalCost = getTotalBasketCost(contents);
        checkCost(totalCost);

        return new BasketCheckoutInfo(contents, totalCost);
    }

    /**
     * Checks the total cost of a {@link Basket}, and throws {@link MoneyValueException} if the money value is
     * below 100 or above 1500
     *
     * @param totalCost The total cost of the basket contents
     */
    private void checkCost(float totalCost) {
        if (totalCost < 100) {
            throw new MoneyValueException(Messages.LOW_MONEY_VALUE_ERROR);
        }
        if (totalCost > 1500) {
            throw new MoneyValueException(Messages.HIGH_MONEY_VALUE_ERROR);
        }
    }

    /**
     * Fetches all contents of a specific {@link Basket} by ID
     *
     * @param basketId The ID of the {@link Basket}
     * @return The {@link Basket} whose ID matches the specified ID, otherwise a {@link EntityNotFoundException} is thrown
     */
    Map<Integer, BasketContent> getContentsByBasketId(@NonNull Integer basketId) {

        List<BasketContent> lineItems = basketContentRepository.findByBasketId(basketId);
        Map<Integer, BasketContent> totalContents = new LinkedHashMap<>();

        // Consider the case that an item is added more than once, then we should combine
        // all occurrences into one
        for (BasketContent lineItem : lineItems) {
            if (totalContents.containsKey(lineItem.getItem().getId())) {
                // Total quantity should equal the sum of the two quantities and then it should be updated
                float totalQuantity = totalContents.get(lineItem.getItem().getId()).getQuantity() + lineItem.getQuantity();
                totalContents.get(lineItem.getItem().getId()).setQuantity(totalQuantity);
            } else {
                totalContents.put(lineItem.getItem().getId(), lineItem);
            }
        }

        return totalContents;
    }

    /**
     * Gets the total cost of all {@link Item}s in the {@link Basket}
     *
     * @param contents {@link Map} with all {@link BasketContent}s
     * @return The total price of the {@link Basket}
     */
    @SneakyThrows
    private float getTotalBasketCost(Map<Integer, BasketContent> contents) {
        float totalCost = 0F;
        for (Map.Entry<Integer, BasketContent> basketContentEntry : contents.entrySet()) {
            // Get the item to determine its price
            Item item = itemsService.get(basketContentEntry.getKey());
            totalCost += item.getPrice() * basketContentEntry.getValue().getQuantity();
        }

        return totalCost;
    }

    /**
     * Checks the availability of each {@link Item} in the stock and throws {@link InsufficientItemQuantityException}
     * in case the quantity is insufficient
     *
     * @param contents The contents of a specific {@link Basket}
     */
    @SneakyThrows
    private void checkItemsAvailability(Map<Integer, BasketContent> contents) throws InsufficientItemQuantityException {
        for (Map.Entry<Integer, BasketContent> basketContentEntry : contents.entrySet()) {
            // Get the item to determine its quantity
            Item item = itemsService.get(basketContentEntry.getKey());
            if (basketContentEntry.getValue().getQuantity() > item.getQuantity()) {
                String errorMessage = String.format(Messages.INSUFFICIENT_ITEM_QUANTITY_ERROR, item.getName());
                log.error(errorMessage);
                throw new InsufficientItemQuantityException(errorMessage);
            }
        }
    }

    @Getter
    @AllArgsConstructor
    public static class BasketCheckoutInfo {
        private Map<Integer, BasketContent> contents;
        private float totalCost;
    }
}
