package com.elmenus.order.basket.checkout.handler;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.exception.ErrorInfo;
import com.elmenus.order.basket.checkout.exception.MoneyValueException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class {@link OrderBasketCheckoutExceptionHandler}
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderBasketCheckoutExceptionHandlerTest {

    // region field values
    private static final String EXCEPTION_MESSAGE = "Exception is thrown";
    private static final String FIELD_NAME = "id";
    private static final String FIELD_VALUE = "test";
    // endregion

    @Spy
    private OrderBasketCheckoutExceptionHandler exceptionHandler;

    /**
     * Tests {@link OrderBasketCheckoutExceptionHandler#handleEntityNotFoundExceptionException(EntityNotFoundException)}
     */
    @Test
    public void testHandleEntityNotFoundExceptionException() {
        // GIVEN
        EntityNotFoundException exception = new EntityNotFoundException(EXCEPTION_MESSAGE);

        // WHEN
        ResponseEntity<ErrorInfo> responseEntity = exceptionHandler.handleEntityNotFoundExceptionException(exception);

        // THEN
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull(responseEntity.getBody().getMessage());
        Assert.assertEquals(EXCEPTION_MESSAGE, responseEntity.getBody().getMessage());
    }

    /**
     * Tests {@link OrderBasketCheckoutExceptionHandler#handleOrderBasketCheckoutExceptions(RuntimeException)}
     */
    @Test
    public void testHandleOrderBasketCheckoutExceptions() {
        // GIVEN
        MoneyValueException exception = new MoneyValueException(EXCEPTION_MESSAGE);

        // WHEN
        ResponseEntity<ErrorInfo> responseEntity = exceptionHandler.handleOrderBasketCheckoutExceptions(exception);

        // THEN
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull(responseEntity.getBody().getMessage());
        Assert.assertEquals(EXCEPTION_MESSAGE, responseEntity.getBody().getMessage());
    }

    /**
     * Tests {@link OrderBasketCheckoutExceptionHandler#handleIllegalStateException(IllegalStateException)}
     */
    @Test
    public void testHandleIllegalStateException() {
        // GIVEN
        IllegalStateException exception = new IllegalStateException(EXCEPTION_MESSAGE);

        // WHEN
        ResponseEntity<ErrorInfo> responseEntity = exceptionHandler.handleIllegalStateException(exception);

        // THEN
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull(responseEntity.getBody().getMessage());
        Assert.assertEquals(EXCEPTION_MESSAGE, responseEntity.getBody().getMessage());
    }

    /**
     * Tests {@link OrderBasketCheckoutExceptionHandler#handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException)}
     */
    @Test
    public void testHandleMethodArgumentTypeMismatchException() {
        // GIVEN
        MethodParameter parameter = Mockito.mock(MethodParameter.class);
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(FIELD_VALUE, Integer.class,
                FIELD_NAME, parameter, new Exception(EXCEPTION_MESSAGE));

        // WHEN
        ResponseEntity<ErrorInfo> responseEntity = exceptionHandler.handleMethodArgumentTypeMismatchException(exception);

        // THEN
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull((responseEntity.getBody()).getMessage());
        String errorMessage = String.format(Messages.FIELD_VALUE_INVALID_ERROR, FIELD_VALUE, FIELD_NAME);
        Assert.assertEquals(errorMessage, responseEntity.getBody().getMessage());
    }

    /**
     * Tests {@link OrderBasketCheckoutExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException)}
     */
    @Test
    public void handleMethodArgumentNotValid() {
        // GIVEN
        BindingResult bindingResult = new MapBindingResult(Map.of(), FIELD_NAME);
        bindingResult.addError(new ObjectError(FIELD_NAME, EXCEPTION_MESSAGE));
        MethodParameter parameter = Mockito.mock(MethodParameter.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        // WHEN
        ResponseEntity<Object> responseEntity = exceptionHandler.handleMethodArgumentNotValid(exception);

        // THEN
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull(((ErrorInfo) responseEntity.getBody()).getMessage());
        Assert.assertEquals(EXCEPTION_MESSAGE, ((ErrorInfo) responseEntity.getBody()).getMessage());
    }

    @Test
    public void handleHttpMessageNotReadable() {
        // GIVEN
        BindingResult bindingResult = new MapBindingResult(Map.of(), FIELD_NAME);
        bindingResult.addError(new ObjectError(FIELD_NAME, EXCEPTION_MESSAGE));
        HttpInputMessage httpInputMessage = Mockito.mock(HttpInputMessage.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(EXCEPTION_MESSAGE, httpInputMessage);

        // WHEN
        ResponseEntity<Object> responseEntity = exceptionHandler.handleHttpMessageNotReadable(exception);

        // THEN
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull(((ErrorInfo) responseEntity.getBody()).getMessage());
        Assert.assertEquals(EXCEPTION_MESSAGE, ((ErrorInfo) responseEntity.getBody()).getMessage());
    }

    @Test
    public void handleGenericException() {
        // GIVEN
        Exception exception = new Exception(EXCEPTION_MESSAGE);

        // WHEN
        ResponseEntity<ErrorInfo> responseEntity = exceptionHandler.handleGenericException(exception);

        // THEN
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertNotNull(responseEntity.getBody().getMessage());
        Assert.assertEquals(EXCEPTION_MESSAGE, responseEntity.getBody().getMessage());
    }
}