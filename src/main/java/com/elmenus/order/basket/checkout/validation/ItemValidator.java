package com.elmenus.order.basket.checkout.validation;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.ItemDto;
import com.elmenus.order.basket.checkout.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for {@link ItemDto}
 */
@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return ItemDto.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        ItemDto itemDto = (ItemDto) object;

        if (StringUtils.isBlank(itemDto.getName())) {
            errors.reject(StringUtils.EMPTY, null, Messages.NAME_BLANK_ERROR);
            return;
        }

        if (itemDto.getPrice() <= 0) {
            errors.reject(StringUtils.EMPTY, null, Messages.PRICE_NEGATIVE_ERROR);
            return;
        }

        if (itemDto.getQuantity() <= 0) {
            errors.reject(StringUtils.EMPTY, null, Messages.QUANTITY_NEGATIVE_ERROR);
            return;
        }
    }
}