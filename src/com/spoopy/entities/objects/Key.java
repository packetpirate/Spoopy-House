package com.spoopy.entities.objects;

import java.util.List;

import com.spoopy.entities.Facing;
import com.spoopy.entities.GameObject;
import com.spoopy.entities.Player;
import com.spoopy.gfx.MessageHandler;
import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Message;
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
	public boolean interact(Player player, TileMap tm, long current) {
		TileMap.forAllTiles(0, tm.getWidth(), 0, tm.getHeight(), (x, y) -> {
			Tile t = tm.getTile(new Pair<Integer>(x, y));
			if((t != null) && (t.getObject() != null) && 
			   (t.getObject() == this) && 
			   (player.getPosition().equals(t.getPosition()) || 
				Facing.nextTile(player.getFacing(), player.getPosition(), tm).equals(t))) {
				t.setObject(null);
				player.addKey(getID());
				MessageHandler.addMessage("You find a key on the ground...", current, Message.MEDIUM);
			}
		});
		return false;
	}
}
