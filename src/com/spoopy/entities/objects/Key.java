package com.spoopy.entities.objects;

import java.util.List;

import com.spoopy.entities.Facing;
import com.spoopy.entities.GameObject;
import com.spoopy.entities.Player;
import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Pair;

import javafx.scene.image.Image;

public class Key extends GameObject {
	private int keyID;
	public int getID() { return keyID; }
	public void setID(int id) { keyID = id; }
	
	public Key(boolean p, boolean b, List<Facing> d, Image i, int id) {
		super(p, b, d, i);
		keyID = id;
	}

	@Override
	public boolean interact(Player player, TileMap tm) {
		TileMap.forAllTiles(0, tm.getWidth(), 0, tm.getHeight(), (x, y) -> {
			Tile t = tm.getTile(new Pair<Integer>(x, y));
			if((t != null) && (t.getObject() != null) && (t.getObject() == this)) {
				t.setObject(null);
				player.addKey(getID());
				System.out.println("You find a key on the ground...");
			}
		});
		return false;
	}
}
