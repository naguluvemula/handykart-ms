package org.tcs.hackathon.prd.model;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;

@MongoEntity(collection = "HKProduct")
public class Product extends PanacheMongoEntityBase  {
	
	
	@BsonId
	private int itemId;
	
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	public boolean isFreeShipping() {
		return isFreeShipping;
	}
	public void setFreeShipping(boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}
	private String sku;
	private String title;
	private String description;
	
	private double price;
	
	public boolean isFreeShipping;
	
	private String category;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	private int inventory;
	
	
	public static List<Product> findByTitle(String title) {
        //return find("title", title).firstResult();
        //return find(" {'title':{$in: [?1]}}",title).list();
		return find("title like ?1",title).list();
    }
	
	public static List<Product> findByCategory(String category) {
		//System.out.println("Cateogry is " + category);
        return find("category", category).list();
        
    }
	
	public static Product findByItemId(int itemId) {
		//System.out.println("Cateogry is " + category);
        return find("_id", itemId).firstResult();
    }

	/*public static Product findById(String id) {
        return find("id", id).firstResult();
    }*/
	
	/* Following variables are not needed will be used in future*/
	//public int installments;
		//public String currencyId;
		//public String currencyFormat;
		//private List<String> availableSize;
	    //public String style;
	
	
}
