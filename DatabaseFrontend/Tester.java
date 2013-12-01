package DatabaseFrontend;

import java.sql.SQLException;
import java.util.Collections;
import java.util.EnumSet;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import ApplicationLogic.AccountControl;

public class Tester {

	public static void main(String[] args) throws SQLException {
		final String salt = AccountControl.makeSalt();
		final EnumSet<Permission> allpermissions = EnumSet
				.allOf(Permission.class);
		for (int i = 0; i < 10; i++) {
			final int j = i;
			new Thread() {

				public void run() {
					int k = j;
					final String salt = AccountControl.makeSalt();
					final EnumSet<Permission> allpermissions = EnumSet
							.allOf(Permission.class);
					User u = null;
					try {
						u = User.makeUser("e@mail.address" + k, "userName" + k,
								"contact information" + k,
								AccountControl.hash("abcd", salt),
								allpermissions, salt);
						User v = null;
						do {
							try {
								v = User.getUserFromEmail("e@mail.address"
										+ (k + 1) % 10);
							} catch (SQLException s) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException i) {

								}
							}
						} while (v == null);

						System.out.println(k + ": " + u);
						for (int i = 0; i < 1000; i++) {
							u.setEmail(i + ":" + k);
							v.setEmail(i + ":2:" + k);
							u.setContactInfo(i + ":" + k);
							v.setContactInfo(i + ":2:" + k);
							u.setPasswordHash(i + ":" + k);
							v.setPasswordHash(i + ":2:" + k);
							u.setUserName(i + ":" + k);
							v.setUserName(i + ":2:" + k);
						}
						System.out.println(k + ": " + u);
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (u != null)
							try {
								u.deleteUser();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					}
				}
			}.start();
		}
		User u = null;
		try {
			u = User.makeUser("e@mail.address", "userName",
					"contact information", AccountControl.hash("abcd", salt),
					allpermissions, salt);
			System.out.println(u);
			System.out.println(User.getUserFromEmail(u.getEmail()));
			System.out.println(User.getUserFromID(u.getID()));
			System.out.println(User.getUserFromUserName(u.getUserName()));
			u.setContactInfo("New contact info");
			u.setEmail("nE@mail.addr");

			u.setPasswordHash(AccountControl.hash("efgh", salt));

			for (Permission p : allpermissions)
				u.deletePermission(p);
			for (Permission p : allpermissions)
				u.setPermission(p);

			u.setUserName("nUserName");
			System.out.println(u);

			User v = null;
			try {
				v = User.makeUser("e@mail.address2", "userName2",
						"contact information2",
						AccountControl.hash("abcd", salt), allpermissions, salt);
				System.out.println(v);
				Item i = null;

				try {
					i = Item.makeItem(100, 50, u, "ItemDescription");
					System.out.println(i);
					i.setBuyNowPriceInCents(101);
					i.setCurrentPriceInCents(51);
					i.setDescription("ItemDescription2");
					System.out.println(i.getSoldToUser());
					i.setSoldToUser(v);
					System.out.println(i.getSoldToUser());
					System.out.println(i);
					System.out.println(i.getSeller());
					Bid b = null;
					try {
						b = Bid.makeBid(u, i, 55);
						System.out.println(b);
						System.out.println(Bid.getBidFromId(b.getId()));
						System.out.println(i.getBids());
						System.out.println(u.getBidsMade());
					} finally {
						if (b != null)
							b.deleteBid();
					}
					Keyword k = null;
					try {
						k = Keyword.makeKeyword("keyword");
						System.out.println(k);
						System.out.println(Keyword.getKeywordByID(k.getID()));
						System.out.println(Keyword.getKeywordByName("keyword"));
						i.addKeyword(k);
						System.out.println(i.getKeywords());
						System.out.println(Keyword.getKeywordsForItem(i));
						System.out.println(Keyword.getPopularKeywords(10));
					} finally {
						if (k != null)
							k.deleteKeyword();
					}
					Category c = null;
					try {
						c = Category.makeCategory("Category", null, "Desc");
						System.out.println(c);
						System.out.println(Category.getCategoryByID(c.getID()));
						System.out.println(Category
								.getCategoryByName("Category"));
						i.addCategory(c);
						System.out.println(i.getCategory());
						System.out.println(Category.getTopLevelCategories());
						Category d = null;
						try {
							d = Category.makeCategory("Category2", c, "Desc2");
							System.out.println(d);
							System.out.println(d.getParent());
							i.addCategory(d);
							System.out.println(i.getCategory());
							System.out
									.println(Category.getTopLevelCategories());
						} finally {
							if (d != null)
								d.deleteCategory();
						}
					} finally {
						if (c != null)
							c.deleteCategory();
					}
					Location l = null;
					try {
						l = Location.makeLocation("Location", null);
						System.out.println(l);
						System.out.println(Location.getLocationByID(l.getID()));
						System.out.println(Location
								.getLocationByName("Location"));
						i.addLocation(l);
						System.out.println(i.getLocation());
						Location m = null;
						try {
							m = Location.makeLocation("Location2", l);
							System.out.println(m);
							i.addLocation(m);
							System.out.println(i.getLocation());
							System.out.println(l.getChildLocations());
							System.out.println(m.getChildLocations());
							System.out.println(new ResultSet(
									Collections.EMPTY_SET,
									Collections.EMPTY_SET,
									Collections.EMPTY_SET, 0, 10000,
									Collections.EMPTY_SET).getItems());
						} finally {
							if (m != null)
								m.deleteLocation();
						}
					} finally {
						if (l != null)
							l.deleteLocation();
					}

				} finally {
					if (i != null)
						i.deleteItem();
				}
			} finally {
				if (v != null)
					v.deleteUser();
			}
		} finally {
			if (u != null)
				u.deleteUser();
		}

	}

}
