package com.elmenus.order.basket.checkout.validation;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for {@link UserDto}
 */
@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return UserDto.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        UserDto userDto = (UserDto) object;

        if (StringUtils.isBlank(userDto.getFirstname())) {
            errors.reject(StringUtils.EMPTY, null, Messages.FIRST_NAME_BLANK_ERROR);
            return;
        }

        if (StringUtils.isBlank(userDto.getLastname())) {
            errors.reject(StringUtils.EMPTY, null, Messages.LAST_NAME_BLANK_ERROR);
            return;
        }

        if (StringUtils.isBlank(userDto.getUsername())) {
            errors.reject(StringUtils.EMPTY, null, Messages.USERNAME_BLANK_ERROR);
            return;
        }

        if (StringUtils.isBlank(userDto.getEmail())) {
            errors.reject(StringUtils.EMPTY, null, Messages.EMAIL_BLANK_ERROR);
            return;
        }


        if (!EmailValidator.getInstance().isValid(userDto.getEmail())) {
            errors.reject(StringUtils.EMPTY, null, Messages.EMAIL_INVALID_ERROR);
            return;
        }
    }
}