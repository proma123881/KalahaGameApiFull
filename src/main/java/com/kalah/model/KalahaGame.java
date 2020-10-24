package com.kalah.model;

import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * KalahGame model class
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KalahaGame {
    private int id;
    private Map<Integer, Integer> board;
    private GameStatus status;
    private Player player;
    private String winner;
    public String message;


    @Getter
    @AllArgsConstructor
    public enum GameStatus {
        IN_PROGRESS("IN PROGRESS"),
        GAME_OVER("GAME OVER"),
        DRAW("DRAW");
        private final String gameStatus;
    }



    @Getter
    @AllArgsConstructor
    public enum Player {
        FIRST_PLAYER("First Player", KalahaBoard.FIRST_KALAH_INDEX.getValue(), Arrays.asList(1, 2, 3, 4, 5, 6)),
        SECOND_PLAYER("Second Player", KalahaBoard.SECOND_KALAH_INDEX.getValue(), Arrays.asList(8, 9, 10, 11, 12, 13));

        private final String name;
        private final int kalahId;
        private final List<Integer> pits;

        public Player getOppositePlayer() {
            return this == FIRST_PLAYER ? SECOND_PLAYER : FIRST_PLAYER;
        }
    }

}
