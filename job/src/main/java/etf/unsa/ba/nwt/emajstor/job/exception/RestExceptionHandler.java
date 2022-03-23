package etf.unsa.ba.nwt.emajstor.job.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;
import java.util.UUID;

public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomExceptionResponse body = getDefaultExceptionBody(status, request);
        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
        body.setMessage(fieldError.getField() + " " + fieldError.getDefaultMessage());
        return new ResponseEntity<>(body, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomExceptionResponse body = getDefaultExceptionBody(status, request);
        body.setMessage(ex.getMessage());
        return new ResponseEntity<>(body, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomExceptionResponse body = getDefaultExceptionBody(status, request);
        body.setMessage(ex.getMessage());
        return new ResponseEntity<>(body, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomExceptionResponse body = getDefaultExceptionBody(status, request);
        body.setMessage("JSON parse error");
        return new ResponseEntity<>(body, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomExceptionResponse body = getDefaultExceptionBody(status, request);
        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
        if (fieldError.contains(TypeMismatchException.class)) {
            Class<?> requiredType = fieldError.unwrap(TypeMismatchException.class).getRequiredType();
            if (Objects.equals(requiredType, Integer.class))
                body.setMessage(fieldError.getField() + " must be a whole number");
            else if (Objects.equals(requiredType, UUID.class))
                body.setMessage(fieldError.getField() + " must be a valid uuid");
            else {
                body.setMessage("Parametar type mismatch");
            }
        } else {
            body.setMessage(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(body, headers, status);
    }

    private CustomExceptionResponse getDefaultExceptionBody(HttpStatus status, WebRequest request) {
        CustomExceptionResponse body = new CustomExceptionResponse();
        body.setStatus(status.value());
        body.setError(status.getReasonPhrase());
        body.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        return body;
    }

}
