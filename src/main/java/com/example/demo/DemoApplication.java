package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding(DemoApplication.DocumentProcessor.class)
@SpringBootApplication
public class DemoApplication {

	public interface DocumentProcessor {
		String INPUT = "docs";
		String OUTPUT = "processedDocs";

		@Input(INPUT)
		SubscribableChannel docs();

		@Output(OUTPUT)
		MessageChannel processedDocs();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Transformer(inputChannel = DocumentProcessor.INPUT, outputChannel = DocumentProcessor.OUTPUT)
	public String process(String message) {
		return message.toUpperCase();
	}
}
