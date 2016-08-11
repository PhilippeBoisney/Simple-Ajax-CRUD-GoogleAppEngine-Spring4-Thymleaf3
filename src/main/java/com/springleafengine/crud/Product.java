package com.springleafengine.crud;

public class Product {
	
	private String name;
	private String description;
	private boolean inStock;
	
	public Product() {
		
	}
	
	public Product(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.inStock = false;
	}

	public Product(String name, String description, boolean inStock) {
		super();
		this.name = name;
		this.description = description;
		this.inStock = inStock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	
}