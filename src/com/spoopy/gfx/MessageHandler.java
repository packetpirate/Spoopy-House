package com.spoopy.gfx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.spoopy.utils.Message;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MessageHandler {
	private static List<Message> messages = new ArrayList<Message>();
	
	public static void addMessage(String message, long current, long duration) {
		MessageHandler.messages.add(new Message(message, current, duration));
	}
	
	public static void updateMessages(long current) {
		Iterator<Message> it = MessageHandler.messages.iterator();
		while(it.hasNext()) {
			Message m = it.next();
			if(!m.isActive(current)) it.remove();
		}
	}
	
	public static void renderMessages(GraphicsContext gc, long current) {
		gc.setStroke(Color.WHITE);
		gc.setFill(Color.DARKRED);
		
		for(int i = 0; i < MessageHandler.messages.size(); i++) {
			if(MessageHandler.messages.get(i).isActive(current)) {
				int y = (int)((i * gc.getFont().getSize()) + 50);
				gc.fillText(MessageHandler.messages.get(i).getMessage(), 30, y);
				gc.strokeText(MessageHandler.messages.get(i).getMessage(), 30, y);
			}
		}
	}
}
