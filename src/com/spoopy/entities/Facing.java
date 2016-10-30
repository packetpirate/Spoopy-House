package com.spoopy.entities;

import com.spoopy.tile.Tile;
import com.spoopy.tile.TileMap;
import com.spoopy.utils.Pair;

public enum Facing {
	LOLWUT, UP, DOWN, LEFT, RIGHT;
	
	public static Facing getFacing(int xD, int yD) {
		if((xD == 0) && (yD == -1)) return UP;
		else if((xD == 0) && (yD == 1)) return DOWN;
		else if((xD == -1) && (yD == 0)) return LEFT;
		else if((xD == 1) && (yD == 0)) return RIGHT;
		else return LOLWUT;
	}
	
	public static Facing getOpposite(Facing f) {
		switch(f) {
			case UP:    return DOWN;
			case DOWN:  return UP;
			case LEFT:  return RIGHT;
			case RIGHT: return LEFT;
			default:  return LOLWUT;
		}
	}
	
	public static String toString(Facing f) {
		switch(f) {
			case UP:    return "UP";
			case DOWN:  return "DOWN";
			case LEFT:  return "LEFT";
			case RIGHT: return "RIGHT";
			default:	return "LOLWUT";
		}
	}
	
	/**
	 * Returns the next tile on the map in the specified direction. Returns null if the given position p is invalid.
	 * @param f The direction in which to retrieve the next tile.
	 * @param p The starting position.
	 * @param tm The tile map reference.
	 * @return The next tile in the specified direction. If there is no next tile, returns the tile at position p.
	 */
	public static Tile nextTile(Facing f, Pair<Integer> p, TileMap tm) {
		Tile t = tm.getTile(p);
		if(t != null) {
			Tile n = null;
			if(f == UP) n = tm.getTile(new Pair<Integer>(p.x, (p.y - 1)));
			else if(f == DOWN) n = tm.getTile(new Pair<Integer>(p.x, (p.y + 1)));
			else if(f == LEFT) n = tm.getTile(new Pair<Integer>((p.x - 1), p.y));
			else if(f == RIGHT) n = tm.getTile(new Pair<Integer>((p.x + 1), p.y));
			
			if(n != null) return n;
		}
		
		return t;
	}
}
