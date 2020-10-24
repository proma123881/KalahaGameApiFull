package com.kalah.controller;

import com.kalah.model.KalahaGame;
import com.kalah.repository.KalahGameInMemoryRepository;
import com.kalah.repository.KalahGameInMemoryRepositoryImpl;
import com.kalah.service.ErrorContentService;
import com.kalah.service.KalahaGameBoardPlanner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@WebMvcTest
public class KalahaGameControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private  ErrorContentService errorContentService;
    private PropertySource errorContent;

    public MockMvc mockMvc;
    private final KalahGameInMemoryRepository gameRepository = KalahGameInMemoryRepositoryImpl.getInstance();

    @Before
    public void setUp() {

        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateNewGame() throws Exception {
        mockMvc.perform(post("/games"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testPageNotFound() throws Exception {
         mockMvc.perform(post("/gamess"))
                .andExpect(status().isNotFound());

    }
    @Test
    public void testMethodNotAllowed() throws Exception {
        mockMvc.perform(put("/games"))
                .andExpect(status().isMethodNotAllowed());

    }
    private PropertySource<?> errorContentPropertySource() throws IOException {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> applicationYamlPropertySource = loader.
                load("errorContent.yaml", new ClassPathResource("errors.yaml"));
        return applicationYamlPropertySource.get(0);
    }


    @Test
    public void testToMoveStones() throws Exception {
        Map<Integer, Integer> board = new HashMap<>();
        errorContent = errorContentPropertySource();
        errorContentService = new ErrorContentService(errorContent);
        KalahaGameBoardPlanner kalahaGameBoardPlanner = new KalahaGameBoardPlanner(errorContentService);
        kalahaGameBoardPlanner.initializeGameBoard(board);
        KalahaGame game = KalahaGame.builder()
                .id(1234).board(board)
                .status(KalahaGame.GameStatus.IN_PROGRESS)
                .player(KalahaGame.Player.FIRST_PLAYER)
                .build();
        gameRepository.save(game);

        mockMvc.perform(put("/games/1234/pits/3"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id").value(1234))
                .andExpect(content().json("{\"status\":{\"1\":6,\"2\":6,\"3\":0,\"4\":7,\"5\":7,\"6\":7,\"7\":1,\"8\":7,\"9\":7,\"10\":6,\"11\":6,\"12\":6,\"13\":6,\"14\":0}}"));

    }




}
