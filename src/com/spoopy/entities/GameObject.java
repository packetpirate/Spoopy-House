package com.spoopy.entities;

import com.spoopy.utils.Pair;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GameObject {
	private boolean actionable;
	public boolean isActionable() { return actionable; }
	public void makeActionable(boolean b) { actionable = b; }
	
	private Facing actionableDirection;
	public Facing getActionableDir() { return actionableDirection; }
	public void setActionableDir(Facing f) { actionableDirection = f; }
	
	private Image image;
	public Image getImage() { return image; }
	public void changeImage(Image i) { image = i; }
	
	public GameObject(boolean b, Facing f) {
		this(b, f, null);
	}
	
	public GameObject(boolean b, Facing f, Image i) {
		actionable = b;
		actionableDirection = f;
		image = i;
	}
	
	public abstract void interact(Facing f);
	
	public void render(GraphicsContext gc, Pair<Integer> position) {
		if(image != null) gc.drawImage(image, position.x, position.y);
		else {
			
		}
	}
}
