/**
 * 
 */
package DatabaseFrontend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class Keyword {

	private static final String ID_FIELD_NAME = "Id";
	private static final String TABLE_NAME = "Keywords";
	private static GarbageCollectingConcurrentMap<Long, Keyword> keywordCache = new GarbageCollectingConcurrentMap<Long, Keyword>();
	private static final String NAME_FIELD_NAME = "Name";
	private static final String POPULARITY_FIELD_NAME = "ItemsWith";
	private static final String ID_DOTTED = TABLE_NAME + "." + ID_FIELD_NAME;
	private static final String NAME_DOTTED = TABLE_NAME + "."
			+ NAME_FIELD_NAME;
	private static final String POPULARITY_DOTTED = TABLE_NAME + "."
			+ POPULARITY_FIELD_NAME;
	private static final String DOTTED_ROW_NAMES = ID_DOTTED + ", "
			+ NAME_DOTTED + ", " + POPULARITY_DOTTED;

	protected static Keyword getKeywordByID(final long id) throws SQLException {
		final String sql = "select * from " + TABLE_NAME + " where "
				+ ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Keyword k;
		try {
			st.setLong(1, id);
			final ResultSet res = st.executeQuery();
			if (!res.next())
				return null;
			k = getKeywordFromCache(res);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return k;
	}

	public static Keyword getKeywordByName(final String name)
			throws SQLException {
		final String sql = "select * from " + TABLE_NAME + " where "
				+ NAME_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Keyword k;
		try {
			st.setString(1, name);
			final ResultSet res = st.executeQuery();
			if (!res.next())
				return null;
			k = getKeywordFromCache(res);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return k;
	}

	private static Keyword getKeywordFromCache(final ResultSet res)
			throws SQLException {
		final long keywordID = res.getLong(ID_FIELD_NAME);
		Keyword k = keywordCache.get(keywordID);
		if (k == null)
			k = keywordCache.getOrPut(keywordID, new Keyword(res));
		return k;
	}

	public static Set<Keyword> getKeywordsForItem(final Item item)
			throws SQLException {
		final String sql = "select " + DOTTED_ROW_NAMES + " from "
				+ ItemKeywordMapping.TABLE_NAME + " join " + TABLE_NAME
				+ " on " + ItemKeywordMapping.KEYWORD_ID_DOTTED + "="
				+ ID_DOTTED + " where " + ItemKeywordMapping.ITEM_ID_FIELD_NAME
				+ " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Keyword> keywords = new HashSet<Keyword>();
		try {
			st.setLong(1, item.getID());
			final ResultSet res = st.executeQuery();
			while (res.next())
				keywords.add(getKeywordFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return keywords;
	}

	public static Set<Keyword> getPopularKeywords(final long limit)
			throws SQLException {
		final String sql = "select * from " + TABLE_NAME + " order by "
				+ POPULARITY_FIELD_NAME + " DESC;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Keyword> keywords = new HashSet<Keyword>();
		try {
			final ResultSet res = st.executeQuery();
			while (res.next())
				keywords.add(getKeywordFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return keywords;
	}

	public static Keyword makeKeyword(final String name) throws SQLException {
		final String sql = "insert into " + TABLE_NAME + " (" + NAME_FIELD_NAME
				+ ", " + POPULARITY_FIELD_NAME + ") values (?, ?);";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		final Keyword k;
		try {
			st.setString(1, name);
			st.setLong(2, 0);
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
						"Internal error: No key returned for generated keyword");
			final long keywordId = rs.getLong(1);
			k = getKeywordByID(keywordId);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return k;
	}

	private final long id;
	private String name;
	private long popularity;

	private Keyword(final ResultSet res) throws SQLException {
		id = res.getLong(ID_FIELD_NAME);
		name = res.getString(NAME_FIELD_NAME);
	}

	public boolean deleteKeyword() throws SQLException {
		final String sql = "delete from " + TABLE_NAME + " where id = ?";
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

	public long getID() {
		return id;
	}

	public Set<Item> getItems() throws SQLException {
		return Item.getItemsByKeyword(this);
	}

	public String getName() {
		return name;
	}

	public long getPopularity() {
		return popularity;
	}

	public boolean setName(final String name) throws SQLException {
		this.name = name;
		final String sql = "update " + TABLE_NAME + " set " + NAME_FIELD_NAME
				+ " = ? where " + ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final int res;
		try {
			st.setString(1, name);
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

	public boolean setPopularity(final long popularity) throws SQLException {
		this.popularity = popularity;
		final String sql = "update " + TABLE_NAME + " set "
				+ POPULARITY_FIELD_NAME + " = ? where " + ID_FIELD_NAME
				+ " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final int res;
		try {
			st.setLong(1, popularity);
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
		return "Keyword [id=" + id + ", name=" + name + ", popularity="
				+ popularity + " addr=" + super.toString().split("@")[1] + "]";
	}
}