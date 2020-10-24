package com.kalah.service;

import com.kalah.exception.KalahaGameException;
import com.kalah.model.KalahaBoard;
import com.kalah.model.KalahaGame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

import static com.kalah.constant.KalahaGameConstant.*;

/**
 * KalahGameBoardFacade class for implementation of game actions
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KalahaGameBoardPlanner {


  private final   ErrorContentService errorContentService;


    /**
     * Method to initialize the game board
     *
     * @param board board object to be initialized
     */
    public void initializeGameBoard(Map<Integer, Integer> board) {
        IntStream.rangeClosed(KalahaBoard.FIRST_PIT_INDEX.getValue(), KalahaBoard.SECOND_KALAH_INDEX.getValue())
                .forEach(pitId -> {
                    int firstPitIndex = KalahaGame.Player.FIRST_PLAYER.getKalahId();
                    int secondPitIndex = KalahaGame.Player.SECOND_PLAYER.getKalahId();
                    int value = (pitId != firstPitIndex && pitId != secondPitIndex) ? KalahaBoard.INITIAL_STONES.getValue() : 0;
                    board.put(pitId, value);
                });
    }

    /**
     * Method to move pit stones
     *
     * @param pitId unique identifier of player pit
     * @param game  game entity
     */
    public void movePitStones(int pitId, KalahaGame game) {
        if(isGameTerminated(game)) {
            updateGameDetails(game, game.getBoard(), pitId);

            throw new KalahaGameException(GAME_TERMINATED_EXCEPTION_GAME_OVER_ERROR_CODE,
                    errorContentService.getErrorDescription(GAME_TERMINATED_EXCEPTION_GAME_OVER_ERROR_CODE,
                            Collections.singletonList(game.getWinner())), HttpStatus.CONFLICT);

        }
        if (isValidPitIndex(pitId, game)) {
            Map<Integer, Integer> board = game.getBoard();
            int stoneCount = board.get(pitId);
            clearSelectedPitStones(pitId, board);
            int lastIndex = pitId + stoneCount;
            int lastPit = lastIndex;
            lastIndex = populateGameBoard(pitId, game, board, lastIndex);
            lastPit = lastPit > KalahaBoard.SECOND_KALAH_INDEX.getValue() ? lastIndex : lastPit;
            int stoneCountInLastPit = board.get(lastPit);
            if (stoneCountInLastPit == 1) {
                pickOppositePitStonesToPlayerKalah(lastPit, game, stoneCountInLastPit);
            }
            updateGameDetails(game, board, lastPit);
        }
    }

    /**
     * Method to update the game details
     *
     * @param game    game entity
     * @param board   game board entity
     * @param lastPit pit where the player's last stone lands
     */
    private void updateGameDetails(KalahaGame game, Map<Integer, Integer> board, int lastPit) {
        if (!hasAnotherTurnForPlayer(lastPit, game.getPlayer())) {
            game.setPlayer(game.getPlayer().getOppositePlayer());
        } else {
            game.setPlayer(game.getPlayer());
        }
        game.setBoard(board);
        if (isGameTerminated(game)) {
            String winner = gameWinner(game);
            game.setWinner(winner);
            game.setStatus(KalahaGame.GameStatus.GAME_OVER);
        }
    }

    /**
     * Method to populate the game board with player's stones
     *
     * @param pitId     unique identifier of player pit
     * @param game      game entity
     * @param board     board entity
     * @param lastIndex integer to be updated
     * @return calculated lastIndex based on player's stones
     */
    private int populateGameBoard(int pitId, KalahaGame game, Map<Integer, Integer> board, int lastIndex) {
        for (int currentIndex = pitId + 1; currentIndex <= lastIndex; currentIndex++) {
            int currentPit = currentIndex;
            if (currentIndex == KalahaBoard.SECOND_KALAH_INDEX.getValue()
                    && lastIndex != KalahaBoard.SECOND_KALAH_INDEX.getValue()) {
                // Player is in 14 th pit and has more than 1 stone left - A
                lastIndex = lastIndex - currentIndex;
                currentIndex = 0;
            }

            if (game.getPlayer().getOppositePlayer().getKalahId() != currentPit) {
                // I can put the stone in the current pit - B
                int currentStonesInPit = board.get(currentPit);
                board.replace(currentPit, currentStonesInPit + 1);
            } else {
                if (currentIndex != KalahaBoard.SECOND_KALAH_INDEX.getValue()) {
                    // Happens for player 1 , Current index is 0 because it was updated in A
                    // Happens for player 2,  Current Index is 7
                    lastIndex++;
                } else {
                    // Happens only for Player 1 and the lastIndex is 14 and current Index 14 i.e
                    //player 1 has exactly one stone and he is in pit 14
                    lastIndex = KalahaBoard.FIRST_PIT_INDEX.getValue();
                    currentIndex = 0;
                }
            }
        }
        return lastIndex;
    }

    /**
     * Method to clear the selected pit's stones
     *
     * @param pitId unique identifier of player pit
     * @param board game entity
     */
    private void clearSelectedPitStones(int pitId, Map<Integer, Integer> board) {
        board.replace(pitId, 0);
    }

    /**
     * Method to pick the opposite pit's stones once the last stone lands in player's own empty pit
     *
     * @param pitId      unique identifier of player pit
     * @param game       game entity
     * @param noOfStones stone which lands on last empty pit of player(=1)
     */
    private void pickOppositePitStonesToPlayerKalah(int pitId, KalahaGame game, int noOfStones) {
        Map<Integer, Integer> board = game.getBoard();
        if (isUserPit(pitId, game.getPlayer())) {
            int oppositePit = getOppositePit(pitId);
            int oppositePitStoneCount = game.getBoard().get(oppositePit);
            clearSelectedPitStones(oppositePit, board);
            clearSelectedPitStones(pitId, board);
            int playerKalahId = game.getPlayer().getKalahId();
            int stonesInPlayerKalah = board.get(playerKalahId);
            int totalStones = stonesInPlayerKalah + oppositePitStoneCount + noOfStones;
            board.replace(playerKalahId, totalStones);
        }
    }

    /**
     * To check whether the current player has another turn to continue
     *
     * @param lastPitIndex pitid choosen by the current player last time
     * @param player       current player of the game
     * @return true if the player has other turn else false
     */
    private boolean hasAnotherTurnForPlayer(int lastPitIndex, KalahaGame.Player player) {
        return player.getKalahId() == lastPitIndex;
    }

    /**
     * Method to get opposite pit number of given pit
     *
     * @param pitId pit id of the current player
     * @return pit id of the opposite player
     */
    private int getOppositePit(int pitId) {
        return KalahaBoard.SECOND_KALAH_INDEX.getValue() - pitId;
    }

    /**
     * To check whether the selected pit is current player's pit
     *
     * @param pitId  pit id of the current player
     * @param player current player object
     * @return true if the pit id is of current player
     */
    private boolean isUserPit(int pitId, KalahaGame.Player player) {
        return player.getPits().contains(pitId);
    }

    /**
     * Validate selected Pit index
     *
     * @param pitId pit id of the current player
     * @param game  KalahGame object
     */
    private boolean isValidPitIndex(int pitId, KalahaGame game) {
        KalahaGame.Player player = game.getPlayer();
        if (pitId == player.getKalahId() || pitId == player.getOppositePlayer().getKalahId()) {
            throw new KalahaGameException
                    (INVALID_PIT_ID_PIT_ID_IS_KALAH_ERROR_CODE, errorContentService.
                            getErrorDescription(INVALID_PIT_ID_PIT_ID_IS_KALAH_ERROR_CODE), HttpStatus.BAD_REQUEST);
        }

        if (pitId < KalahaBoard.FIRST_PIT_INDEX.getValue() || pitId > KalahaBoard.LAST_PIT_INDEX.getValue()) {
            throw new KalahaGameException
                    (INVALID_PIT_ID_PIT_ID_DOES_NOT_EXIST_ERROR_CODE,  errorContentService.
                            getErrorDescription(INVALID_PIT_ID_PIT_ID_DOES_NOT_EXIST_ERROR_CODE), HttpStatus.BAD_REQUEST);
        }

        if (!isUserPit(pitId, player)) {
            throw new KalahaGameException
                    (INVALID_PIT_ID_WRONG_TURN_ERROR_CODE, errorContentService.
                            getErrorDescription(INVALID_PIT_ID_WRONG_TURN_ERROR_CODE), HttpStatus.BAD_REQUEST);
        }
        if (game.getBoard().get(pitId) == 0) {
            throw new KalahaGameException
                    (INVALID_PIT_ID_EMPTY_PIT_SELECTED_ERROR_CODE, errorContentService.
                            getErrorDescription(INVALID_PIT_ID_EMPTY_PIT_SELECTED_ERROR_CODE), HttpStatus.BAD_REQUEST);
        }
        return true;
    }

    /**
     * Method to verify whether the game is terminated
     *
     * @param game KalahGame entity
     * @return true if either of the player's pits ran out of stones else false
     */
    private boolean isGameTerminated(KalahaGame game) {
        KalahaGame.Player player = game.getPlayer();
        Map<Integer, Integer> board = game.getBoard();
        if (arePitsEmpty(player, board) || arePitsEmpty(player.getOppositePlayer(), board)) {
            putAllStonesToKalah(player, board);
            putAllStonesToKalah(player.getOppositePlayer(), board);
            return true;
        }
        return false;
    }

    /**
     * Method to check whether the player's pits are empty
     *
     * @param player current player
     * @param board  game board entity
     * @return true if the player's pits are empty else false
     */
    private boolean arePitsEmpty(KalahaGame.Player player, Map<Integer, Integer> board) {
        return player.getPits().stream()
                .map(board::get)
                .allMatch(stoneNumbers -> stoneNumbers == 0);
    }

    /**
     * Method to put the stones in Player's Kalah
     *
     * @param player current player of the game
     * @param board  game board entity
     */
    private void putAllStonesToKalah(KalahaGame.Player player, Map<Integer, Integer> board) {
        player.getPits().forEach(pit -> {
            int stonesInCurrentPit = board.get(pit);
            if (stonesInCurrentPit != 0) {
                int kalahId = player.getKalahId();
                board.replace(kalahId, board.get(kalahId) + stonesInCurrentPit);
                clearSelectedPitStones(pit, board);
            }
        });
    }

    /**
     * Method to get the Winner of the current game
     *
     * @param game current game entity
     * @return winner of the game
     */
    private String gameWinner(KalahaGame game) {
        Map<Integer, Integer> board = game.getBoard();
        int firstPlayerStones = board.get(KalahaGame.Player.FIRST_PLAYER.getKalahId());
        int secondPlayerStones = board.get(KalahaGame.Player.SECOND_PLAYER.getKalahId());
        if (firstPlayerStones > secondPlayerStones) {
            return KalahaGame.Player.FIRST_PLAYER.getName();
        } else if (firstPlayerStones < secondPlayerStones) {
            return KalahaGame.Player.SECOND_PLAYER.getName();
        } else {
            return KalahaGame.GameStatus.DRAW.getGameStatus();
        }
    }

}
