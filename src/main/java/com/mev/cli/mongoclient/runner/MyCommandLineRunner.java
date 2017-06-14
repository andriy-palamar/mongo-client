package com.mev.cli.mongoclient.runner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.executor.MongoExecutor;
import com.mev.cli.mongoclient.view.ConsolePrinter;

@Component
@Profile("!test")
public class MyCommandLineRunner implements CommandLineRunner {

	private MongoExecutor<List<String>> mongoExecutor;

	private ConsolePrinter<List<String>> consolePrinter;

	@Autowired
	public MyCommandLineRunner(MongoExecutor<List<String>> mongoExecutor,
			ConsolePrinter<List<String>> consolePrinter) {
		this.mongoExecutor = mongoExecutor;
		this.consolePrinter = consolePrinter;
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		do {
			System.out.print("> ");
			
			try{
				String sqlQuery = br.readLine();
				
				List<String> object = mongoExecutor.execute(sqlQuery);
				
				consolePrinter.print(object);
			} catch (Exception e) {
				System.out.println("Wrong query!");
			}
			System.out.println();
		} while(true);
	}
	
}
