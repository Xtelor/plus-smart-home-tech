package ru.yandex.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NoAuthorizedUserException.class)
    public ResponseEntity<ErrorResponse> handleNoAuthorizedUser(NoAuthorizedUserException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public ResponseEntity<ErrorResponse> handleNoProductsInCart(NoProductsInShoppingCartException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyInWarehouse(SpecifiedProductAlreadyInWarehouseException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleNoProductInWarehouse(NoSpecifiedProductInWarehouseException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    public ResponseEntity<ErrorResponse> handleLowQuantity(ProductInShoppingCartLowQuantityInWarehouse e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
