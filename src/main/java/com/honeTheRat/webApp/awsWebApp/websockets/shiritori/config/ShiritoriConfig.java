package com.honeTheRat.webApp.awsWebApp.websockets.shiritori.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos.ShiritoriGame;

@Configuration
public class ShiritoriConfig {
	@Bean
	public Map<String, ShiritoriGame> shiritoriGames(){
		return new HashMap<String, ShiritoriGame>();
	}
}
