package com.spoopy.gfx;

import com.spoopy.utils.Pair;

public class Viewport {
	private Pair<Integer> position;
	public Pair<Integer> getPosition() { return position; }
	public Integer getX() { return position.x; }
	public Integer getY() { return position.y; }
	
	private Pair<Integer> viewMax;
	public Pair<Integer> getViewMax() { return viewMax; }
	public Integer getViewMaxX() { return viewMax.x; }
	public Integer getViewMaxY() { return viewMax.y; }
	
	private Pair<Integer> dimensions;
	public Pair<Integer> getDimensions() { return dimensions; }
	public Integer getWidth() { return dimensions.x; }
	public Integer getHeight() { return dimensions.y; }
	
	public Viewport(Pair<Integer> startPos, Pair<Integer> viewportSize, Pair<Integer> worldSize) {
		position = startPos;
		dimensions = viewportSize;
		viewMax = new Pair<Integer>((worldSize.x - dimensions.x), 
									(worldSize.y - dimensions.y));
	}
	
	public void move(int x, int y, Pair<Integer> player) {
		//if(!((position.x + x) > viewMax.x)) position.x += x;
		//if(!((position.y + y) > viewMax.y)) position.y += y;
		position.x += x;
		position.y += y;
		//if(position.x > viewMax.x) position.x = viewMax.x;
		//if(position.y > viewMax.y) position.y = viewMax.y;
	}
	
	public Pair<Integer> getAdjustedPosition(Pair<Integer> p) {
		Pair<Integer> a = new Pair<Integer>(p.x, p.y);
		
		a.x -= position.x;
		a.y -= position.y;
		
		return a;
	}
}
