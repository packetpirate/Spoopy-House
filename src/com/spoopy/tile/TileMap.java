package com.spoopy.tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import com.spoopy.entities.Facing;
import com.spoopy.entities.Player;
import com.spoopy.utils.Pair;

public class TileMap {
	private int width, height;
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public void addWidth(int w) { width += w; }
	public void addHeight(int h) { height += h; }
	public void setWidth(int w) { width = w; }
	public void setHeight(int h) { height = h; }
	public boolean inBounds(int xP, int yP) {
		return ((xP >= 0) && (xP < width) && 
				(yP >= 0) && (yP < height));
	}
	
	private Map<Pair<Integer>, Tile> tiles;
	public Tile getTile(Pair<Integer> p) { return tiles.get(p); }
	public void addTile(Tile t) { tiles.put(t.getPosition(), t); }
	public Tile remTile(Tile t) { return tiles.remove(t.getPosition()); }
	public void resetTileMap()  { tiles.clear(); }
	public boolean moveObject(Player player) {
		//System.out.println("INFO: Player facing " + Facing.toString(player.getFacing()));
		Tile t = Facing.nextTile(player.getFacing(), player.getPosition(), this);
		if((t != null) && (!t.getPosition().equals(player.getPosition())) &&
		    (t.getObject() != null) && t.getObject().isPushable(player.getFacing(), t.getPosition(), this)) {
			Tile next = Facing.nextTile(player.getFacing(), t.getPosition(), this);
			next.setObject(t.getObject());
			t.setObject(null);
			//System.out.println("INFO: Player at " + player.getPosition().toString());
			//System.out.println("INFO: Attempting to move object at " + t.getPosition().toString() + " to " + next.getPosition().toString());
			return true;
		}
		
		//System.out.println("INFO: No valid tile movement possible!");
		return false;
	}
	
	public TileMap() {
		width = 0;
		height = 0;
		tiles = new HashMap<>();
	}
	
	public Pair<Integer> findStart() {
		Iterator<Entry<Pair<Integer>, Tile>> it = tiles.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Pair<Integer>, Tile> entry = it.next();
			Tile t = entry.getValue();
			if(t.getTileType() == TileType.START) {
				Pair<Integer> start = entry.getKey();
				return new Pair<Integer>(start.x, start.y);
			}
		}
		return null;
	}
	
	public static void forAllTiles(int xMin, int xMax, int yMin, int yMax, 
								   BiConsumer<Integer, Integer> f) {
		for(int y = yMin; y <= yMax; y++) {
			for(int x = xMin; x <= xMax; x++) {
				f.accept(x, y);
			}
		}
	}
}
