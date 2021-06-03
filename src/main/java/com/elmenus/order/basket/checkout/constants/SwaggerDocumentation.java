package com.elmenus.order.basket.checkout.constants;

import lombok.experimental.UtilityClass;

/**
 * Container for swagger documentation
 */
@UtilityClass
public class SwaggerDocumentation {
    // region swagger documentation for baskets
    public final String BASKETS_CONTROLLER_SUMMARY = "Controller for handling baskets";
    public final String GET_ALL_BASKETS_SUMMARY = "Get all baskets";
    public final String GET_BASKET_SUMMARY = "Get basket by ID";
    public final String ADD_BASKET_SUMMARY = "Add a new basket";
    public final String UPDATE_BASKET_SUMMARY = "Update basket by ID";
    public final String DELETE_BASKET_SUMMARY = "Delete basket by ID";
    public final String CHECKOUT_BASKET_SUMMARY = "Checkout basket contents";
    // endregion

    // region swagger documentation for users
    public final String USERS_CONTROLLER_SUMMARY = "Controller for handling users";
    public final String GET_ALL_USERS_SUMMARY = "Get all users";
    public final String GET_USER_SUMMARY = "Get user by ID";
    public final String ADD_USER_SUMMARY = "Add a new user";
    public final String UPDATE_USER_SUMMARY = "Update user by ID";
    public final String DELETE_USER_SUMMARY = "Delete user by ID";
    // endregion

    // region swagger documentation for items
    public final String ITEMS_CONTROLLER_SUMMARY = "Controller for handling items";
    public final String GET_ALL_ITEMS_SUMMARY = "Get all items";
    public final String GET_ITEM_SUMMARY = "Get item by ID";
    public final String ADD_ITEM_SUMMARY = "Add a new item";
    public final String UPDATE_ITEM_SUMMARY = "Update item by ID";
    public final String DELETE_ITEM_SUMMARY = "Delete item by ID";
    // endregion

    // region swagger documentation for baskets
    public final String BASKET_CONTENTS_CONTROLLER_SUMMARY = "Controller for handling basket contents";
    public final String GET_ALL_BASKET_CONTENTS_SUMMARY = "Get all basket contents";
    public final String GET_BASKET_CONTENTS_SUMMARY = "Get basket contents by ID";
    public final String ADD_BASKET_CONTENTS_SUMMARY = "Add a new basket content";
    public final String UPDATE_BASKET_CONTENTS_SUMMARY = "Update basket content by ID";
    public final String DELETE_BASKET_CONTENTS_SUMMARY = "Delete basket content by ID";
    // endregion

    // region HTTP status codes
    public final String HTTP_OK = "200";
    public final String HTTP_CREATED = "201";
    public final String HTTP_NOT_FOUND = "404";
    // endregion
}
