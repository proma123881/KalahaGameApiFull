package com.kalah.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * ErrorDetails Model class
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Data
@Builder
public class ErrorDetails {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorCode;
    private String timestamp;
    private String message;
    private String requestUri;



}
