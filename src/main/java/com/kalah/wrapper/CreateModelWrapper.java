package com.kalah.wrapper;


import com.kalah.model.KalahaGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CreateModelWrapper {

	private static final Logger log = LoggerFactory.getLogger(CreateModelWrapper.class);

	@Value("${kalaha.pits.per.player}")
	private int noOfPitsPerPlayer;

	/**
	 * Update model object to create the front end
	 *
	 * @param model the model
	 * @param game  the game
	 */
	public Model updateModel(Model model, KalahaGame game) {
		if(game != null) {
			model.addAttribute("player", game.getPlayer().getName());
			model.addAttribute("game", game);
			model.addAttribute("winner", game.getWinner());
			model.addAttribute("pits", game.getBoard().values().toArray());
			model.addAttribute("message", game.getMessage());
			model.addAttribute("gameId", game.getId());
		} else {
			model.addAttribute("message", new String("Game does not exist"));
		}
		return model;
	}
}
