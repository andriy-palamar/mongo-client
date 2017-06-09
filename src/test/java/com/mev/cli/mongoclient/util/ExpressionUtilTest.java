package com.mev.cli.mongoclient.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExpressionUtilTest {
	
	@Test
	public void processProjections() {
		List<String> projections = new ArrayList<>();
		
		projections.add("name");
		projections.add("surname.*");
		
		List<String> newProjections = ExpressionUtil.processProjections(projections);

		assertThat(newProjections.get(0)).isEqualTo("name");
		assertThat(newProjections.get(1)).isEqualTo("surname");
	}
}
