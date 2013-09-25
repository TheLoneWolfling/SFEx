package fakeDatabase;

import java.util.ArrayList;
import java.util.List;

import databaseBackendInterface.IItem;
import databaseBackendInterface.IUser;

public class fakeItem implements IItem {
	private String title = "";
	private String description = "";
	private String location = "";
	private List<String> tags = new ArrayList<String>();
	private int price;
	private boolean bought = false;
	private IUser seller;

	
	protected fakeItem(String title, String description, String location,
			List<String> tags, int price, IUser seller) {
		this.title = title;
		this.description = description;
		this.location = location;
		this.tags = tags;
		this.price = price;
		this.seller = seller;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isBought() {
		return bought;
	}
	public void setBought(boolean bought) {
		this.bought = bought;
	}
	public String[] getTags() {
		return (String[]) tags.toArray();
	}
	public IUser getSeller() {
		return seller;
	}
	@Override
	public boolean addTag(String tag) {
		if (tags.contains(tag))
			return true;
		tags.add(tag);
		return false;
	}
	@Override
	public boolean removeTag(String tag) {
		return tags.remove(tag);
	}
	@Override
	public int getPriceInCents() {
		return price;
	}
	@Override
	public boolean beenBought() {
		return bought;
	}
	@Override
	public void setBought() {
		bought = true;
	}
	@Override
	public void setPriceInCents(int price) {
		this.price = price;
	}
	@Override
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

}
