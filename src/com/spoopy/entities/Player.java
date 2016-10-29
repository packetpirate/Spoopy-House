package com.spoopy.entities;

import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Pair;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player {
	public static final long MOVE_DELAY = 200;
	
	private Pair<Integer> position;
	public Pair<Integer> getPosition() { return position; }
	public int getX() { return position.x; }
	public int getY() { return position.y; }
	public void setPosition(Pair<Integer> p) { position = p; }
	public void setX(int x) { position.x = x; }
	public void setY(int y) { position.y = y; }
	
	private long lastMove = 0;
	public boolean canMove(TileMap tm, int xD, int yD, long current) {
		Tile t = tm.getTile(new Pair<Integer>((position.x + xD), (position.y + yD)));
		return ((current - lastMove) >= Player.MOVE_DELAY) &&
			   (tm.inBounds((position.x + xD), (position.y + yD))) &&
			   ((t != null) && t.isPassable()); 
	}
	public void move(int xD, int yD, long current) {
		position.x += xD;
		position.y += yD;
		lastMove = current;
	}
	
	private Image image;
	public Image getImage() { return image; }
	public void changeImage(Image i) { image = i; }
	
	public Player(Pair<Integer> p) {
		position = p;
	}
	
	public void render(GraphicsContext gc) {
		if(image != null) gc.drawImage(image, (position.x * Tile.SIZE), (position.y * Tile.SIZE));
		else {
			gc.setFill(Color.BLUE);
			gc.fillOval(((position.x * Tile.SIZE) + 16), ((position.y * Tile.SIZE) + 16), 
						(Tile.SIZE - 32), (Tile.SIZE - 32));
		}
	}
}
