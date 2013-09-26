package database.fake;

import java.util.ArrayList;
import java.util.Collection;

import shared.API.IItem;

import database.API.IUser;
import database.API.NoSuchUserException;
import database.API.NullTagsException;

public class fakeItem implements IItem {
	private String title = "";
	private String description = "";
	private String location = "";
	private Collection<String> tags = new ArrayList<String>();
	private int price;
	private boolean bought = false;
	private IUser seller;

	public fakeItem(String title, String description, String location,
			Collection<String> tags, int price, IUser seller)
			throws NullTagsException, NoSuchUserException {
		if (tags == null)
			throw new NullTagsException();
		else if (seller == null)
			throw new NoSuchUserException(null);
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

	public String[] getTags() {
		return tags.toArray(new String[] {});
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
