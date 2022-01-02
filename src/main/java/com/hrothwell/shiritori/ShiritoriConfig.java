package com.hrothwell.shiritori;

import java.util.HashMap;
import java.util.Map;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hrothwell.shiritori.game.pojos.ShiritoriGame;

@Configuration
@EnableScheduling
public class ShiritoriConfig {
	@Bean
	public Map<String, ShiritoriGame> shiritoriGames(){
		return new HashMap<String, ShiritoriGame>();
	}
}
