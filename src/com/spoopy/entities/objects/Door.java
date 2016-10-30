package com.spoopy.entities.objects;

import java.util.List;

import com.spoopy.entities.Facing;
import com.spoopy.entities.GameObject;
import com.spoopy.entities.Player;
import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Pair;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Door extends GameObject {
	private int doorID;
	public int getDoorID() { return doorID; }
	public void changeDoorID(int id) { doorID = id; }
	
	private boolean open;
	public boolean isOpen() { return open; }
	public void open() { open = true; }
	public void close() { open = false; }
	
	private boolean locked;
	public boolean isLocked() { return locked; }
	public void lock() { locked = true; }
	public void unlock() { locked = false; }
	
	public Door(boolean p, boolean b, List<Facing> d, Image i) {
		this(p, b, d, i, false, 0);
	}
	
	public Door(boolean p, boolean b, List<Facing> d, Image i, boolean l, int id) {
		super(p, b, d, i);
		open = false;
		locked = l;
		doorID = id;
	}

	@Override
	public boolean interact(Player player, TileMap tm, long current) {
		if(isActionable(Facing.getOpposite(player.getFacing()))) {
			if(isLocked()) {
				if(player.hasKey(getDoorID())) {
					unlock();
					open();
					makePassable(true);
					System.out.println("You unlock the door and open it...");
					return true;
				} else {
					System.out.println("You do not have the correct key for this door!");
					return false;
				}
			} else { 
				if(!isOpen()) {
					open();
					makePassable(true);
					System.out.println("The door swings open...");
					return true;
				} else {
					close();
					makePassable(false);
					System.out.println("You gently close the door...");
					return true;
				}
			}
		} else {
			System.out.println("You can't interact with that from here!");
			return false;
		}
	}
	
	@Override
	public void render(GraphicsContext gc, Pair<Integer> position) {
		if(!isOpen()) {
			if(getImage() != null) gc.drawImage(getImage(), (position.x * Tile.SIZE), (position.y * Tile.SIZE));
			else {
				gc.setFill(Color.BLUE);
				gc.fillRect((position.x * Tile.SIZE), (position.y * Tile.SIZE), Tile.SIZE, Tile.SIZE);
			}
		}
	}
}
