package com.example.auth.Controller;

import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@PostMapping("rabbitmq/send/queue") 
	public String sendQueue(final @RequestBody Map<String, String> req) {
		String queueName = req.get("queueName");
		String message = req.get("message");
		System.out.println("queue : " + queueName);
		System.out.println("message : " + message);
		
		try {
			amqpTemplate.convertAndSend(queueName, message);
			return "sendQueue success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "sendQueue fail";
		}
	}
	
	@PostMapping("rabbitmq/send/fanout") 
	public String sendExchange(final @RequestBody Map<String, String> req) {
		String message = req.get("message");
		String fanoutName = req.get("fanoutName");

		System.out.println("fanoutName : " + fanoutName);
		System.out.println("message : " + message);
		
		try {	
			amqpTemplate.convertAndSend(fanoutName, "", message);
			return "sendExchange success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "sendExchange fail";
		}
	}
	@PostMapping("/rabbitmq/declare/queue")
	public String queueDeclare(final @RequestBody Map<String, String> req) {
		String queueName = req.get("queueName");
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
	
	@PostMapping("/rabbitmq/delete/queue")
	public String queueDelete(final @RequestBody Map<String, String> req) {
		String queueName = req.get("queueName");
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
	
	@PostMapping("/rabbitmq/declare/topic")
	public String topicDeclare(final @RequestBody Map<String, String> req) {
		String topicName = req.get("topicName");
		System.out.println("topic : " + topicName);
		try {
			TopicExchange topic = new TopicExchange(topicName);
			admin.declareExchange(topic);
			return "declareExchange success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "declareExchange fail";
		}
	}
	
	@PostMapping("/rabbitmq/delete/topic")
	public String topicDelete(final @RequestBody Map<String, String> req) {
		String topicName = req.get("topicName");
		System.out.println("topicName : " + topicName);
		try {
			admin.deleteExchange(topicName);
			return "delete success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "declare fail";
		}
	}
	
	@PostMapping("/rabbitmq/declare/fanout")
	public String fanoutDeclare(final @RequestBody Map<String, String> req) {
		String fanoutName = req.get("fanoutName");
		System.out.println("topic : " + fanoutName);
		try {
			FanoutExchange topic = new FanoutExchange(fanoutName);
			admin.declareExchange(topic);
			return "declare FanoutExchange success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "declare FanoutExchange fail";
		}
	}
	
	@PostMapping("/rabbitmq/delete/fanout")
	public String fanoutDelete(final @RequestBody Map<String, String> req) {
		String fanoutName = req.get("fanoutName");
		System.out.println("fanoutName : " + fanoutName);
		try {
			admin.deleteExchange(fanoutName);
			return "delete FanoutExchange success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "declare FanoutExchange fail";
		}
	}
	
	@PostMapping("/rabbitmq/declare/bind")
	public String bind(final @RequestBody Map<String, String> req) {
		String fanoutName = req.get("fanoutName");
		String queueName = req.get("queueName");

		System.out.println("fanout : " + fanoutName);
		System.out.println("queue : " + queueName);

		try {
			Queue queue = new Queue(queueName, false);
			FanoutExchange fanout = new FanoutExchange(fanoutName);
			Binding bind = BindingBuilder.bind(queue).to(fanout);
			admin.declareBinding(bind);
			return "declareExchange success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "declareExchange fail";
		}
	}
	
	@PostMapping("/rabbitmq/delete/bind")
	public String deleteBind(final @RequestBody Map<String, String> req) {
		String fanoutName = req.get("fanoutName");
		String queueName = req.get("queueName");

		System.out.println("fanout : " + fanoutName);
		System.out.println("queue : " + queueName);

		try {
			Queue queue = new Queue(queueName, false);
			FanoutExchange fanout = new FanoutExchange(fanoutName);
			Binding bind = BindingBuilder.bind(queue).to(fanout);
			admin.removeBinding(bind);
			return "declareExchange success";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "declareExchange fail";
		}
	}
	
	

	
}
