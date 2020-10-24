package com.kalah.repository;

import com.kalah.model.KalahaGame;
import com.kalah.service.ErrorContentService;
import com.kalah.service.KalahaGameBoardPlanner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class KalahaGameInMemoryRepositoryImplTest {
    private final KalahGameInMemoryRepository gameRepository = KalahGameInMemoryRepositoryImpl.getInstance();
    KalahaGame kalahaGame;
    private ErrorContentService errorContentService;
    private  PropertySource errorContent;

    private PropertySource<?> errorContentPropertySource() throws IOException {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> applicationYamlPropertySource = loader.
                load("errorContent.yaml", new ClassPathResource("errors.yaml"));
        return applicationYamlPropertySource.get(0);
    }
    @Before
    public void setUp() throws IOException {

        errorContent = errorContentPropertySource();
        errorContentService = new ErrorContentService(errorContent);
        KalahaGameBoardPlanner kalahaGameBoardPlanner = new KalahaGameBoardPlanner(errorContentService);
        Map<Integer, Integer> gameBoard = new HashMap<>();
        kalahaGameBoardPlanner.initializeGameBoard(gameBoard);
        kalahaGame = KalahaGame.builder()
                .id(1234).board(gameBoard)
                .status(KalahaGame.GameStatus.IN_PROGRESS)
                .player(KalahaGame.Player.FIRST_PLAYER)
                .build();
        gameRepository.save(kalahaGame);

    }

    @Test
    public void testForSave() {
        gameRepository.save(kalahaGame);
        assertEquals(1234, kalahaGame.getId());
        assertEquals(KalahaGame.GameStatus.IN_PROGRESS, kalahaGame.getStatus());
        assertEquals(KalahaGame.Player.SECOND_PLAYER, kalahaGame.getPlayer().getOppositePlayer());
    }

    @Test
    public void testForFindByValidGameId() {
        Optional<KalahaGame> optionalKalahGame = gameRepository.findByGameId(1234);
        if (optionalKalahGame.isPresent()) {
            assertEquals(1234, optionalKalahGame.get().getId());
            assertEquals(KalahaGame.GameStatus.IN_PROGRESS, optionalKalahGame.get().getStatus());
            assertEquals(KalahaGame.Player.SECOND_PLAYER, optionalKalahGame.get().getPlayer().getOppositePlayer());
        }
    }

    @Test
    public void testForFindByInValidGameId() {
        assertEquals(Optional.empty(), gameRepository.findByGameId(0));
    }
}
