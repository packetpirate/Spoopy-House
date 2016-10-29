package com.spoopy.tile;

import com.spoopy.utils.Utils;

import javafx.scene.image.Image;

public enum TileType {
	ERROR("", false), 
	VOID("", false),
	START("", true),
	EMPTY("", true),
	WALL("", false),
	DOOR("", false),
	EXIT("", false);
	
	private boolean passable;
	public boolean isPassable() { return passable; }
	public void makePassable(boolean b) { passable = b; }
	
	private Image image;
	public Image getImage() { return image; }
	
	TileType(String filename, boolean b) {
		this.image = Utils.LoadImage(filename);
		this.passable = b;
	}
	
	public static TileType getTileType(char c) {
		switch(c) {
			case '0': return TileType.VOID;
			case 'P': return TileType.START;
			case '1': return TileType.EMPTY;
			case 'X': return TileType.WALL;
			case 'D': return TileType.DOOR;
			case 'E': return TileType.EXIT;
			default:  return TileType.ERROR;
		}
	}
}
