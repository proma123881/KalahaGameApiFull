package com.kalah.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * KalahBoard enum
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum KalahaBoard {
    INITIAL_STONES(6),
    FIRST_PIT_INDEX(1),
    LAST_PIT_INDEX(13),
    FIRST_KALAH_INDEX(7),
    SECOND_KALAH_INDEX(14);
    private final int value;

}
