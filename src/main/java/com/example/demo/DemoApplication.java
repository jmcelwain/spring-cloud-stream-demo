package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringWriter;

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

	@Bean
	DocumentBuilder documentBuilder(@Autowired DocumentBuilderFactory documentBuilderFactory) throws ParserConfigurationException {
		return documentBuilderFactory.newDocumentBuilder();
	}

	@Bean
	DocumentBuilderFactory documentBuilderFactory() {
		return DocumentBuilderFactory.newInstance();
	}

	@Bean
	javax.xml.transform.Transformer transformer() throws TransformerConfigurationException {
		return TransformerFactory.newInstance().newTransformer();
	}

	@Autowired
	DocumentBuilder documentBuilder;

	@Autowired
	javax.xml.transform.Transformer transformer;

	@Transformer(inputChannel = DocumentProcessor.INPUT, outputChannel = DocumentProcessor.OUTPUT)
	public String process(String message) throws SAXException, IOException, TransformerException {
		Document doc = documentBuilder.parse(new StringBufferInputStream(message));
		Element newNode = doc.createElement("new-node");
		newNode.appendChild(doc.createTextNode("wow!"));
		doc.appendChild(newNode);

		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		return writer.toString();
	}
}
