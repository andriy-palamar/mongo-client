package com.mev.cli.mongoclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import com.mongodb.MongoClient;

@SpringBootApplication
public class MongoClientApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(MongoClientApplication.class, args);

		final MongoClient mongoClient = applicationContext.getBean(MongoClient.class);

		applicationContext.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {

			@Override
			public void onApplicationEvent(ContextClosedEvent event) {
				mongoClient.close();
			}
		});
	}
}
