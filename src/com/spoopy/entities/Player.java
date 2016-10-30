package com.spoopy.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Pair;
import com.spoopy.utils.Utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player {
	public static final long MOVE_DELAY = 250;
	
	private Pair<Integer> position;
	public Pair<Integer> getPosition() { return position; }
	public int getX() { return position.x; }
	public int getY() { return position.y; }
	public void setPosition(Pair<Integer> p) { position = p; }
	public void setX(int x) { position.x = x; }
	public void setY(int y) { position.y = y; }
	
	private long lastMove = 0;
	public boolean onMoveCD(long current) { return !((current - lastMove) >= Player.MOVE_DELAY); }
	public boolean canMove(TileMap tm, int xD, int yD, long current) {
		Tile t = tm.getTile(new Pair<Integer>((position.x + xD), (position.y + yD)));
		return (!onMoveCD(current) && (tm.inBounds((position.x + xD), (position.y + yD))) &&
			   ((t != null) && t.isPassable())); 
	}
	public void move(int xD, int yD, long current) {
		position.x += xD;
		position.y += yD;
		lastMove = current;
	}
	
	private Facing facing;
	public Facing getFacing() { return facing; }
	public void face(Facing f) { facing = f; }
	public Image getFacingSprite() {
		switch(facing) {
			case UP:    return images.get("up");
			case DOWN:  return images.get("down");
			case LEFT:  return images.get("left");
			case RIGHT: return images.get("right");
			default:    return null;
		}
	}
	
	private boolean interacting;
	public boolean isInteracting() { return interacting; }
	public void setInteracting(boolean b) { interacting = b; }
	
	private Set<Integer> keys;
	public Set<Integer> getKeys() { return keys; }
	public void clearKeys() { keys.clear(); }
	public boolean hasKey(int id) { return keys.contains(id); }
	public void addKey(int id) { keys.add(id); }
	public void remKey(int id) { keys.remove(id); }
	
	private Map<String, Image> images;
	public Image getImage(String state) { return images.get(state); }
	public void changeImage(String state, Image i) { images.put(state, i); }
	
	@SuppressWarnings("serial")
	public Player(Pair<Integer> p) {
		position = p;
		facing = Facing.DOWN;
		
		keys = new HashSet<>();
		
		images = new HashMap<String, Image>() {{
			put("up", Utils.LoadImage("professor_up.png"));
			put("down", Utils.LoadImage("professor_down.png"));
			put("left", Utils.LoadImage("professor_left.png"));
			put("right", Utils.LoadImage("professor_right.png"));
		}};
	}
	
	public void render(GraphicsContext gc) {
		Image facingSprite = getFacingSprite();
		if(facingSprite != null) gc.drawImage(facingSprite, (position.x * Tile.SIZE), 
				  							  (position.y * Tile.SIZE));
		else {
			gc.setFill(Color.BLUE);
			gc.fillOval(((position.x * Tile.SIZE) + 16), 
						((position.y * Tile.SIZE) + 16), 
						(Tile.SIZE - 32), (Tile.SIZE - 32));
		}
	}
}
