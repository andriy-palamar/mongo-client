package com.mev.cli.mongoclient.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ExpressionUtilTest {
	
	@Test
	public void whenThereIsProjectionWithAsteriskTest() {
		//arrange
		List<String> projections = new ArrayList<>();
		
		projections.add("name");
		projections.add("surname.*");
		
		//act
		List<String> newProjections = ExpressionUtil.processProjections(projections);

		//assert
		assertThat(newProjections.get(0)).isEqualTo("name");
		assertThat(newProjections.get(1)).isEqualTo("surname");
	}
}
