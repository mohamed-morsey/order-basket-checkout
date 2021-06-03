package com.elmenus.order.basket.checkout.dto;

import com.elmenus.order.basket.checkout.constants.Messages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @NotBlank(message = Messages.FIRST_NAME_BLANK_ERROR)
    private String firstname;

    @NotBlank(message = Messages.LAST_NAME_BLANK_ERROR)
    private String lastname;

    @NotBlank(message = Messages.USERNAME_BLANK_ERROR)
    private String username;

    @NotBlank(message = Messages.EMAIL_BLANK_ERROR)
    @Email(message = Messages.EMAIL_INVALID_ERROR)
    private String email;
}
