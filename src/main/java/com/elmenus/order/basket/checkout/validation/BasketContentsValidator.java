package com.elmenus.order.basket.checkout.validation;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.BasketContentDto;
import com.elmenus.order.basket.checkout.dto.ItemDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for {@link BasketContentDto}
 */
@Component
public class BasketContentsValidator implements Validator {
    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return BasketContentDto.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        BasketContentDto basketContentDto = (BasketContentDto) object;

        if (basketContentDto.getQuantity() <= 0) {
            errors.reject(StringUtils.EMPTY, null, Messages.QUANTITY_NEGATIVE_ERROR);
            return;
        }
    }
}