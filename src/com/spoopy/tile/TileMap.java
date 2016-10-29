package com.spoopy.tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.spoopy.utils.Pair;

public class TileMap {
	private Map<Pair<Integer>, Tile> tiles;
	public Tile getTile(Pair<Integer> p) { return tiles.get(p); }
	public void addTile(Tile t) { tiles.put(t.getPosition(), t); }
	public Tile remTile(Tile t) { return tiles.remove(t.getPosition()); }
	public void resetTileMap()  { tiles.clear(); }
	
	public TileMap() {
		tiles = new HashMap<>();
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
				for(int i = 0; i < line.length(); i++, x++) {
					char c = line.charAt(i);
					TileType tt = TileType.getTileType(c);
					boolean b = tt.isPassable();
					Tile t = new Tile(tt, new Pair<Integer>((x * Tile.SIZE), (y * Tile.SIZE)), b, tt.getImage());
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
