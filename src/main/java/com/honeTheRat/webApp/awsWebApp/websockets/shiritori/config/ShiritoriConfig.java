package com.honeTheRat.webApp.awsWebApp.websockets.shiritori.config;

import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos.ShiritoriGame;

@Configuration
public class ShiritoriConfig {
	@Bean
	public HashMap<String, ShiritoriGame> shiritoriGames(){
		return new HashMap<String, ShiritoriGame>();
	}
}
