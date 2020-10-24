package com.kalah.service;

import com.kalah.exception.KalahaGameException;
import com.kalah.mapper.KalahaGameMapper;
import com.kalah.model.KalahaGame;
import com.kalah.repository.KalahGameInMemoryRepository;
import com.kalah.repository.KalahGameInMemoryRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.NamedParameterJdbcOperationsDependsOnPostProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.kalah.constant.KalahaGameConstant.*;
import static com.kalah.constant.KalahaGameConstant.GAME_TERMINATED_EXCEPTION_GAME_OVER_ERROR_CODE;

/**
 * Service class for performing game actions
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KalahaGameServiceImpl implements KalahaGameService {
    private final AtomicInteger randomInteger = new AtomicInteger(1);
    private final KalahGameInMemoryRepository gameRepository = KalahGameInMemoryRepositoryImpl.getInstance();
    private final KalahaGameMapper gameMapper;
    private final KalahaGameBoardPlanner kalahaGameBoardPlanner;
    private final ErrorContentService errorContentService;


    /**
     * Method to create a new game entity
     *
     * @return persisted/created NewGameResponse object with
     * {id - unique identifier of game ,uri - hosting uri of the game} variables
     */
    @Override
    public KalahaGame createNewGame() {
        Map<Integer, Integer> board = new HashMap<>();
        kalahaGameBoardPlanner.initializeGameBoard(board);
        int gameId = randomInteger.getAndIncrement();
        KalahaGame game = gameRepository.save(KalahaGame.builder()
                .id(gameId).board(board)
                .status(KalahaGame.GameStatus.IN_PROGRESS)
                .player(KalahaGame.Player.FIRST_PLAYER)
                .build());


        log.debug("--Game created with game id---"+gameId);
        return game;
    }

    /**
     * Method for moving or sowing the stones inside the kalah board by the current player
     *
     * @param gameId unique identifier of game
     * @param pitId  unique identifier of player's pit
     * @return persisted/created MoveResponse object with
     * {id - unique identifier of game ,
     * uri - hosting uri of the game ,
     * status - moved game board status } variables
     */
    @Override
    public KalahaGame movePlayerPitStones(int gameId, int pitId) {

        log.debug("-----To make a move --------gameId is--"+gameId+"--pitId is--"+pitId);

        KalahaGame game = gameRepository.findByGameId(gameId);
                //.orElseThrow(() ->
                // return null; );
                 //   new KalahaGameException(GAME_ID_NOT_FOUND_GAME_DOES_NOT_EXIST_ERROR_CODE,
                 //           errorContentService.getErrorDescription(
                 //                   GAME_ID_NOT_FOUND_GAME_DOES_NOT_EXIST_ERROR_CODE,
                 //                   Collections.singletonList(String.valueOf(gameId))), HttpStatus.NOT_FOUND));
        try {
            if (game == null) {
                return null;
            }
            if (game.getStatus() != KalahaGame.GameStatus.IN_PROGRESS) {
                throw new KalahaGameException(GAME_TERMINATED_EXCEPTION_GAME_OVER_ERROR_CODE,
                        errorContentService.getErrorDescription(GAME_TERMINATED_EXCEPTION_GAME_OVER_ERROR_CODE,
                                Collections.singletonList(game.getWinner())), HttpStatus.CONFLICT);
            }
            kalahaGameBoardPlanner.movePitStones(pitId, game);
            gameRepository.save(game);
            game.message = "Successful move";
        } catch (KalahaGameException e)
        {
            game.message =  e.getMessage();
            System.out.println(e.getMessage());
            return game;
        }

        return game;

    }
}
