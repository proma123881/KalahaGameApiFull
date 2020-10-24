package com.kalah.controller;

import com.kalah.model.KalahaGame;
import com.kalah.wrapper.CreateModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import com.kal.game.bolkalaha.domain.Game;
//import com.demo.game.bolkalaha.service.GameService;
//import com.demo.game.bolkalaha.wrapper.CreateModelWrapper;
import com.kalah.service.KalahaGameService;

/**
 * The Class KalahaGameController is the controller to perform below operation
 * during the Kalaha game:
 * 1. Initialize the game
 * 2. Perform actions during a move
 * 3. Display messages
 * 4. Update model to show in frontend
 *
 * @author Proma Chowdhury
 * @version 1.0
 * @since 2020-10-24
 */
@Controller
public class KalahaGameController {

    private static final Logger log = LoggerFactory.getLogger(KalahaGameController.class);
    private final KalahaGameService kalahaGameService;
    private KalahaGame game;
    private CreateModelWrapper createModelWrapper;

    /**
     * Instantiates a new kalaha game controller.
     *  @param gameService        the game service
     * @param createModelWrapper the create model wrapper
     * @param kalahaGameService
     */
    @Autowired
    public KalahaGameController(KalahaGameService kalahaGameService, CreateModelWrapper createModelWrapper) {

        this.kalahaGameService = kalahaGameService;
        this.createModelWrapper = createModelWrapper;
    }

    /**
     * Game landing page.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/")
    public String gameLanding(Model model) {
        log.info("With in the game landing page method");
        return "landing";
    }

    /**
     * Starts and initialize the game.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/start")
    public String startGame(Model model) {
        log.info("Enter into the start game method");
        game = kalahaGameService.createNewGame();

        createModelWrapper.updateModel(model, game);
        log.info("Exit from the start game method");
        return "game";
    }

    /**
     * Performs the moves during the game.
     *
     * @param pitId the pit id
     * @param model the model
     * @return the string
     */
    @GetMapping("/play")
    public String moveGame(@RequestParam String pitId, Model model, String gameId) {
        log.info("Enter into the move game method");
        int clickedPitId = Integer.parseInt(pitId);
        int gameid = Integer.parseInt(gameId);
        log.info("Selected index for the move is :: {}", clickedPitId);
        game = kalahaGameService.movePlayerPitStones(gameid, clickedPitId);
        createModelWrapper.updateModel(model, game);
        log.info("Selected index for the move is :: {}", model.getAttribute("message"));
        log.info("Exit from the move game method");

        return "game";


    }
}
