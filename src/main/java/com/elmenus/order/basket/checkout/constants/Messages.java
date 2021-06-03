package com.elmenus.order.basket.checkout.constants;

import lombok.experimental.UtilityClass;

/**
 * Container for success and error messages
 */
@UtilityClass
public class Messages {
    // region success messages
    public final String USER_CREATED_MESSAGE = "User created successfully";
    public final String USER_UPDATED_MESSAGE = "User updated successfully";
    public final String USER_DELETED_MESSAGE = "User deleted successfully";

    public final String BASKET_CREATED_MESSAGE = "Basket created successfully";
    public final String BASKET_UPDATED_MESSAGE = "Basket updated successfully";
    public final String BASKET_DELETED_MESSAGE = "Basket deleted successfully";
    public final String BASKET_CHECKED_OUT_MESSAGE = "Basket checked out successfully";
    public final String BASKET_CONTENT_VALIDATED_MESSAGE = "Basket contents have been validated successfully";

    public final String ITEM_CREATED_MESSAGE = "Item created successfully";
    public final String ITEM_UPDATED_MESSAGE = "Item updated successfully";
    public final String ITEM_DELETED_MESSAGE = "Item deleted successfully";
    public final String ITEM_QUANTITY_UPDATED_MESSAGE = "Item quantity updated successfully";

    public final String BASKET_CONTENT_CREATED_MESSAGE = "Basket content created successfully";
    public final String BASKET_CONTENT_UPDATED_MESSAGE = "Basket content updated successfully";
    public final String BASKET_CONTENT_DELETED_MESSAGE = "Basket content deleted successfully";
    // endregion

    // region error messages for user
    public final String FIRST_NAME_BLANK_ERROR = "First name is not provided";
    public final String LAST_NAME_BLANK_ERROR = "Last name is not provided";
    public final String USERNAME_BLANK_ERROR = "Username is not provided";
    public final String EMAIL_BLANK_ERROR = "eMail is not provided";
    public final String EMAIL_INVALID_ERROR = "eMail is invalid";
    public final String ID_NULL_ERROR = "id is not provided";

    public final String USER_NOT_FOUND_ERROR = "User with ID %s is not found";
    public final String USER_NULL_ERROR = "User is not provided";
    // endregion

    // region error messages for baskets
    public final String BASKET_NOT_FOUND_ERROR = "Basket with ID %s is not found";
    public final String BASKET_NULL_ERROR = "Basket is not provided";
    public final String BASKET_CHECKED_OUT_ALREADY_ERROR = "Basket with ID %s has already been checked out";

    public final String BASKET_CONTENT_NOT_FOUND_ERROR = "Basket content with ID %s is not found";
    public final String BASKET_CONTENT_NULL_ERROR = "Basket content is not provided";
    public final String FIELD_VALUE_INVALID_ERROR = "Value %s is invalid for field %s";

    public final String LOW_MONEY_VALUE_ERROR = "Money value below 100";
    public final String HIGH_MONEY_VALUE_ERROR = "Fraud user, money value above 1500";
    // endregion

    // region error messages for items
    public final String ITEM_NOT_FOUND_ERROR = "Item with ID %s is not found";
    public final String ITEM_NULL_ERROR = "Item is not provided";
    public final String NAME_BLANK_ERROR = "Name is not provided";
    public final String INSUFFICIENT_ITEM_QUANTITY_ERROR = "Insufficient quantity of %s";
    public final String PRICE_NEGATIVE_ERROR = "Price should be greater than 0";
    public final String QUANTITY_NEGATIVE_ERROR = "Quantity should be greater than 0";
    // endregion
}
