package com.kalah.model;

import lombok.*;

/**
 * Response class to handle created Kalah Game entity
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewGameResponse {
    private String id;
    private String uri;
}
