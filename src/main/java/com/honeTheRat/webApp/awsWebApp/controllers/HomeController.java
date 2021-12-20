package com.honeTheRat.webApp.awsWebApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
	@GetMapping("/")
	public String noDirectory(Model model) {
		model.addAttribute("name", "World");
		return "home";
	}
	
	/**
	 * 
	 * @param name = query paramater
	 * @param model = the thing that is accessible via the view template?
	 * @return
	 */
	@GetMapping("/home")
	public String homePage(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "home"; //does this matter what it is? should it refer to the view's name? 
	}
}
