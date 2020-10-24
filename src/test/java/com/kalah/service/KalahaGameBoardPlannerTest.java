package com.kalah.service;

import com.google.common.primitives.Ints;
import com.kalah.model.KalahaBoard;
import com.kalah.model.KalahaGame;
import com.kalah.repository.KalahGameInMemoryRepository;
import com.kalah.repository.KalahGameInMemoryRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class KalahaGameBoardPlannerTest {

    private KalahaGameBoardPlanner kalahaGameBoardPlanner;
    private  ErrorContentService errorContentService;
    private  PropertySource errorContent;

    private Map<Integer, Integer> gameBoard;

    private final KalahGameInMemoryRepository gameRepository = KalahGameInMemoryRepositoryImpl.getInstance();

    private Class<? extends KalahaGameBoardPlanner> kalahGameBoard;

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
        kalahaGameBoardPlanner = new KalahaGameBoardPlanner(errorContentService);
        kalahGameBoard = kalahaGameBoardPlanner.getClass();
        gameBoard = new HashMap<>();

    }

    @Test
    public void testForInitializingGameBoard() {
        kalahaGameBoardPlanner.initializeGameBoard(gameBoard);
        assertEquals(14, gameBoard.size());
        IntStream.rangeClosed(KalahaBoard.FIRST_PIT_INDEX.getValue(), KalahaBoard.SECOND_KALAH_INDEX.getValue())
                .forEach(pit -> {
                    int noOfStones = gameBoard.get(pit);
                    if (pit == KalahaBoard.FIRST_KALAH_INDEX.getValue() || pit == KalahaBoard.SECOND_KALAH_INDEX.getValue()) {
                        assertEquals(0, noOfStones);
                    } else {
                        assertEquals(6, noOfStones);
                    }
                });
        gameBoard.forEach((pit, value) -> {
            if (pit == KalahaBoard.FIRST_KALAH_INDEX.getValue() || pit == KalahaBoard.SECOND_KALAH_INDEX.getValue()) {
                assertEquals(0, gameBoard.get(pit).intValue());
            } else {
                assertEquals(6, gameBoard.get(pit).intValue());
            }
        });
    }


    private void playScenario(PropertySource propertySource,String testCase) throws IOException, ClassNotFoundException {

        System.out.println(testCase);
        List<Integer> input = getKalahGameState(testCase+".input", propertySource);
        int pitToMove = (int)propertySource.getProperty(testCase+".pit");
        gameBoard = getGameBoardState(input);
        KalahaGame game = KalahaGame.builder()
                .id((Integer)propertySource.getProperty(testCase+".gameId")).board(gameBoard)
                .status(mapGameStatus(testCase+".gameStatus", propertySource))
                .player(mapPlayerTurn(testCase+".currentTurn", propertySource))
                .build();
        gameRepository.save(game);


        if(propertySource.getProperty(testCase+".error") == null){
            kalahaGameBoardPlanner.movePitStones(pitToMove, game);
            List<Integer> output = getKalahGameState(testCase+".output", propertySource);
            assertEquals(mapPlayerTurn(testCase+".nextTurn", propertySource).getName(), game.getPlayer().getName());
            gameBoard.forEach((pit, value) -> {
                assertEquals(output.get(pit-1).intValue(), gameBoard.get(pit).intValue());
            });
        } else
        {
            String exceptionClass = (String)propertySource.getProperty(testCase+".errorClass");
            String errorCode = (String)propertySource.getProperty(testCase+".error");
            assertThatThrownBy(() ->  kalahaGameBoardPlanner.movePitStones(pitToMove, game))
                    .isInstanceOf(Class.forName(exceptionClass))
                    .hasMessageContaining(errorContentService.getErrorDescription(errorCode, Collections.singletonList(game.getWinner())));

        }

    }
    @Test
    public void testFromYaml() throws IOException, ClassNotFoundException {

        PropertySource propertySource = loadTestDataFile();
        String[] testCases = ((String)propertySource.getProperty("Keys")).split(",");
        for(int i = 0; i < testCases.length; i++) {
            playScenario(propertySource,testCases[i]);
        }
    }

    private List<Integer> getKalahGameState(String propertyName, PropertySource propertySource) {

        return Ints.asList(Stream.of(((String) propertySource.getProperty(propertyName)).split(","))
                .mapToInt(Integer::parseInt)
                .toArray());
    }

    private Map<Integer, Integer> getGameBoardState(List<Integer> pitList) {

        int i = 0;
        for (Integer pit : pitList) {
            gameBoard.put(++i, pit);
        }
        return gameBoard;
    }

    private KalahaGame.GameStatus mapGameStatus(String propertyName, PropertySource propertySource) {

       String gameStatus = (String)propertySource.getProperty(propertyName);
       if(gameStatus.equalsIgnoreCase("inProgress"))
           return KalahaGame.GameStatus.IN_PROGRESS;
       else if(gameStatus.equalsIgnoreCase("gameOver"))
           return KalahaGame.GameStatus.GAME_OVER;
       else
           return KalahaGame.GameStatus.DRAW;
    }


    private KalahaGame.Player mapPlayerTurn(String propertyName, PropertySource propertySource) {

        String palyerTurn = (String)propertySource.getProperty(propertyName);
        if(palyerTurn.equalsIgnoreCase("firstPlayer"))
            return KalahaGame.Player.FIRST_PLAYER;
        else
            return KalahaGame.Player.SECOND_PLAYER;
    }

    private PropertySource<?> loadTestDataFile() throws IOException {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> applicationYamlPropertySource = loader.
                load("test.yaml", new ClassPathResource("testData.yaml"));
        return applicationYamlPropertySource.get(0);
    }




}
