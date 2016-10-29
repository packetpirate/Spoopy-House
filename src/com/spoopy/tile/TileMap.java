package com.spoopy.tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

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
	
	public static TileMap ReadFromFile(String filename) {
		TileMap tm = new TileMap();
		
		BufferedReader br = null;
		try {
			String uriPath = TileMap.class.getResource("/resources/maps/" + filename)
										  .toString().replaceFirst("file:/", "");
			br = new BufferedReader(new FileReader(uriPath));
			
			String line;
			int x = 0, y = 0;
			while((line = br.readLine()) != null) {
				tm.addHeight(1);
				if(line.length() > tm.getWidth()) tm.setWidth(line.length());
				for(int i = 0; i < line.length(); i++, x++) {
					char c = line.charAt(i);
					TileType tt = TileType.getTileType(c);
					boolean b = tt.isPassable();
					Tile t = new Tile(tt, new Pair<Integer>(x, y), b, tt.getImage());
					tm.addTile(t);
				}
				x = 0;
				y++;
			}
		} catch(IOException io) {
			System.err.println("ERROR: Problem reading from map file!");
			io.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				System.err.println("ERROR: Could not close BufferedReader!");
				e.printStackTrace();
			}
		}
		
		return tm;
	}
}
