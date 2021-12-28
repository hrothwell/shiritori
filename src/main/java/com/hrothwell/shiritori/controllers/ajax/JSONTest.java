package com.hrothwell.shiritori.controllers.ajax;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class JSONTest {
	private Logger log = LoggerFactory.getLogger(JSONTest.class);
	@GetMapping("/jsontest")
	public String getBasicJson(Model model) {
		List<Integer> list = Arrays.asList(1,2,3,4);
		//initialize to empty object to start with
		String json = "{}";
		try {
			json = new ObjectMapper().writeValueAsString(list);
		}
		catch(JsonProcessingException e) {
			log.error("Error parsing json");
		}
		log.info("JSON: {}", json);
		model.addAttribute("json",json);
		return "jsonResult.txt";
	}
}
