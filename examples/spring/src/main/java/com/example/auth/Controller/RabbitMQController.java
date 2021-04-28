package com.example.auth.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.rabbitmq.Sender;


@RestController
@CrossOrigin
@RequestMapping
public class RabbitMQController {

	@Autowired
	Sender sender; 
	
	@GetMapping("/rabbitmq/gettest")
	public String test() {
		return "test";
	}
	@PostMapping("/rabbitmq/messages") 
	public String sendMessage(final @RequestBody Map<String,String> req ) { 
		String message = req.get("message"); 
		try { 
			sender.send(message); 
			return "send message success"; 
		} catch (Exception e ) { 
			System.out.println(e.getMessage()); 
			return "send message fail"; 
			} 
		}
}
