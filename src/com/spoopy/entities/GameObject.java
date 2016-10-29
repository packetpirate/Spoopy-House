package com.spoopy.entities;

import java.util.List;

import com.spoopy.utils.Pair;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GameObject {
	private boolean passable;
	public boolean isPassable() { return passable; }
	public void makePassable(boolean b) { passable = b; }
	
	private boolean actionable;
	public boolean isActionable() { return actionable; }
	public boolean isActionable(Facing f) { return (actionable && directions.contains(f)); }
	public void makeActionable(boolean b) { actionable = b; }
	
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
		directions = d;
		image = i;
	}
	
	public abstract boolean interact(Player player);
	
	public void render(GraphicsContext gc, Pair<Integer> position) {
		if(image != null) gc.drawImage(image, position.x, position.y);
		else {
			
		}
	}
}
