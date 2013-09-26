package fakeDatabase;

import java.util.ArrayList;
import java.util.List;

import databaseBackendInterface.IItem;
import databaseBackendInterface.IUser;

public class fakeUser implements IUser {

	private String username;
	private String passwordHash;
	private String email;
	private List<IItem> items = new ArrayList<IItem>();
	
	public fakeUser(String username, String passwordHash, String email) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.email = email;
	}


	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPasswordHash() {
		return passwordHash;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public Iterable<IItem> getItems() {
		return items;
	}

	@Override
	public void refresh() {
	}

	@Override
	public void populate() {
	}

	public void register(IItem item) {
		items.add(item);
	}

}
