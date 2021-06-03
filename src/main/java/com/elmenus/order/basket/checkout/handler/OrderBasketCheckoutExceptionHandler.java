package com.elmenus.order.basket.checkout.handler;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.exception.ErrorInfo;
import com.elmenus.order.basket.checkout.exception.InsufficientItemQuantityException;
import com.elmenus.order.basket.checkout.exception.MoneyValueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for exceptions that are thrown by the application
 */
@Slf4j
@RestControllerAdvice
public class OrderBasketCheckoutExceptionHandler {

    /**
     * Handler for {@link EntityNotFoundException}
     *
     * @param exception The exception to be handled
     * @return {@link ResponseEntity} with the error details
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ErrorInfo> handleEntityNotFoundExceptionException(EntityNotFoundException exception) {
        final ErrorInfo errorInfo = new ErrorInfo(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorInfo);
    }

    /**
     * Handler for {@link MoneyValueException} and {@link InsufficientItemQuantityException}
     *
     * @param exception The exception to be handled
     * @return {@link ResponseEntity} with the error details
     */
    @ExceptionHandler({MoneyValueException.class, InsufficientItemQuantityException.class})
    public final ResponseEntity<ErrorInfo> handleOrderBasketCheckoutExceptions(RuntimeException exception) {
        final ErrorInfo errorInfo = new ErrorInfo(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorInfo);
    }

    /**
     * Handler for {@link Exception} which handles the generic exception
     *
     * @param exception The exception to be handled
     * @return {@link ResponseEntity} with the error details
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public final ResponseEntity<ErrorInfo> handleWebExchangeBindException(WebExchangeBindException exception) {
        final ErrorInfo errorInfo = new ErrorInfo(exception.getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorInfo);
    }

    /**
     * Handler for {@link IllegalStateException}
     *
     * @param exception The exception to be handled
     * @return {@link ResponseEntity} with the error details
     */
    @ExceptionHandler(IllegalStateException.class)
    public final ResponseEntity<ErrorInfo> handleIllegalStateException(IllegalStateException exception) {
        final ErrorInfo errorInfo = new ErrorInfo(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorInfo);
    }

    /**
     * Handler for {@link MethodArgumentTypeMismatchException}
     *
     * @param exception The exception to be handled
     * @return {@link ResponseEntity} with the error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ErrorInfo> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String errorMessage = String.format(Messages.FIELD_VALUE_INVALID_ERROR, exception.getValue(), exception.getName());
        final ErrorInfo errorInfo = new ErrorInfo(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorInfo);
    }

    /**
     * Handler for {@link MethodArgumentNotValidException}
     *
     * @param exception The exception to be handled
     * @return {@link ResponseEntity} with the error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().iterator().forEachRemaining(error -> errors.add(error.getDefaultMessage()));

        String allErrors = String.join(", ", errors);
        final ErrorInfo errorInfo = new ErrorInfo(allErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorInfo);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        final ErrorInfo errorInfo = new ErrorInfo(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorInfo);
    }

    /**
     * Handler for {@link Exception} which handles the generic exception
     *
     * @param exception The exception to be handled
     * @return {@link ResponseEntity} with the error details
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorInfo> handleGenericException(Exception exception) {
        final ErrorInfo errorInfo = new ErrorInfo(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorInfo);
    }
}
