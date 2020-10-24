package com.kalah.service;

import com.kalah.model.KalahaGame;

/**
 * KalahGameService Interface for performing game actions
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
public interface KalahaGameService {

    KalahaGame createNewGame();
    KalahaGame movePlayerPitStones(int gameId, int pitId);
}
