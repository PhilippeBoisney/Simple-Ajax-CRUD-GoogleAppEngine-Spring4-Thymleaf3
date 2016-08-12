package com.springleafengine.models;

import com.springleafengine.interfaces.IApiObject;

public class User implements IApiObject{
	
	private String id;
	private String surname;
	private String lastname;
	private String type;
	private boolean admin;
	
	public User() {
		
	}
	
	public User(String id, String surname, String lastname, String type, boolean admin) {
		super();
		this.id = id;
		this.surname = surname;
		this.lastname = lastname;
		this.type = type;
		this.admin = admin;
	}
	
	public User(String surname, String lastname, String type, boolean admin) {
		super();
		this.surname = surname;
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
	
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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