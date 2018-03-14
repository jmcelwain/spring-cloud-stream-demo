package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MessageConverter;
import org.w3c.dom.Document;

import static org.joox.JOOX.$;

@EnableBinding(DocumentProcessor.class)
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@StreamMessageConverter
	public MessageConverter documentMessageConverter() {
		return new DocumentMessageConverter();
	}

	@StreamListener(DocumentProcessor.INPUT)
	@Output(DocumentProcessor.OUTPUT)
	public Document process(Document message)  {
		return message;
	}
}
