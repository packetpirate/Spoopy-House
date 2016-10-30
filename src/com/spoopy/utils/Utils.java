package com.spoopy.utils;

import java.net.URISyntaxException;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

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
	
	public static Font LoadFont(String filename, double size) {
		if((filename == null) || filename.isEmpty()) return null;
		
		Font font = null;
		try {
			font = Font.loadFont(Utils.class.getResource("/resources/fonts/" + filename).toURI().toString(), size);
		} catch (URISyntaxException urie) {
			System.err.println("ERROR: Invalid URI when loading font!");
			urie.printStackTrace();
		} catch(NullPointerException npe) {
			System.err.println("ERROR: Invalid filename when loading font!");
			npe.printStackTrace();
		}
		return font;
	}
}
