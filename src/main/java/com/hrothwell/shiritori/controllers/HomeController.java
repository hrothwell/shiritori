package com.hrothwell.shiritori.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
	@GetMapping("/")
	public String noDirectory(Model model) {
		model.addAttribute("pageTitle", "home page");
		return "home.html";
	}
	
	/**
	 * 
	 * @param name = query paramater
	 * @param model = the thing that is accessible via the view template?
	 * @return
	 */
	@GetMapping("/home")
	public String homePage(Model model) {
		model.addAttribute("pageTitle", "home page");
		//should match the "template" name I believe. File extension not always necessary but it was needed for a .txt file 
		//so I thought it would be worthwhile to add it here for now also. TODO: look into defining my own ThymeleafViewResolver or ServletContextTemplateResolver (seems cleaner)
		return "home.html";
	}
}
