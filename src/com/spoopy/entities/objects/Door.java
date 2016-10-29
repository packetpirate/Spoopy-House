package com.spoopy.entities.objects;

import com.spoopy.entities.Facing;
import com.spoopy.entities.GameObject;

import javafx.scene.image.Image;

public class Door extends GameObject {
	private boolean locked;
	public boolean isLocked() { return locked; }
	public void lock() { locked = true; }
	public void unlock() { locked = false; }
	
	public Door(boolean b, Facing f, Image i) {
		super(b, f, i);
		
	}

	@Override
	public void interact(Facing f) {
		
	}
}
