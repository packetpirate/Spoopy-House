package com.spoopy;

import java.util.ArrayList;
import java.util.List;

import com.spoopy.entities.Player;
import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Pair;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
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
	public static final long NANO_TO_MS = 1000000L;
	private final double updateTime = 0.016667;
	
	private final KeyCodeCombination GO_FULLSCREEN = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
	
	private Stage mainStage;
	private Scene mainScene;
	
	private Canvas canvas = new Canvas(Game.CANVAS_WIDTH, Game.CANVAS_HEIGHT);
	private GraphicsContext gc = canvas.getGraphicsContext2D();
	
	private List<String> input;
	
	private TileMap tilemap;
	private Player player;
	
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
		
		input = new ArrayList<>();
		mainScene.setOnKeyPressed(keyPress);
		mainScene.setOnKeyReleased(keyRelease);
		
		tilemap = TileMap.ReadFromFile("map1.txt");
		player = new Player(tilemap.findStart());
		
		new AnimationTimer() {
			private long lastUpdate = 0;
			
			public void handle(long nanoTime) {
				long current = nanoTime / Game.NANO_TO_MS;
				if((((double)nanoTime - (double)lastUpdate) / 1000000000.0) >= updateTime) {
					long dt = current - lastUpdate;
					update(current, dt);
					render(current, dt);
					lastUpdate = current;
				}
			}
		}.start();
		
		mainStage.show();
	}
	
	private void update(long current, long delta) {
		// Check if we can move the player yet.
		if(input.contains("W") && player.canMove(tilemap, 0, -1, current)) player.move(0, -1, current);
		if(input.contains("A") && player.canMove(tilemap, -1, 0, current)) player.move(-1, 0, current);
		if(input.contains("S") && player.canMove(tilemap, 0, 1, current)) player.move(0, 1, current);
		if(input.contains("D") && player.canMove(tilemap, 1, 0, current)) player.move(1, 0, current);
	}
	
	private void render(long current, long delta) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, mainScene.getWidth(), mainScene.getHeight());
		
		// Render all tiles in the tilemap.
		TileMap.forAllTiles(0, (tilemap.getWidth() - 1), 
							0, (tilemap.getHeight() - 1), 
							(x, y) -> {
			Tile t = tilemap.getTile(new Pair<Integer>(x, y));
			if(t != null) t.render(gc);
		});
		
		// Render the player.
		player.render(gc);
	}
	
	EventHandler<KeyEvent> keyPress = (key) -> {
		String code = key.getCode().toString();
		if(!input.contains(code)) {
			input.add(code);
		}
	};
	
	EventHandler<KeyEvent> keyRelease = (key) -> {
		String code = key.getCode().toString();
		input.remove(code);
	};
}
