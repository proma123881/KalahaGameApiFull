package com.kalah.mapper;

import com.kalah.exception.KalahaGameException;
import com.kalah.model.KalahaBoard;
import com.kalah.model.KalahaGame;
import com.kalah.model.MoveResponse;
import com.kalah.model.NewGameResponse;
import com.kalah.repository.KalahGameInMemoryRepository;
import com.kalah.repository.KalahGameInMemoryRepositoryImpl;
import com.kalah.service.ErrorContentService;
import com.kalah.service.KalahaGameBoardPlanner;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kalah.constant.KalahaGameConstant.GAME_ID_NOT_FOUND_GAME_DOES_NOT_EXIST_ERROR_CODE;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class KalahaGameMapperTest {
    private KalahaGameBoardPlanner kalahaGameBoardPlanner;
    private ErrorContentService errorContentService;
    private  PropertySource errorContent;
    private final KalahGameInMemoryRepository gameRepository = KalahGameInMemoryRepositoryImpl.getInstance();
    private KalahaGameMapper gameMapper;


    private PropertySource<?> errorContentPropertySource() throws IOException {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> applicationYamlPropertySource = loader.
                load("errorContent.yaml", new ClassPathResource("errors.yaml"));
        return applicationYamlPropertySource.get(0);
    }
    @Before
    public void setUp() throws IOException {

        gameMapper = new KalahaGameMapper(new MockHttpServletRequest());
        errorContent = errorContentPropertySource();
        errorContentService = new ErrorContentService(errorContent);
        KalahaGameBoardPlanner kalahaGameBoardPlanner = new KalahaGameBoardPlanner(errorContentService);
        Map<Integer, Integer> gameBoard = new HashMap<>();
        kalahaGameBoardPlanner.initializeGameBoard(gameBoard);
        KalahaGame game = KalahaGame.builder()
                .id(1234).board(gameBoard)
                .status(KalahaGame.GameStatus.IN_PROGRESS)
                .player(KalahaGame.Player.FIRST_PLAYER)
                .build();
        gameRepository.save(game);
    }

    @Test
    public void testForGetNewGameResponse() {
        KalahaGame kalahaGame = gameRepository.findByGameId(1234).orElseThrow(() ->
                new KalahaGameException(GAME_ID_NOT_FOUND_GAME_DOES_NOT_EXIST_ERROR_CODE,
                        errorContentService.getErrorDescription(
                                GAME_ID_NOT_FOUND_GAME_DOES_NOT_EXIST_ERROR_CODE,
                                Collections.singletonList(String.valueOf(1234))), HttpStatus.NOT_FOUND));
        NewGameResponse gameResponse = gameMapper.getNewGameResponse(kalahaGame);
        assertEquals("1234", gameResponse.getId());
        assertEquals("http://localhost:80/games/1234", gameResponse.getUri());
    }

    @Test
    public void testForGetMovedGameResponse() {
        KalahaGame kalahaGame = gameRepository.findByGameId(1234).orElseThrow(() ->
                new KalahaGameException(GAME_ID_NOT_FOUND_GAME_DOES_NOT_EXIST_ERROR_CODE,
                        errorContentService.getErrorDescription(
                                GAME_ID_NOT_FOUND_GAME_DOES_NOT_EXIST_ERROR_CODE,
                                Collections.singletonList(String.valueOf(1234))), HttpStatus.NOT_FOUND));
        Map<Integer, Integer> existingBoard = kalahaGame.getBoard();
        existingBoard.put(existingBoard.get(KalahaBoard.FIRST_KALAH_INDEX.getValue()), 20);
        existingBoard.put(existingBoard.get(KalahaBoard.SECOND_KALAH_INDEX.getValue()), 10);
        existingBoard.put(existingBoard.get(1), 2);
        kalahaGame.setBoard(existingBoard);
        gameRepository.save(kalahaGame);
        MoveResponse gameResponse = gameMapper.getMovedGameResponse(kalahaGame);
        assertEquals("1234", gameResponse.getId());
        assertEquals("http://localhost:80/games/1234", gameResponse.getUrl());
        MatcherAssert.assertThat(gameResponse.getStatus(), CoreMatchers.is(kalahaGame.getBoard()));
    }
}
