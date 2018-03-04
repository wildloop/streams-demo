package com.github.wildloop.streamsdemo;

import org.slf4j.Logger;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@EnableBinding(Processor.class)
public class MessagingProcessorService {
	private static final Logger log = getLogger(MessagingProcessorService.class);

	//@StreamListener(Processor.INPUT)	@SendTo(Processor.OUTPUT)
	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Message<byte[]> demoProcessor(@Payload final byte[] body, @Headers Map<String, Object> headers) {
		log.info("Processor");
		return MessageBuilder.withPayload(body).copyHeaders(headers).build();
	}
}

