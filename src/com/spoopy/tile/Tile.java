package com.spoopy.tile;

import com.spoopy.gfx.Viewport;
import com.spoopy.utils.Pair;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Tile {
	public static final int SIZE = 64;
	
	private TileType type;
	public TileType getTileType() { return type; }
	public void setTileType(TileType tt) { type = tt; }
	
	private Pair<Integer> position;
	public Pair<Integer> getPosition() { return position; }
	public int getX() { return position.x; }
	public int getY() { return position.y; }
	public void setPosition(Pair<Integer> p) { position = p; }
	public void setX(int x) { position.x = x; }
	public void setY(int y) { position.y = y; }
	
	private boolean passable;
	public boolean isPassable() { return passable; }
	public void makePassable(boolean b) { passable = b; }
	
	private Image image;
	public Image getImage() { return image; }
	public void changeImage(Image i) { image = i; }
	
	public Tile(TileType tt, int x, int y, boolean b) {
		this(tt, new Pair<Integer>(x, y), b);
	}
	
	public Tile(TileType tt, Pair<Integer> p, boolean b) {
		this(tt, p, b, null);
	}
	
	public Tile(TileType tt, Pair<Integer> p, boolean b, Image i) {
		type = tt;
		position = p;
		passable = b;
		image = i;
	}
	
	public void render(GraphicsContext gc, Viewport view) {
		Pair<Integer> aPosition = view.getAdjustedPosition(position);
		if(image != null) gc.drawImage(image, aPosition.x, aPosition.y);
		else {
			Color c = Color.BLACK;
			if     (type == TileType.START) c = Color.GREEN;
			else if(type == TileType.EMPTY) c = Color.WHITE;
			else if(type == TileType.ERROR) c = Color.RED;
			
			gc.setFill(c);
			gc.fillRect((aPosition.x * Tile.SIZE), 
						(aPosition.y * Tile.SIZE), 
						Tile.SIZE, Tile.SIZE);
		}
	}
}
