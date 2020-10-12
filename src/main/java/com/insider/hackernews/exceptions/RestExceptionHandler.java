package com.insider.hackernews.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.io.IOException;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object
	 * fails @Valid validation.
	 *
	 * @param ex
	 *            the MethodArgumentNotValidException that is thrown when @Valid
	 *            validation fails
	 * @param headers
	 *            HttpHeaders
	 * @param status
	 *            HttpStatus
	 * @param request
	 *            WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage("Validation error");
		return buildResponseEntity(apiError);
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown
	 * when @Validated fails.
	 *
	 * @param ex
	 *            the ConstraintViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.setDebugMessage(ex.getConstraintViolations().toString());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex
	 *            HttpMessageNotWritableException
	 * @param headers
	 *            HttpHeaders
	 * @param status
	 *            HttpStatus
	 * @param request
	 *            WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Error writing JSON output";
		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage(
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	
	/**
	 * Handle Illegal Argument Exception
	 *
	 * @param ex
	 *            the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle IO Exception
	 *
	 * @param ex
	 *            the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(IOException.class)
	protected ResponseEntity<Object> handleIOException(IOException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage("Unexpected Error");
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}


	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex
	 *            the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}