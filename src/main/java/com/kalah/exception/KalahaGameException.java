package com.kalah.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * KalahGameTerminatedException Class
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
public class KalahaGameException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    /**
     * Constructor
     * @param message  message describing the cause of error
     * @param errorCode  code for the particular error
     * @param httpStatus  relevant http status code for the particlular error
     */
    public KalahaGameException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
