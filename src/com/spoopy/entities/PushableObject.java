package com.spoopy.entities;

import java.util.List;

import com.spoopy.tile.TileMap;

import javafx.scene.image.Image;

public class PushableObject extends GameObject {
	public PushableObject(boolean p, boolean b, List<Facing> d, Image i) {
		super(p, b, d, i);
		makePushable(true);
		makePassable(false);
	}

	@Override
	public boolean interact(Player player, TileMap tm, long current) {
		return false;
	}
}
