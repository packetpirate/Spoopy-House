package com.spoopy.entities;

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
}
