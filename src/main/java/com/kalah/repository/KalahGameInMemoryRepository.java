package com.kalah.repository;

import com.kalah.model.KalahaGame;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Repository interface for Kalah Game
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Component
public interface KalahGameInMemoryRepository {
    KalahaGame save(final KalahaGame game);
    KalahaGame findByGameId(int gameId);
}
