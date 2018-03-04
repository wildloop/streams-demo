package com.github.wildloop.streamsdemo;

import org.slf4j.Logger;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class MessagingConfiguration {
	private static final Logger log = getLogger(MessagingConfiguration.class);
	private final UUID uuid = UUID.randomUUID();
	private final String instanceId = uuid.toString().substring(0, 8); // TODO: Consul/Eureka
	private final String connectionNamePrefix;
	private AtomicInteger connectionNumber = new AtomicInteger(0);

	public MessagingConfiguration(@Value("${spring.rabbitmq.connection-name-prefix:SpringBootApp}") String connectionNamePrefix) {
		this.connectionNamePrefix = connectionNamePrefix;
	}

	private String generateConnectionName() {
		String connectionName = connectionNamePrefix + '#' + instanceId + ':' + connectionNumber.getAndIncrement();
		log.info("Messaging connection name: " + connectionName + " (uuid " + uuid + ")");
		return connectionName;
	}

	@Bean
	public SmartInitializingSingleton reconfigureConnectionFactory(final AbstractConnectionFactory cf) {
		return () -> cf.setConnectionNameStrategy(f -> generateConnectionName());
	}
}
