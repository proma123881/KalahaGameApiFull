package com.kalah.repository;

import com.kalah.model.KalahaGame;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * KalahGameInMemoryRepositoryImpl Singleton for persisting the game details in-Memory
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
public class KalahGameInMemoryRepositoryImpl implements KalahGameInMemoryRepository{

    private static final KalahGameInMemoryRepositoryImpl instance = new KalahGameInMemoryRepositoryImpl();

    private final Map<Integer, KalahaGame> inMemoryGameMap = new ConcurrentHashMap<>();

    public static KalahGameInMemoryRepositoryImpl getInstance() {
        return instance;
    }

    private KalahGameInMemoryRepositoryImpl() {
    }

    /**
     * Method to save the persisted/created details
     *
     * @param game game object to be created
     * @return persisted/created KalahGame entity
     */
    @Override
    public KalahaGame save(KalahaGame game) {
        inMemoryGameMap.put(game.getId(), game);
        return game;
    }

    /**
     * Method to look for already persisted KalahGame entity
     *
     * @param gameId unique identifier of game
     * @return Optional object of Kalaha Game type
     */
    @Override
    public KalahaGame findByGameId(int gameId) {
        if (gameId > 0) {
            return inMemoryGameMap.get(gameId);
        }
        return null;
    }
}
