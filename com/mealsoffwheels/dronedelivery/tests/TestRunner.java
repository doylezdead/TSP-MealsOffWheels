package com.mealsoffwheels.dronedelivery.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

  	public static void main(String[] args) {
		System.out.println("JUnit testing\nTeam Software Project\nMeals off Wheels\n===================\n");
		Result resultFixed = JUnitCore.runClasses(ClientTests.class);
		for (Failure failure : resultFixed.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("\n===================");
	}
}
