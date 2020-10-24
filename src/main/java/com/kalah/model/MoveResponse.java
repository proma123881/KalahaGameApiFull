package com.kalah.model;

import lombok.*;

import java.util.Map;

/**
 * Response class to handle moved Kalah Game board entity
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoveResponse {
    private String id;
    private String url;
    private Map<Integer, Integer> status;
}
