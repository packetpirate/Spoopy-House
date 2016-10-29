package com.spoopy;

import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Pair;

import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game {
	public static final int CANVAS_WIDTH = 1280;
	public static final int CANVAS_HEIGHT = 720;
	private final double updateTime = 0.016667;
	
	private final KeyCodeCombination GO_FULLSCREEN = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
	
	private Stage mainStage;
	private Scene mainScene;
	
	private Canvas canvas = new Canvas(Game.CANVAS_WIDTH, Game.CANVAS_HEIGHT);
	private GraphicsContext gc = canvas.getGraphicsContext2D();
	
	private TileMap tilemap;
	
	public Game(Stage stage) {
		mainStage = stage;
		mainStage.requestFocus();
		mainStage.centerOnScreen();
		mainStage.setFullScreen(true);
		mainStage.setFullScreenExitHint("");
		
		Group root = new Group();
		mainScene = new Scene(root);
		mainStage.setScene(mainScene);
		
		root.getChildren().add(canvas);
		
		mainScene.widthProperty().addListener((obsv) -> {
			canvas.setWidth(mainScene.widthProperty().doubleValue());
		});
		mainScene.heightProperty().addListener((obsv) -> {
			canvas.setHeight(mainScene.heightProperty().doubleValue());
		});
		mainScene.addEventHandler(KeyEvent.KEY_RELEASED, (event) -> {
			if(GO_FULLSCREEN.match(event)) {
				boolean full = mainStage.fullScreenProperty().getValue();
				mainStage.setFullScreen(!full);
			}
		});
		
		tilemap = TileMap.ReadFromFile("map1.txt");
		
		new AnimationTimer() {
			private long lastUpdate = 0;
			
			public void handle(long nanoTime) {
				if((((double)nanoTime - (double)lastUpdate) / 1000000000.0) >= updateTime) {
					long dt = nanoTime - lastUpdate;
					update(nanoTime, dt);
					render(nanoTime, dt);
					lastUpdate = nanoTime;
				}
			}
		}.start();
		
		mainStage.show();
	}
	
	private void update(long current, long delta) {
		
	}
	
	private void render(long current, long delta) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, mainScene.getWidth(), mainScene.getHeight());
		
		// Render all tiles in the tilemap.
		TileMap.forAllTiles(0, (Game.CANVAS_WIDTH - 1), 
							0, (Game.CANVAS_HEIGHT - 1), 
							(x, y) -> {
			Tile t = tilemap.getTile(new Pair<Integer>(x, y));
			if(t != null) t.render(gc);
		});
	}
}
