package com.springleafengine.models;

import com.springleafengine.interfaces.IApiObject;

public class User implements IApiObject{
	
	private String id;
	private String name;
	private String lastname;
	private String type;
	private boolean admin;
	
	public User() {
		
	}
	
	public User(String id, String name, String lastname, String type, boolean admin) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.type = type;
		this.admin = admin;
	}
	
	public User(String name, String lastname, String type, boolean admin) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.type = type;
		this.admin = admin;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
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