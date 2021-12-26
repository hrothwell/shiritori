package com.honeTheRat.webApp.awsWebApp.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos.CreateAndJoinGameBody;
import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos.ShiritoriGame;

/**
 * Controller for creating/joining a Shiritori
 * @author hrothwell
 *
 */
@Controller
public class ShiritoriGameCreateAndJoin {
	
	@Autowired
	private HashMap<String, ShiritoriGame> shiritoriGames;
	//TODO set content type? 
	@PostMapping("/shiritori/createGame")
	public String createShiritoriGame(@RequestBody CreateAndJoinGameBody body, Model model) {
		//TODO create a new game with name, add password or not
		//make game object, pass into view
		if(shiritoriGames.get(body.getGameName()) == null) {
			//make game, attach to model
			ShiritoriGame newGame = new ShiritoriGame();
			newGame.setGameName(body.getGameName());
			//TODO if it is null will that affect joining? 
			newGame.setGamePassword(StringUtils.trim(body.getPassword()));
			newGame.getPlayers().add("TODO");
			model.addAttribute("game", newGame);
		}
		else {
			//game already exists
		}
		//returns just a fragment, kinda cool
		return "shiritori.html :: shiritori"; 
	}
	
	@GetMapping("/shiritori/joinGame/{gameName}")
	public String joinShiritoriGame(@PathVariable String gameName, @RequestParam(name="password", required=false, defaultValue="") String password,
			@RequestParam(name="userName", required=false, defaultValue="NO_NAME") String userName, Model model) {
		ShiritoriGame g = shiritoriGames.get(gameName);
		if(g != null && g.getGamePassword().equals(password)) {
			//if name is NO_NAME generate a random name? Check for users with their name and if one exists, try to make it unique somehow in order for them to join? 
			//join game
			//pass game object into view
			model.addAttribute("game", null);
			model.addAttribute("pageTitle", "title");
			return "shiritori.html";
		}
		else {
			throw new RuntimeException("Incorrect password or game does not exist!");
		}
	}
	
}
