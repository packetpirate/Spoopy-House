package com.spoopy.tile;

import com.spoopy.utils.Utils;

import javafx.scene.image.Image;

public enum TileType {
	ERROR("", false), 
	VOID("", false),
	START("", true),
	EMPTY("", true),
	WALL("", false);
	
	private boolean passable;
	public boolean isPassable() { return passable; }
	
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
			default:  return TileType.ERROR;
		}
	}
}
