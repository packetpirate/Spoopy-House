package com.spoopy.entities;

import java.util.List;

import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Pair;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class GameObject {
	private boolean passable;
	public boolean isPassable() { return passable; }
	public void makePassable(boolean b) { passable = b; }
	
	private boolean actionable;
	public boolean isActionable() { return actionable; }
	public boolean isActionable(Facing f) { return (actionable && directions.contains(f)); }
	public void makeActionable(boolean b) { actionable = b; }
	
	private boolean pushable;
	public boolean isPushable() { return pushable; }
	public boolean isPushable(Facing f, Pair<Integer> p, TileMap tm) {
		Tile n = Facing.nextTile(f, p, tm);
		return (pushable && (n != null) && (n.getPosition() != p) && 
			   (n.getObject() == null) && n.isPassable());
	}
	public void makePushable(boolean b) { pushable = b; }
	
	private List<Facing> directions;
	public List<Facing> getDirections() { return directions; }
	public void addFacing(Facing f) { directions.add(f); }
	public void remFacing(Facing f) { directions.remove(f); }
	
	private Image image;
	public Image getImage() { return image; }
	public void changeImage(Image i) { image = i; }
	
	public GameObject(boolean p, boolean b, List<Facing> d) {
		this(p, b, d, null);
	}
	
	public GameObject(boolean p, boolean b, List<Facing> d, Image i) {
		passable = p;
		actionable = b;
		pushable = false;
		directions = d;
		image = i;
	}
	
	public abstract boolean interact(Player player, TileMap tm, long current);
	
	public void render(GraphicsContext gc, Pair<Integer> position) {
		if(image != null) gc.drawImage(image, (position.x * Tile.SIZE), (position.y * Tile.SIZE));
		else {
			int x = (position.x * Tile.SIZE) + 16;
			int y = (position.y * Tile.SIZE) + 16;
			int w = Tile.SIZE / 2;
			int h = Tile.SIZE / 2;
			gc.setStroke(Color.BLACK);
			gc.setFill(Color.YELLOW);
			gc.fillRect(x, y, w, h);
			gc.strokeRect(x, y, w, h);
		}
	}
}
