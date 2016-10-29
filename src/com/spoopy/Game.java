package com.spoopy;

import java.util.ArrayList;
import java.util.List;

import com.spoopy.entities.Facing;
import com.spoopy.entities.Player;
import com.spoopy.entities.objects.Door;
import com.spoopy.gfx.Viewport;
import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Pair;
import com.spoopy.utils.Utils;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game {
	public static final int CANVAS_WIDTH = 1280;
	public static final int CANVAS_HEIGHT = 704;
	public static final long NANO_TO_MS = 1000000L;
	private final double updateTime = 0.016667;
	
	private final KeyCodeCombination GO_FULLSCREEN = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
	
	private Stage mainStage;
	private Scene mainScene;
	
	private Canvas canvas = new Canvas(Game.CANVAS_WIDTH, Game.CANVAS_HEIGHT);
	private GraphicsContext gc = canvas.getGraphicsContext2D();
	
	private List<String> input;
	
	private TileMap tilemap;
	private Viewport view;
	private Player player;
	
	@SuppressWarnings("serial")
	public Game(Stage stage) {
		mainStage = stage;
		mainStage.requestFocus();
		mainStage.centerOnScreen();
		mainStage.setFullScreen(true);
		mainStage.setFullScreenExitHint("");
		mainStage.setFullScreenExitKeyCombination(GO_FULLSCREEN);
		
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
			} else if(event.getCode().equals(KeyCode.ESCAPE)) {
				mainStage.close();
			}
		});
		
		input = new ArrayList<>();
		mainScene.setOnKeyPressed(keyPress);
		mainScene.setOnKeyReleased(keyRelease);
		
		tilemap = TileMap.ReadFromFile("map1.txt");
		player = new Player(tilemap.findStart());
		
		Pair<Integer> viewDimensions = new Pair<Integer>((int)(canvas.getWidth() / Tile.SIZE), 
											  			 (int)(canvas.getHeight() / Tile.SIZE));
		view = new Viewport(new Pair<Integer>((player.getX() - (viewDimensions.x / 2)), 
											  (player.getY() - (viewDimensions.y / 2))), 
							viewDimensions,
							new Pair<Integer>(tilemap.getWidth(), tilemap.getHeight()));
		
		{ // Add doors to test.
			Image door = Utils.LoadImage("door_01.png");
			tilemap.getTile(new Pair<Integer>(13, 10))
				   .setObject(new Door(false, true, new ArrayList<Facing>() {{ add(Facing.UP); add(Facing.DOWN); }}, 
						   			   door, true));
			tilemap.getTile(new Pair<Integer>(14, 10))
				   .setObject(new Door(false, true, new ArrayList<Facing>() {{ add(Facing.UP); add(Facing.DOWN); }}, 
			   			   			   door, false));
		} // End door adds.
		
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
		if(input.contains("W")) {
			if(player.canMove(tilemap, 0, -1, current)) {
				player.move(0, -1, current);
				view.move(0, -1, player.getPosition());
			}
			player.face(Facing.getFacing(0, -1));
		}
		if(input.contains("A")) {
			if(player.canMove(tilemap, -1, 0, current)) {
				player.move(-1, 0, current);
				view.move(-1, 0, player.getPosition());
			}
			player.face(Facing.getFacing(-1, 0));
		}
		if(input.contains("S")) {
			if(player.canMove(tilemap, 0, 1, current)) {
				player.move(0, 1, current);
				view.move(0, 1, player.getPosition());
			}
			player.face(Facing.getFacing(0, 1));
		}
		if(input.contains("D")) {
			if(player.canMove(tilemap, 1, 0, current)) {
				player.move(1, 0, current);
				view.move(1, 0, player.getPosition());
			}
			player.face(Facing.getFacing(1, 0));
		}
		if(player.isInteracting()) {
			boolean interacted = false;
			outer:for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					if((i != 0) && (j != 0)) continue;
					Tile t = tilemap.getTile(new Pair<Integer>((player.getX() + i), (player.getY() + j)));
					if((t != null) && (t.getObject() != null)) {
						if(t.getObject().interact(player)) {
							interacted = true;
							break outer;
						}
					}
				}
			}
			if(!interacted) System.out.println("There was nothing to interact with!");
			player.setInteracting(false);
		}
	}
	
	private void render(long current, long delta) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, mainScene.getWidth(), mainScene.getHeight());
		
		// Save the transform so we can transform everything according to the viewport.
		gc.save();
		gc.translate(-(view.getX() * Tile.SIZE), -(view.getY() * Tile.SIZE));
		
		// Render all tiles in the tilemap.
		TileMap.forAllTiles(0, (tilemap.getWidth() - 1), 
							0, (tilemap.getHeight() - 1), 
							(x, y) -> {
			Tile t = tilemap.getTile(new Pair<Integer>(x, y));
			if(t != null) t.render(gc);
		});
		
		// Render the player.
		player.render(gc);
		
		// Restore the transform.
		gc.restore();
	}
	
	EventHandler<KeyEvent> keyPress = (key) -> {
		String code = key.getCode().toString();
		if(!input.contains(code)) {
			if(!key.getCode().equals(KeyCode.SPACE)) input.add(code);
		}
	};
	
	EventHandler<KeyEvent> keyRelease = (key) -> {
		String code = key.getCode().toString();
		if(key.getCode().equals(KeyCode.SPACE)) player.setInteracting(true);
		else input.remove(code);
	};
}
