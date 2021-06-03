package com.elmenus.order.basket.checkout.validation;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

/**
 * Test class for {@link UserValidator}
 */
@RunWith(MockitoJUnitRunner.class)
public class UserValidatorTest {

    // region field values
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Smith";
    private static final String USERNAME = "jsmith";
    private static final String EMAIL = "smith@example.org";
    private static final String INVALID_EMAIL = "smith@example";
    private static final String DTO = "dto";
    // endregion

    @Spy
    private UserValidator validator;
    private UserDto userDto;

    @Before
    public void setUp() {
        userDto = new UserDto(FIRSTNAME, LASTNAME, USERNAME, EMAIL);
    }

    /**
     * Tests {@link UserValidator#supports(Class)}
     */
    @Test
    public void testSupports() {
        // GIVEN

        // WHEN
        boolean isSupported = validator.supports(UserDto.class);

        // THEN
        Assertions.assertTrue(isSupported);
    }

    /**
     * Tests {@link UserValidator#supports(Class)} but with an invalid type
     */
    @Test
    public void testSupportsًWithInvalidType() {
        // GIVEN

        // WHEN
        boolean isSupported = validator.supports(String.class);

        // THEN
        Assertions.assertFalse(isSupported);
    }

    /**
     * Tests {@link UserValidator#validate(Object, Errors)}
     */
    @Test
    public void testValidate() {
        // GIVEN
        Errors errors = new BeanPropertyBindingResult(userDto, DTO);

        // WHEN
        validator.validate(userDto, errors);

        // THEN
        Assert.assertFalse(errors.hasErrors());
    }

    /**
     * Tests {@link UserValidator#validate(Object, Errors)} but blank first name
     */
    @Test
    public void testValidateWithBlankFirstname() {
        // GIVEN
        userDto.setFirstname(StringUtils.EMPTY);
        Errors errors = new BeanPropertyBindingResult(userDto, DTO);

        // WHEN
        validator.validate(userDto, errors);

        // THEN
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getAllErrors().get(0));
        Assert.assertEquals(Messages.FIRST_NAME_BLANK_ERROR, errors.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * Tests {@link UserValidator#validate(Object, Errors)} but blank last name
     */
    @Test
    public void testValidateWithBlankLastname() {
        // GIVEN
        userDto.setLastname(StringUtils.EMPTY);
        Errors errors = new BeanPropertyBindingResult(userDto, DTO);

        // WHEN
        validator.validate(userDto, errors);

        // THEN
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getAllErrors().get(0));
        Assert.assertEquals(Messages.LAST_NAME_BLANK_ERROR, errors.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * Tests {@link UserValidator#validate(Object, Errors)} but blank username
     */
    @Test
    public void testValidateWithBlankUsername() {
        // GIVEN
        userDto.setUsername(StringUtils.EMPTY);
        Errors errors = new BeanPropertyBindingResult(userDto, DTO);

        // WHEN
        validator.validate(userDto, errors);

        // THEN
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getAllErrors().get(0));
        Assert.assertEquals(Messages.USERNAME_BLANK_ERROR, errors.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * Tests {@link UserValidator#validate(Object, Errors)} but blank email
     */
    @Test
    public void testValidateWithBlankEmail() {
        // GIVEN
        userDto.setEmail(StringUtils.EMPTY);
        Errors errors = new BeanPropertyBindingResult(userDto, DTO);

        // WHEN
        validator.validate(userDto, errors);

        // THEN
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getAllErrors().get(0));
        Assert.assertEquals(Messages.EMAIL_BLANK_ERROR, errors.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * Tests {@link UserValidator#validate(Object, Errors)} but invalid email
     */
    @Test
    public void testValidateWithInvalidEmail() {
        // GIVEN
        userDto.setEmail(INVALID_EMAIL);
        Errors errors = new BeanPropertyBindingResult(userDto, DTO);

        // WHEN
        validator.validate(userDto, errors);

        // THEN
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getAllErrors().get(0));
        Assert.assertEquals(Messages.EMAIL_INVALID_ERROR, errors.getAllErrors().get(0).getDefaultMessage());
    }
}