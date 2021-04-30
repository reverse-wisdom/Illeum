package com.example.auth.Controller;

import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping
public class RabbitMQController {
	
	@Autowired
	private AmqpAdmin admin;

	@PostMapping("/rabbitmq/declare")
	public String declare(final @RequestBody Map<String, String> req) {
		String queueName = req.get("queue");
		System.out.println("queue : " + queueName);
		try {
			Queue queue = new Queue(queueName, false);
			admin.declareQueue(queue);
			//template.convertAndSend(queue, "test declare"); 
			return "declare success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "declare fail";
		}
	}
	
	@PostMapping("/rabbitmq/delete")
	public String delete(final @RequestBody Map<String, String> req) {
		String queueName = req.get("queue");
		System.out.println("queue : " + queueName);
		try {
			//Queue queue = new Queue(queueName, false);
			admin.deleteQueue(queueName);
			//template.convertAndSend(queue, "test declare"); 
			return "delete success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "declare fail";
		}
	}
	
}
