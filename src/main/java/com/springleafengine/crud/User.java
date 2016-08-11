package com.springleafengine.crud;

public class User {
	
	private String name;
	private String type;
	private boolean admin;
	
	public User() {
		
	}
	
	public User(String name, String type, boolean admin) {
		super();
		this.name = name;
		this.type = type;
		this.admin = admin;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	
	
}