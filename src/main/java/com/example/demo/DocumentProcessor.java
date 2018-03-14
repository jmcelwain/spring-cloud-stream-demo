package com.example.demo;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface DocumentProcessor {
    String INPUT = "docs";
    String OUTPUT = "processedDocs";

    @Input(INPUT)
    SubscribableChannel docs();

    @Output(OUTPUT)
    MessageChannel processedDocs();
}
