package com.kalah.service;

import com.kalah.exception.KalahaGameException;
import com.kalah.mapper.KalahaGameMapper;
import com.kalah.model.KalahaGame;
import com.kalah.model.MoveResponse;
import com.kalah.model.NewGameResponse;
import com.kalah.repository.KalahGameInMemoryRepository;
import com.kalah.repository.KalahGameInMemoryRepositoryImpl;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kalah.constant.KalahaGameConstant.GAME_ID_NOT_FOUND_GAME_DOES_NOT_EXIST_ERROR_CODE;
import static com.kalah.constant.KalahaGameConstant.GAME_TERMINATED_EXCEPTION_GAME_OVER_ERROR_CODE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

@SpringBootTest
public class KalahaGameServiceImplTest {

    private KalahaGameService kalahaGameService;

    private final KalahGameInMemoryRepository gameRepository = KalahGameInMemoryRepositoryImpl.getInstance();

    private KalahaGameBoardPlanner kalahaGameBoardPlanner;

    private  ErrorContentService errorContentService;


    private  PropertySource errorContent;

    private Map<Integer, Integer> gameBoard;


    private PropertySource<?> errorContentPropertySource() throws IOException {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> applicationYamlPropertySource = loader.
                load("errorContent.yaml", new ClassPathResource("errors.yaml"));
        return applicationYamlPropertySource.get(0);
    }
    @Before
    public void setUp() throws IOException {
        errorContent = errorContentPropertySource();
        kalahaGameBoardPlanner = new KalahaGameBoardPlanner(errorContentService);
        errorContentService = new ErrorContentService(errorContent);
        MockHttpServletRequest request = new MockHttpServletRequest();
        KalahaGameMapper gameMapper = new KalahaGameMapper(request);
        kalahaGameService = new KalahaGameServiceImpl(gameMapper, kalahaGameBoardPlanner, errorContentService);
    }


    @Test
    public void testForCreateGame() {
        KalahaGame game = kalahaGameService.createNewGame();
        assertEquals(1, game.getId());

    }

    @Test
    public void testToMovePlayerPitStones() {
        gameBoard = new HashMap<>();
        kalahaGameBoardPlanner.initializeGameBoard(gameBoard);
        KalahaGame game = KalahaGame.builder()
                .id(1234).board(gameBoard)
                .status(KalahaGame.GameStatus.IN_PROGRESS)
                .player(KalahaGame.Player.FIRST_PLAYER)
                .build();
        gameRepository.save(game);

        game = kalahaGameService.movePlayerPitStones(game.getId(), 3);
        assertEquals(1234, game.getId());
        assertEquals("[6, 6, 0, 7, 7, 7, 1, 7, 7, 6, 6, 6, 6, 0]", game.getBoard().values().toString());
        assertEquals(KalahaGame.GameStatus.IN_PROGRESS, game.getStatus());
    }



    @Test
    public void testForMovePlayerPitStonesForGameOver() {
        Map<Integer, Integer> board = new HashMap<>();
        kalahaGameBoardPlanner.initializeGameBoard(board);
        KalahaGame game = KalahaGame.builder()
                .id(1234).board(board)
                .status(KalahaGame.GameStatus.GAME_OVER)
                .player(KalahaGame.Player.FIRST_PLAYER).winner(KalahaGame.Player.FIRST_PLAYER.getName())
                .build();
        gameRepository.save(game);
        game = kalahaGameService.movePlayerPitStones(1234, 3);
        assertEquals("The Game is over! Winner is: First Player",game.getMessage());
        assertEquals(KalahaGame.Player.FIRST_PLAYER.getName(),game.getWinner());
    }

    @Test
    public void testForMovePlayerPitStonesForInvalidGameId() {
        gameBoard = new HashMap<>();
        kalahaGameBoardPlanner.initializeGameBoard(gameBoard);
        KalahaGame game = KalahaGame.builder()
                .id(1234).board(gameBoard)
                .status(KalahaGame.GameStatus.IN_PROGRESS)
                .player(KalahaGame.Player.FIRST_PLAYER)
                .build();
        KalahaGame savedGame = gameRepository.save(game);
        savedGame = kalahaGameService.movePlayerPitStones(savedGame.getId()+1, 3);
        assertNull(savedGame);
    }
}
