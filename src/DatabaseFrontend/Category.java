/**
 * 
 */
package DatabaseFrontend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class Category {

	private static GarbageCollectingConcurrentMap<Long, Category> categoryCache = new GarbageCollectingConcurrentMap<Long, Category>();
	private static final String DESCRIPTION_FIELD_NAME = "Description";
	private static final String ID_FIELD_NAME = "Id";
	private static final long INVALID_PARENT_ID = 0;
	private static final String LEVEL_FIELD_NAME = "LevelNo";
	private static final String NAME_FIELD_NAME = "Name";
	private static final String PARENT_ID_FIELD_NAME = "ParentId";
	private static final String TABLE_NAME = "Categories";
	private static final int TOP_LEVEL = 0;
	private static final String ID_DOTTED = TABLE_NAME + "." + ID_FIELD_NAME;
	private static final String DESCRIPTION_DOTTED = TABLE_NAME + "."
			+ DESCRIPTION_FIELD_NAME;
	private static final String LEVEL_DOTTED = TABLE_NAME + "."
			+ LEVEL_FIELD_NAME;
	private static final String NAME_DOTTED = TABLE_NAME + "."
			+ NAME_FIELD_NAME;
	private static final String PARENT_ID_DOTTED = TABLE_NAME + "."
			+ PARENT_ID_FIELD_NAME;
	private static final String DOTTED_ROW_NAMES = DESCRIPTION_DOTTED + ", "
			+ ID_DOTTED + ", " + LEVEL_DOTTED + ", " + NAME_DOTTED + ", "
			+ PARENT_ID_DOTTED;

	protected static Category getCategoryByID(final long id)
			throws SQLException {
		final String sql = "select * from " + TABLE_NAME + " where "
				+ ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		Category c;
		try {
			st.setLong(1, id);
			final ResultSet res = st.executeQuery();
			if (!res.next())
				return null;
			c = getCategoryFromCache(res);
			assert (!res.next());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return c;
	}

	public static Category getCategoryByName(final String name)
			throws SQLException {
		assert (name != null);
		final String sql = "select * from " + TABLE_NAME + " where "
				+ NAME_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		Category c;
		try {
			st.setString(1, name);
			final ResultSet res = st.executeQuery();
			if (!res.next())
				return null;
			c = getCategoryFromCache(res);
			assert (!res.next());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return c;
	}

	public static Set<Category> getCategoryForItem(final Item item)
			throws SQLException {
		assert (item != null);
		final String sql = "select " + DOTTED_ROW_NAMES + " from "
				+ ItemCategoryMapping.TABLE_NAME + " join " + TABLE_NAME
				+ " on " + ItemCategoryMapping.CATEGORY_ID_DOTTED + "="
				+ ID_DOTTED + " where "
				+ ItemCategoryMapping.ITEM_ID_DOTTED + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Category> categories = new HashSet<Category>();
		try {
			st.setLong(1, item.getID());
			final ResultSet res = st.executeQuery();
			while (res.next())
				categories.add(getCategoryFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return categories;
	}

	private static Category getCategoryFromCache(final ResultSet res)
			throws SQLException {
		final long keywordID = res.getLong(ID_FIELD_NAME);
		Category c = categoryCache.get(keywordID);
		if (c == null)
			c = categoryCache.getOrPut(keywordID, new Category(res));
		return c;
	}

	public static Set<Category> getTopLevelCategories() throws SQLException {
		final String sql = "select * from " + TABLE_NAME + " where "
				+ LEVEL_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Category> categories = new HashSet<Category>();
		try {
			st.setLong(1, TOP_LEVEL);
			final ResultSet res = st.executeQuery();
			while (res.next())
				categories.add(getCategoryFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return categories;
	}

	public static Category makeCategory(final String name,
			final Category parent, final String description)
			throws SQLException {
		assert (name != null);
		assert (description != null);
		final String sql = "insert into " + TABLE_NAME + " ("
				+ PARENT_ID_FIELD_NAME + ", " + LEVEL_FIELD_NAME + ", "
				+ NAME_FIELD_NAME + ", " + DESCRIPTION_FIELD_NAME
				+ ") values (?, ?, ?, ?);";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		final Category c;
		final int res;
		try {
			if (parent != null) {
				st.setLong(1, parent.id);
				st.setLong(2, parent.level + 1);
			} else {
				st.setLong(1, INVALID_PARENT_ID);
				st.setLong(2, TOP_LEVEL);
			}
			st.setString(3, name);
			st.setString(4, description);
			try {
				res = st.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException s) {
				return null;
			}
			final ResultSet rs = st.getGeneratedKeys();
			if (!rs.next())
				throw new SQLException(
						"Internal error: No key returned for generated category");
			final long categoryId = rs.getLong(1);
			c = getCategoryByID(categoryId);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		assert (res == 1);
		return c;
	}

	private String description;
	private final long id;
	private long level;
	private String name;
	private long parentID;

	private Category(final ResultSet res) throws SQLException {
		id = res.getLong(ID_FIELD_NAME);
		parentID = res.getLong(PARENT_ID_FIELD_NAME);
		level = res.getLong(LEVEL_FIELD_NAME);
		name = res.getString(NAME_FIELD_NAME);
		description = res.getString(DESCRIPTION_FIELD_NAME);
	}

	public boolean deleteCategory() throws SQLException {
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
		assert (res <= 1);
		return res == 1;
	}

	public String getDescription() {
		return description;
	}

	public long getID() {
		return id;
	}

	public Set<Item> getItems() throws SQLException {
		return Item.getItemsByCategory(this);
	}

	public String getName() {
		return name;
	}

	public Category getParent() throws SQLException {
		return getCategoryByID(parentID);
	}

	public synchronized boolean setDescription(final String description)
			throws SQLException {
		this.description = description;
		final String sql = "update " + TABLE_NAME + " set "
				+ DESCRIPTION_FIELD_NAME + " = ? where " + ID_FIELD_NAME
				+ " = ?;";
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

	public synchronized boolean setName(final String name) throws SQLException {
		this.name = name;
		final String sql = "update " + TABLE_NAME + " set " + NAME_FIELD_NAME
				+ " = ? where " + ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final int res;
		try {
			st.setString(1, name);
			st.setLong(2, id);
			try {
				res = st.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException s) {
				return false;
			}
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

	public synchronized boolean setParent(final Category parent)
			throws SQLException {
		Category p = parent;
		while (p != null) {
			if (p == this)
				return false;
			p = p.getParent();
		}
		
		if (parent == null) {
			level = TOP_LEVEL;
			parentID = INVALID_PARENT_ID;
		} else {
			level = parent.level + 1;
			parentID = parent.getID();
		}
		final String sql = "update " + TABLE_NAME + " set "
				+ PARENT_ID_FIELD_NAME + " = ?, " + LEVEL_FIELD_NAME
				+ " = ? where " + ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final int res;
		try {
			st.setLong(1, parentID);
			st.setLong(2, level);
			st.setLong(3, id);
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
		assert (res <= 1);
		return res == 1;
	}

	@Override
	public String toString() {
		return "Category [description=" + description + ", id=" + id
				+ ", level=" + level + ", name=" + name + ", parentID="
				+ parentID + " addr=" + super.toString().split("@")[1] + "]";
	}

	public Set<Category> getChildCategories() throws SQLException {
		final String sql = "select * from " + TABLE_NAME + " where "
				+ PARENT_ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Category> categories = new HashSet<Category>();
		try {
			st.setLong(1, id);
			final ResultSet res = st.executeQuery();
			while (res.next())
				categories.add(getCategoryFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return categories;
	}
}