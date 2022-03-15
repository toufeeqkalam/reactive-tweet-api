package za.co.example.reactivetweetapi.controller.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import za.co.example.reactivetweetapi.model.error.ValidationError;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<?> handleException(WebExchangeBindException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ValidationError> validationErrors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(fieldError -> validationErrors.add(new ValidationError(fieldError.getObjectName(), fieldError.getDefaultMessage())));
        return ResponseEntity.badRequest().body(validationErrors);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKeyException(DuplicateKeyException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

}
