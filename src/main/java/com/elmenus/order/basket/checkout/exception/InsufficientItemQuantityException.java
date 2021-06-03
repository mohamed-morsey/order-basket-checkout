package com.elmenus.order.basket.checkout.exception;

import com.elmenus.order.basket.checkout.model.Item;

/**
 * An exception to be thrown when an {@link Item}'s quantity is insufficient
 */
public class InsufficientItemQuantityException extends RuntimeException {
    public InsufficientItemQuantityException(String message){
        super(message);
    }
}
