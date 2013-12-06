package DatabaseFrontend;

import java.sql.SQLException;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class CommonTesting {
	public User u;
	public Item i;
	public Bid b;
	public Category c;
	public Keyword k;
	public Location l;

	public CommonTesting() throws SQLException {
		User v = User.getUserFromEmail("emailTESTING12345");
		if (v != null)
			v.deleteUser();
		u = User.getUserFromEmail("emailTESTING1234");
		if (u != null)
			u.deleteUser();
		u = User.makeUser("emailTESTING1234", "userNameTESTING1234",
				"contactInfoTESTING1234", "passwordHashTESTING1234",
				EnumSet.allOf(Permission.class), "saltTESTING1234");
		try {
			i = Item.getItemsByUserSelling(u).iterator().next();
		} catch (NoSuchElementException e) {
		}
		if (i != null)
			i.deleteItem();
		i = Item.makeItem(1, "NameTESTING1234", 0, u, "descriptionTESTING1234");
		try {
			b = Bid.getBidsByUser(u).iterator().next();
		} catch (NoSuchElementException e) {
		}
		if (b != null)
			b.deleteBid();
		b = Bid.makeBid(u, i, 20);
		c = Category.getCategoryByName("CategoryTESTING1234");
		if (c != null)
			c.deleteCategory();
		c = Category.makeCategory("CategoryTESTING1234", null,
				"descriptionTESTING1234");
		i.addCategory(c);
		k = Keyword.getKeywordByName("KeywordTESTING1234");
		if (k != null)
			k.deleteKeyword();
		k = Keyword.makeKeyword("KeywordTESTING1234");
		i.addKeyword(k);
		l = Location.getLocationByName("LocationTESTING1234");
		if (l != null)
			l.deleteLocation();
		l = Location.makeLocation("LocationTESTING1234", null);
		i.addLocation(l);
	}
	
	public void del() throws SQLException {
		if (i != null)
			i.deleteItem();
		Item j = null;
		try {
		j = Item.getItemsByName("NameTESTING123456").iterator().next();
		} catch (NoSuchElementException e) {
		}
		if (j != null)
			j.deleteItem();
		User v = User.getUserFromEmail("emailTESTING12345");
		if (v != null)
			v.deleteUser();
		if (c != null)
			c.deleteCategory();
		if (k != null)
			k.deleteKeyword();
		if (l != null)
			l.deleteLocation();
	}

}
