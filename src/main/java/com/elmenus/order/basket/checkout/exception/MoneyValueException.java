package com.elmenus.order.basket.checkout.exception;

import com.elmenus.order.basket.checkout.model.Basket;

/**
 * An exception to be thrown when there is a problem with the total cost of a {@link Basket}
 */
public class MoneyValueException extends RuntimeException {
    public MoneyValueException(String message){
        super(message);
    }
}
