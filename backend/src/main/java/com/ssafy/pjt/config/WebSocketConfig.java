package com.ssafy.pjt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
//		config.enableSimpleBroker("/ws");
//		config.setApplicationDestinationPrefixes("/app");
//		config.enableStompBrokerRelay("/topic")
//		.setRelayHost("http://k4d106.p.ssafy.io")
//		.setRelayPort(5672)
//		.setSystemLogin("illeum")
//		.setSystemPasscode("illeum123");
		config.enableStompBrokerRelay("/topic", "/queue")
        .setRelayHost("http://k4d106.p.ssafy.io")
        .setRelayPort(5672)
        .setSystemLogin("illeum")
        .setSystemPasscode("illeum123")
        .setClientLogin("illeum")
        .setClientPasscode("illeum123");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();
	}
}
