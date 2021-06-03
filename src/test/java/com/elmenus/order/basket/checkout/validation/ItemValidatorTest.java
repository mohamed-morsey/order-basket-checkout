package com.elmenus.order.basket.checkout.validation;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.ItemDto;
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
 * Test class for {@link ItemValidator}
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemValidatorTest {

    // region field values
    private static final String NAME = "Cheese";
    private static final float PRICE = 10F;
    private static final float QUANTITY = 100F;
    private static final String DTO = "dto";
    // endregion

    @Spy
    private ItemValidator validator;
    private ItemDto itemDto;

    @Before
    public void setUp() {
        itemDto = new ItemDto(NAME, PRICE, QUANTITY);
    }

    /**
     * Tests {@link ItemValidator#supports(Class)}
     */
    @Test
    public void testSupports() {
        // GIVEN

        // WHEN
        boolean isSupported = validator.supports(ItemDto.class);

        // THEN
        Assertions.assertTrue(isSupported);
    }

    /**
     * Tests {@link ItemValidator#supports(Class)} but with an invalid type
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
     * Tests {@link ItemValidator#validate(Object, Errors)}
     */
    @Test
    public void testValidate() {
        // GIVEN
        Errors errors = new BeanPropertyBindingResult(itemDto, DTO);

        // WHEN
        validator.validate(itemDto, errors);

        // THEN
        Assert.assertFalse(errors.hasErrors());
    }

    /**
     * Tests {@link ItemValidator#validate(Object, Errors)} but blank name
     */
    @Test
    public void testValidateWithBlankName() {
        // GIVEN
        itemDto.setName(StringUtils.EMPTY);
        Errors errors = new BeanPropertyBindingResult(itemDto, DTO);

        // WHEN
        validator.validate(itemDto, errors);

        // THEN
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getAllErrors().get(0));
        Assert.assertEquals(Messages.NAME_BLANK_ERROR, errors.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * Tests {@link ItemValidator#validate(Object, Errors)} but invalid price
     */
    @Test
    public void testValidateWithInvalidPrice() {
        // GIVEN
        itemDto.setPrice(-1F);
        Errors errors = new BeanPropertyBindingResult(itemDto, DTO);

        // WHEN
        validator.validate(itemDto, errors);

        // THEN
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getAllErrors().get(0));
        Assert.assertEquals(Messages.PRICE_NEGATIVE_ERROR, errors.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * Tests {@link ItemValidator#validate(Object, Errors)} but invalid quantity
     */
    @Test
    public void testValidateWithInvalidQuantity() {
        // GIVEN
        itemDto.setQuantity(-1F);
        Errors errors = new BeanPropertyBindingResult(itemDto, DTO);

        // WHEN
        validator.validate(itemDto, errors);

        // THEN
        Assert.assertTrue(errors.hasErrors());
        Assert.assertNotNull(errors.getAllErrors().get(0));
        Assert.assertEquals(Messages.QUANTITY_NEGATIVE_ERROR, errors.getAllErrors().get(0).getDefaultMessage());
    }
}