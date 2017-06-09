package com.mev.cli.mongoclient.view.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.view.ConsolePrinter;

@Component
public class SQLConsolePrinter implements ConsolePrinter<List<String>> {

	@Override
	public void print(List<String> jsonResults) {
		if (jsonResults.size() != 0) {
			jsonResults.stream().forEach((o) -> System.out.println(o.toString()));
		} else {
			System.out.println("Found no values!");
		}
	}

}
