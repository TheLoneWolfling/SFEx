package DatabaseFrontend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class Item {

	private static final String BUY_NOW_PRICE_FIELD_NAME = "BuyNowPriceInCents";
	private static GarbageCollectingConcurrentMap<Long, Item> cache = new GarbageCollectingConcurrentMap<Long, Item>();
	private static final String DESC_FIELD_NAME = "Description";
	private static final String ID_FIELD_NAME = "Id";
	private final static long INVALID_USER = 0;
	private static final String PRICE_FIELD_NAME = "CurrentPriceInCents";
	private static final String SELLER_ID_FIELD_NAME = "SellerId";
	private static final String SOLD_TO_USER_ID_FIELD_NAME = "BuyerId";
	private static final String TABLE_NAME = "Items";
	private static final String DESC_DOTTED = TABLE_NAME + "."
			+ DESC_FIELD_NAME;
	private static final String SELLER_ID_DOTTED = TABLE_NAME + "."
			+ SELLER_ID_FIELD_NAME;
	private static final String PRICE_DOTTED = TABLE_NAME + "."
			+ PRICE_FIELD_NAME;
	private static final String SOLD_TO_USER_ID_DOTTED = TABLE_NAME + "."
			+ SOLD_TO_USER_ID_FIELD_NAME;
	private static final String BUY_NOW_PRICE_DOTTED = TABLE_NAME + "."
			+ BUY_NOW_PRICE_FIELD_NAME;
	private static final String ID_DOTTED = TABLE_NAME + "." + ID_FIELD_NAME;
	public static final String DOTTED_ROW_NAMES = ID_DOTTED + ", "
			+ BUY_NOW_PRICE_DOTTED + ", " + SOLD_TO_USER_ID_DOTTED + ", "
			+ PRICE_DOTTED + ", " + SELLER_ID_DOTTED + ", " + DESC_DOTTED;

	private static Item getItemFromCache(final ResultSet res)
			throws SQLException {
		final long itemID = res.getLong(ID_FIELD_NAME);
		Item i = cache.get(itemID);
		if (i == null)
			i = cache.getOrPut(itemID, new Item(res));
		return i;
	}

	public static Item getItemFromID(final long itemID) throws SQLException {
		final String sql = "select * from " + TABLE_NAME + " where "
				+ ID_FIELD_NAME + " = ?";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Item i;
		try {
			st.setLong(1, itemID);
			final ResultSet res = st.executeQuery();
			if (!res.next())
				return null;
			i = getItemFromCache(res);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return i;
	}

	public static Set<Item> getItemsBy(final Set<Location> locations,
			final Set<Keyword> keywords, final Set<Category> categories,
			final long minPrice, final long maxPrice, final Set<String> text)
			throws SQLException {
		final List<String> searchTerms = new ArrayList<String>();

		if (!locations.isEmpty()) {
			final List<String> locationSearch = new ArrayList<String>();
			for (final Location l : locations)
				locationSearch.add("(" + ItemLocationMapping.LOCATION_ID_DOTTED
						+ " = ?)");
			searchTerms.add("(" + join(locationSearch, "\n or ") + ")");
		}

		if (!keywords.isEmpty()) {
			final List<String> keywordSearch = new ArrayList<String>();
			for (final Keyword k : keywords)
				keywordSearch.add("(" + ItemKeywordMapping.KEYWORD_ID_DOTTED
						+ " = ?)");
			searchTerms.add("(" + join(keywordSearch, "\n or ") + ")");
		}

		if (!categories.isEmpty()) {
			final List<String> categorySearch = new ArrayList<String>();
			for (final Category c : categories)
				categorySearch.add("(" + ItemCategoryMapping.CATEGORY_ID_DOTTED
						+ " = ?)");
			searchTerms.add("(" + join(categorySearch, "\n or ") + ")");
		}

		searchTerms.add("(" + PRICE_DOTTED + " between ? and ?)");

		if (!text.isEmpty()) {
			final List<String> textSearch = new ArrayList<String>();
			for (final String s : text)
				textSearch.add("(" + DESC_DOTTED + " like ?)");
			searchTerms.add("(" + join(textSearch, "\n or ") + ")");
		}

		final String search = join(searchTerms, "\n and ");

		final String joins = TABLE_NAME + " join "
				+ ItemLocationMapping.TABLE_NAME + " on "
				+ ItemLocationMapping.ITEM_ID_DOTTED + "=" + ID_DOTTED
				+ " join " + ItemKeywordMapping.TABLE_NAME + " on "
				+ ItemKeywordMapping.ITEM_ID_DOTTED + "=" + ID_DOTTED
				+ " join " + ItemCategoryMapping.TABLE_NAME + " on "
				+ ItemCategoryMapping.ITEM_ID_DOTTED + "=" + ID_DOTTED;
		final String sql = "select " + DOTTED_ROW_NAMES + "\n from " + joins
				+ "\n where " + search + ";";
		System.out.println(sql);
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		int i = 1;
		final Set<Item> items = new HashSet<Item>();
		try {
			for (final Location l : locations)
				st.setLong(i++, l.getID());
			for (final Keyword k : keywords)
				st.setLong(i++, k.getID());
			for (final Category c : categories)
				st.setLong(i++, c.getID());
			st.setLong(i++, minPrice);
			st.setLong(i++, maxPrice);
			for (final String s : text)
				st.setString(i++, s);
			final ResultSet res = st.executeQuery();
			while (res.next())
				items.add(getItemFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return items;
	}

	protected static Set<Item> getItemsByCategory(final Category category)
			throws SQLException {
		final String sql = "select * from " + ItemCategoryMapping.TABLE_NAME
				+ " where " + ItemCategoryMapping.CATEGORY_ID_FIELD_NAME
				+ " = ?";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Item> items = new HashSet<Item>();
		try {
			st.setLong(1, category.getID());
			final ResultSet res = st.executeQuery();
			while (res.next())
				items.add(getItemFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return items;
	}

	public static Set<Item> getItemsByKeyword(final Keyword keyword)
			throws SQLException {

		final String sql = "select * from " + ItemKeywordMapping.TABLE_NAME
				+ " where " + ItemKeywordMapping.KEYWORD_ID_FIELD_NAME + " = ?";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Item> items = new HashSet<Item>();
		try {
			st.setLong(1, keyword.getID());
			final ResultSet res = st.executeQuery();
			while (res.next())
				items.add(getItemFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return items;
	}

	public static Set<Item> getItemsByLocation(final Location location)
			throws SQLException {
		final String sql = "select * from " + ItemLocationMapping.TABLE_NAME
				+ " where " + ItemLocationMapping.LOCATION_ID_FIELD_NAME
				+ " = ?";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Item> items = new HashSet<Item>();
		try {
			st.setLong(1, location.getID());
			final ResultSet res = st.executeQuery();
			while (res.next())
				items.add(getItemFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return items;
	}

	public static Set<Item> getItemsByPrice(final long minPrice,
			final long maxPrice) throws SQLException {
		final String sql = "select * from " + ItemCategoryMapping.TABLE_NAME
				+ " where " + PRICE_FIELD_NAME + " between ? and ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Item> items = new HashSet<Item>();
		try {
			st.setLong(1, minPrice);
			st.setLong(2, maxPrice);
			final ResultSet res = st.executeQuery();
			while (res.next())
				items.add(getItemFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return items;
	}

	protected static Set<Item> getItemsByUserSelling(final User user)
			throws SQLException {
		final String sql = "select * from " + TABLE_NAME + " where "
				+ SELLER_ID_FIELD_NAME + " = ?";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Item> items = new HashSet<Item>();
		try {
			st.setLong(1, user.getID());
			final ResultSet res = st.executeQuery();
			while (res.next())
				items.add(getItemFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return items;
	}

	public static String join(final Iterable<String> s, final String delimiter) {
		if (s == null)
			return "";
		final Iterator<String> iter = s.iterator();
		final StringBuilder builder = new StringBuilder(iter.next());
		while (iter.hasNext())
			builder.append(delimiter).append(iter.next());
		return builder.toString();
	}

	public static Item makeItem(final long buyNowPriceInCents,
			final long currentPrice, final User seller, final String description)
			throws SQLException {
		final String sql = "insert into " + TABLE_NAME + " ("
				+ BUY_NOW_PRICE_FIELD_NAME + ", " + PRICE_FIELD_NAME + ", "
				+ SELLER_ID_FIELD_NAME + ", " + DESC_FIELD_NAME
				+ ") values (?, ?, ?, ?);";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		final long itemId;
		try {
			st.setLong(1, buyNowPriceInCents);
			st.setLong(2, currentPrice);
			st.setLong(3, seller.getID());
			st.setString(4, description);
			try {
				final int res = st.executeUpdate();
				if (res != 1)
					return null;
			} catch (MySQLIntegrityConstraintViolationException s) {
				return null;
			}
			final ResultSet rs = st.getGeneratedKeys();
			if (!rs.next())
				throw new SQLException(
						"Internal error: No key returned for generated item");
			itemId = rs.getLong(1);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		final Item item = getItemFromID(itemId);
		return item;
	}

	private long buyNowPriceInCents;
	private long currentPrice;
	private String description;
	private final long id;
	private final long sellerID;
	private long soldToUserID;

	private Item(final ResultSet res) throws SQLException {
		id = res.getLong(ID_FIELD_NAME);
		buyNowPriceInCents = res.getLong(BUY_NOW_PRICE_FIELD_NAME);
		soldToUserID = res.getLong(SOLD_TO_USER_ID_FIELD_NAME);
		currentPrice = res.getLong(PRICE_FIELD_NAME);
		sellerID = res.getLong(SELLER_ID_FIELD_NAME);
		description = res.getString(DESC_FIELD_NAME);
	}

	public boolean deleteItem() throws SQLException {
		final String sql = "delete from " + TABLE_NAME + " where "
				+ ID_FIELD_NAME + " = ?";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final int res;
		try {
			st.setLong(1, id);
			res = st.executeUpdate();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return res == 1;
	}

	public Set<Bid> getBids() throws SQLException {
		return Bid.getBidsByItem(this);
	}

	public long getBuyNowPriceInCents() {
		return buyNowPriceInCents;
	}

	public Set<Category> getCategory() throws SQLException {
		return Category.getCategoryForItem(this);
	}

	public String getDescription() {
		return description;
	}

	protected long getID() {
		return id;
	}

	public Set<Keyword> getKeywords() throws SQLException {
		return Keyword.getKeywordsForItem(this);
	}

	public Set<Location> getLocation() throws SQLException {
		return Location.getLocationForItem(this);
	}

	public User getSeller() throws SQLException {
		return User.getUserFromID(sellerID);
	}

	public User getSoldToUser() throws SQLException {
		if (soldToUserID == INVALID_USER)
			return null;
		return User.getUserFromID(soldToUserID);
	}

	public boolean setBuyNowPriceInCents(final long buyNowPriceInCents)
			throws SQLException {
		this.buyNowPriceInCents = buyNowPriceInCents;
		final String sql = "update " + TABLE_NAME + " set "
				+ BUY_NOW_PRICE_FIELD_NAME + " = ? where " + ID_FIELD_NAME
				+ " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final int res;
		try {
			st.setLong(1, buyNowPriceInCents);
			st.setLong(2, id);
			res = st.executeUpdate();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return res == 1;
	}

	public boolean setDescription(final String description) throws SQLException {
		this.description = description;
		final String sql = "update " + TABLE_NAME + " set " + DESC_FIELD_NAME
				+ " = ? where " + ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final int res;
		try {
			st.setString(1, description);
			st.setLong(2, id);
			res = st.executeUpdate();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return res == 1;
	}

	public boolean setSoldToUser(final User user) throws SQLException {
		soldToUserID = user.getID();
		final String sql = "update " + TABLE_NAME + " set "
				+ SOLD_TO_USER_ID_FIELD_NAME + " = ? where " + ID_FIELD_NAME
				+ " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final int res;
		try {
			st.setLong(1, soldToUserID);
			st.setLong(2, id);
			res = st.executeUpdate();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return res == 1;
	}

	public boolean setCurrentPriceInCents(long price) throws SQLException {
		currentPrice = price;
		final String sql = "update " + TABLE_NAME + " set " + PRICE_FIELD_NAME
				+ " = ? where " + ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final int res;
		try {
			st.setLong(1, currentPrice);
			st.setLong(2, id);
			res = st.executeUpdate();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return res == 1;
	}

	@Override
	public String toString() {
		return "Item [buyNowPriceInCents=" + buyNowPriceInCents
				+ ", currentPrice=" + currentPrice + ", description="
				+ description + ", id=" + id + ", sellerID=" + sellerID
				+ ", soldToUserID=" + soldToUserID + " addr="
				+ super.toString().split("@")[1] + "]";
	}

	public boolean addKeyword(Keyword k) throws SQLException {
		return ItemKeywordMapping.addMapping(this, k);
	}

	public void addCategory(Category c) throws SQLException {
		ItemCategoryMapping.addMapping(this, c);
	}

	public void addLocation(Location l) throws SQLException {
		ItemLocationMapping.addMapping(this, l);
	}

	public boolean delKeyword(Keyword k) throws SQLException {
		return ItemKeywordMapping.removeMapping(this, k);
	}
}