package com.spoopy.utils;

import java.net.URISyntaxException;

import com.spoopy.tile.TileMap;

import javafx.scene.image.Image;

public class Utils {
	public static Image LoadImage(String filename) {
		if((filename == null) || filename.isEmpty()) return null;
		
		Image image = null;
		try {
			image = new Image(Utils.class.getResource("/resources/images/" + filename).toURI().toString());
		} catch(URISyntaxException urie) {
			System.err.println("ERROR: Invalid URI when loading image!");
			urie.printStackTrace();
		} catch(NullPointerException npe) {
			System.err.println("ERROR: Invalid filename when loading image!");
			npe.printStackTrace();
		}
		return image;
	}
}
