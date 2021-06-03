package com.elmenus.order.basket.checkout.constants;

import lombok.experimental.UtilityClass;

/**
 * Container for constants used throughout the system
 */
@UtilityClass
public class Constants {

    public final String SLASH = "/";
    public final String QUESTION_MARK = "?";
    public final String EQUALS = "=";
    public final String AMPERSAND = "&";

    // region field and column names
    public final String ID_FIELD = "id";
    public final String NAME_FIELD = "name";
    public final String FIRSTNAME_FIELD = "firstname";
    // endregion

    // region parameter names
    public final String ID_PARAMETER = "id";
    public final String BASKET_ID_PARAMETER = "basketId";
    // endregion
}
