package com.example.auth.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Listner {
	@RabbitListener(queues = "TestQ") 
	public void processMessage(String content) { 
		System.out.println(content);
	}

}
