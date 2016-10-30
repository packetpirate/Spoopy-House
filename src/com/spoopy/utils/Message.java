package com.spoopy.utils;

public class Message {
	public static long SHORT = 2000;
	public static long MEDIUM = 4000;
	public static long LONG = 6000;
	
	private String message;
	public String getMessage() { return message; }
	public void changeMessage(String m) { message = m; }
	
	private long duration;
	private long created;
	public boolean isActive(long current) { return ((current - created) < duration); }
	public void refresh(long current) { created = current; }
	
	public Message(String m, long t, long d) {
		message = m;
		duration = d;
		created = t;
	}
}
