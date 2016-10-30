package com.spoopy.tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spoopy.entities.Facing;
import com.spoopy.entities.PushableObject;
import com.spoopy.entities.objects.Door;
import com.spoopy.entities.objects.Key;
import com.spoopy.utils.Pair;
import com.spoopy.utils.Utils;

import javafx.scene.image.Image;

public class MapCreator {
	public static final Pattern DOOR_FILE_FORMAT = Pattern.compile("(\\((\\d+),(\\d+)\\)){1}\\s(\\d+){1}\\s(((true|false)\\s?){3}){1}(\\{((UP|DOWN|LEFT|RIGHT),?){1,4}\\}){1}\\s(\\w+\\.png){1}");
	public static final Pattern KEYS_FILE_FORMAT = Pattern.compile("(\\((\\d+),(\\d+)\\)){1}\\s(\\d+){1}\\s(((true|false)\\s?){2}){1}(\\{((UP|DOWN|LEFT|RIGHT),?){1,4}\\}){1}\\s(\\w+\\.png){1}");
	public static final Pattern PUSH_FILE_FORMAT = Pattern.compile("(\\w+\\.png){1}\\s((true|false)\\s?){2}(\\{((UP|DOWN|LEFT|RIGHT),?){1,4}\\}){1}\\s(\\{(\\((\\d+),(\\d+)\\)\\s*)+\\}){1}");
	public static Map<String, Image> IMAGES = new HashMap<>();
	
	public static TileMap ReadFromFile(String filename) {
		if((filename == null) || filename.isEmpty()) return null;
		
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
			
			if(br != null) br.close();
		} catch(IOException e) {
			System.err.println("ERROR: Problem reading from map file!");
			e.printStackTrace();
		}
		
		return tm;
	}
	
	public static void ReadObjectsFromFile(String filename, TileMap tm) {
		String section = "";
		BufferedReader br = null;
		try {
			String uriPath = TileMap.class.getResource("/resources/maps/" + filename)
	  								.toString().replaceFirst("file:/", "");
			br = new BufferedReader(new FileReader(uriPath));
			
			String line;
			while((line = br.readLine()) != null) {
				if(line.matches("\\[[a-zA-Z]+\\]")) section = line;
				else {
					if(section.equals("[doors]")) {
						Pattern p = MapCreator.DOOR_FILE_FORMAT;
						Matcher m = p.matcher(line);
						if((m != null) && m.matches()) {
							try {
								ParseNewDoor(m, tm);
							} catch(IndexOutOfBoundsException iob) {
								System.err.println("ERROR: (Doors) Invalid capture group!");
								continue;
							}
						} else {
							System.err.println("ERROR: Problem matching door pattern!");
							continue;
						}
					} else if(section.equals("[keys]")) {
						Pattern p = MapCreator.KEYS_FILE_FORMAT;
						Matcher m = p.matcher(line);
						if((m != null) && m.matches()) {
							try {
								ParseNewKey(m, tm);
							} catch(IndexOutOfBoundsException iob) {
								System.err.println("ERROR: (Keys) Invalid capture group!");
								continue;
							}
						} else {
							System.err.println("ERROR: Problem matching keys pattern!");
							continue;
						}
					} else if(section.equals("[pushables]")) {
						Pattern p = MapCreator.PUSH_FILE_FORMAT;
						Matcher m = p.matcher(line);
						if((m != null) && m.matches()) {
							try {
								ParseNewPushables(m, tm);
							} catch(IndexOutOfBoundsException iob) {
								System.err.println("ERROR: (Pushables) Invalid capture group!");
								continue;
							}
						} else {
							System.err.println("ERROR: Problem matching pushables pattern!");
							continue;
						}
					}
				}
			}
		} catch(Exception e) {
			System.err.println("ERROR: Problem reading from objects file!");
			e.printStackTrace();
		}
	}
	
	private static void ParseNewDoor(Matcher m, TileMap tm) throws IndexOutOfBoundsException {
		// Get the position of the door in the tile map.
		Pair<Integer> position = new Pair<>(Integer.parseInt(m.group(2)),
											Integer.parseInt(m.group(3)));
		
		// Get the door's ID.
		int doorID = Integer.parseInt(m.group(4));
		
		// Read in the boolean flags for this door.
		String [] flags = m.group(5).split(" ");
		boolean passable = Boolean.parseBoolean(flags[0]);
		boolean actionable = Boolean.parseBoolean(flags[1]);
		boolean locked = Boolean.parseBoolean(flags[2]);
		
		// Read in the actionable directions of the door.
		List<Facing> actionableDirs = ParseActionableDirs(m, 8);
		
		// Get the image associated with this door.
		Image img = ParseImage(m, 11);
		
		tm.getTile(position).setObject(new Door(passable, actionable, 
												actionableDirs, img, 
												locked, doorID));
	}
	
	private static void ParseNewKey(Matcher m, TileMap tm) throws IndexOutOfBoundsException {
		// Get the position of the door in the tile map.
		Pair<Integer> position = new Pair<>(Integer.parseInt(m.group(2)),
											Integer.parseInt(m.group(3)));
		
		// Get the door's ID.
		int doorID = Integer.parseInt(m.group(4));
		
		// Read in the boolean flags for this key.
		String [] flags = m.group(5).split(" ");
		boolean passable = Boolean.parseBoolean(flags[0]);
		boolean actionable = Boolean.parseBoolean(flags[1]);
		
		// Read in the actionable directions of the key.
		List<Facing> actionableDirs = ParseActionableDirs(m, 8);
		
		// Get the image associate with this key.
		Image img = ParseImage(m, 11);
		
		tm.getTile(position).setObject(new Key(passable, actionable, 
												actionableDirs, img, doorID));
	}
	
	private static void ParseNewPushables(Matcher m, TileMap tm) throws IndexOutOfBoundsException {
		// Get the image associated with the pushable.
		Image img = ParseImage(m, 1);
		
		// Read in the boolean flags for this pushable.
		boolean passable = Boolean.parseBoolean(m.group(2));
		boolean actionable = Boolean.parseBoolean(m.group(3));
		
		// Read in the actionable directions of the pushable.
		List<Facing> actionableDirs = ParseActionableDirs(m, 4);
		
		// Read in the array of positions for the pushables.
		List<Pair<Integer>> positions = ParsePositionArray(m, 7);
		for(Pair<Integer> pair : positions) {
			tm.getTile(pair).setObject(new PushableObject(passable, actionable,
														  actionableDirs, img));
		}
	}
	
	private static List<Facing> ParseActionableDirs(Matcher m, int group) {
		String [] directions = m.group(group).replaceAll("\\{|\\}", "").replace(',', ' ').split(" ");
		List<Facing> actionableDirs = new ArrayList<>();
		for(String s : directions) {
			if(s.equals("UP")) actionableDirs.add(Facing.UP);
			else if(s.equals("DOWN")) actionableDirs.add(Facing.DOWN);
			else if(s.equals("LEFT")) actionableDirs.add(Facing.LEFT);
			else if(s.equals("RIGHT")) actionableDirs.add(Facing.RIGHT);
		}
		
		return actionableDirs;
	}
	
	private static Image ParseImage(Matcher m, int group) {
		String file = m.group(group);
		Image img = null;
		if(MapCreator.IMAGES.containsKey(file)) img = MapCreator.IMAGES.get(file);
		else {
			img = Utils.LoadImage(file);
			if(img != null) MapCreator.IMAGES.put(file, img);
		}
		
		return img;
	}
	
	private static List<Pair<Integer>> ParsePositionArray(Matcher m, int group) {
		String arr = m.group(group).replaceAll("\\{|\\}", "");
		List<Pair<Integer>> positions = new ArrayList<>();
		
		Pattern p = Pattern.compile("\\((\\d+),(\\d+)\\)");
		String [] pairs = arr.split(" ");
		for(String pair : pairs) {
			Matcher i = p.matcher(pair);
			if((i != null) && i.matches()) {
				int x = Integer.parseInt(i.group(1));
				int y = Integer.parseInt(i.group(2));
				positions.add(new Pair<Integer>(x, y));
			}
		}
		
		return positions;
	}
}
