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

public class Location {

	private static final int INVALID_PARENT = 0;
	private static final String ID_FIELD_NAME = "Id";
	private static final String LEVEL_FIELD_NAME = "LevelNo";
	private static final String LOCATION_TABLE_NAME = "Locations";
	private static GarbageCollectingConcurrentMap<Long, Location> locationCache = new GarbageCollectingConcurrentMap<Long, Location>();
	private static final String NAME_FIELD_NAME = "Name";
	private static final String PARENT_ID_FIELD_NAME = "ParentId";
	private static final int ROOT_LEVEL = 0;

	protected static Location getLocationByID(final long id)
			throws SQLException {
		final String sql = "select * from " + LOCATION_TABLE_NAME + " where "
				+ ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Location l;
		try {
			st.setLong(1, id);
			final ResultSet res = st.executeQuery();
			if (!res.next())
				return null;
			l = getLocationFromCache(res);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return l;
	}

	public static Location getLocationByName(final String name)
			throws SQLException {
		final String sql = "select * from " + LOCATION_TABLE_NAME + " where "
				+ NAME_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Location l;
		try {
			st.setString(1, name);
			final ResultSet res = st.executeQuery();
			if (!res.next())
				return null;
			l = getLocationFromCache(res);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return l;
	}

	public static Set<Location> getLocationForItem(final Item item)
			throws SQLException {
		final String sql = "select * from " + ItemLocationMapping.TABLE_NAME
				+ " where " + ItemLocationMapping.ITEM_ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Location> toRet = new HashSet<Location>();
		try {
			st.setLong(1, item.getID());
			final ResultSet res = st.executeQuery();
			while (res.next())
				toRet.add(getLocationByID(res
						.getLong(ItemLocationMapping.LOCATION_ID_FIELD_NAME)));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return toRet;
	}

	private static Location getLocationFromCache(final ResultSet res)
			throws SQLException {
		final long LocationID = res.getLong(ID_FIELD_NAME);
		Location u = locationCache.get(LocationID);
		if (u == null)
			u = locationCache.getOrPut(LocationID, new Location(res));
		return u;
	}

	public static Location makeLocation(final String name, final Location parent)
			throws SQLException {
		final String sql = "insert into " + LOCATION_TABLE_NAME + " ("
				+ NAME_FIELD_NAME + ", " + PARENT_ID_FIELD_NAME + ", "
				+ LEVEL_FIELD_NAME + ") values (?, ?, ?);";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		final long locationId;
		try {
			st.setString(1, name);
			if (parent == null) {
				st.setLong(2, INVALID_PARENT);
				st.setInt(3, ROOT_LEVEL);
			} else {
				st.setLong(2, parent.id);
				st.setLong(3, parent.level + 1);
			}
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
						"Internal error: No key returned for generated location");
			locationId = rs.getLong(1);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		final Location toRet = getLocationByID(locationId);
		return toRet;
	}

	private final long id;
	private final int level;
	private String name;
	private final long parentID;

	private Location(final ResultSet res) throws SQLException {
		id = res.getLong(ID_FIELD_NAME);
		name = res.getString(NAME_FIELD_NAME);
		parentID = res.getLong(PARENT_ID_FIELD_NAME);
		level = res.getInt(LEVEL_FIELD_NAME);
	}

	public boolean deleteLocation() throws SQLException {
		assert (getChildLocations().isEmpty());
		final String sql = "delete from " + LOCATION_TABLE_NAME + " where "
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

	public Set<Location> getChildLocations() throws SQLException {
		final String sql = "select * from " + LOCATION_TABLE_NAME + " where "
				+ PARENT_ID_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Location> locations = new HashSet<Location>();
		try {
			st.setLong(1, id);
			final ResultSet res = st.executeQuery();
			while (res.next())
				locations.add(getLocationFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return locations;
	}

	public long getID() {
		return id;
	}

	public Set<Item> getItems() throws SQLException {
		return Item.getItemsByLocation(this);
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public Location getParent() throws SQLException {
		return getLocationByID(parentID);
	}

	public Set<Location> getTopLevelLocations() throws SQLException {
		final String sql = "select * from " + LOCATION_TABLE_NAME + " where "
				+ LEVEL_FIELD_NAME + " = ?;";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Location> locations = new HashSet<Location>();
		try {
			st.setInt(1, INVALID_PARENT);
			final ResultSet res = st.executeQuery();
			while (res.next())
				locations.add(getLocationFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return locations;
	}

	public boolean setName(final String name) throws SQLException {
		this.name = name;
		final String sql = "update " + LOCATION_TABLE_NAME + " set "
				+ NAME_FIELD_NAME + " = ? where " + ID_FIELD_NAME + " = ?;";
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

	@Override
	public String toString() {
		return "Location [id=" + id + ", level=" + level + ", name=" + name
				+ ", parentID=" + parentID + " inst="
				+ super.toString().split("@")[1] + "]";
	}
}