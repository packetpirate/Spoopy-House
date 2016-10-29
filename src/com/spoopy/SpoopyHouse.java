package com.spoopy;

import javafx.application.Application;
import javafx.stage.Stage;

public class SpoopyHouse extends Application {
	public static void main(String[] args) {
		try {
			launch(args);
		} catch(Exception e) {
			System.err.println("ERROR: Could not launch application.");
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("The Spoopy House");
		new Game(stage);
	}
}
