package com.spoopy.tile;

import com.spoopy.entities.GameObject;
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
	public boolean isPassable() { 
		return (passable || ((object != null) && object.isPassable()));
	}
	public void makePassable(boolean b) { passable = b; }
	
	private GameObject object;
	public GameObject getObject() { return object; }
	public void setObject(GameObject obj) { 
		object = obj;
		if(object != null) passable = object.isPassable();
		else passable = type.isPassable();
	}
	public void unsetObject() { object = null; }
	
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
		object = null;
		image = i;
	}
	
	public void render(GraphicsContext gc) {
		if(image != null) gc.drawImage(image, (position.x * Tile.SIZE), 
											  (position.y * Tile.SIZE));
		else {
			Color c = Color.BLACK;
			if     (type == TileType.START) c = Color.DARKGREEN;
			else if(type == TileType.EMPTY) c = Color.WHITE;
			else if(type == TileType.WALL)  c = Color.BROWN;
			else if(type == TileType.EXIT)  c = Color.GREEN;
			else if(type == TileType.ERROR) c = Color.RED;
			
			gc.setFill(c);
			gc.fillRect((position.x * Tile.SIZE), 
						(position.y * Tile.SIZE), 
						Tile.SIZE, Tile.SIZE);
		}
		
		if(object != null) object.render(gc, position);
	}
}
