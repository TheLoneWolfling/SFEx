package DatabaseFrontend;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class ItemLocationMapping {

	public static final String ITEM_ID_FIELD_NAME = "ItemId";
	public static final String LOCATION_ID_FIELD_NAME = "LocationId";
	public static final String TABLE_NAME = "LocationItemMapping";
	public static final String ITEM_ID_DOTTED = TABLE_NAME + "."
			+ ITEM_ID_FIELD_NAME;
	public static final String LOCATION_ID_DOTTED = TABLE_NAME + "."
			+ LOCATION_ID_FIELD_NAME;

	public static boolean addMapping(Item item, Location l) throws SQLException {
		final String sql = "insert into " + TABLE_NAME + " ("
				+ ITEM_ID_FIELD_NAME + ", " + LOCATION_ID_FIELD_NAME
				+ ") values (?, ?);";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		st.setQueryTimeout(5);
		final int res;
		try {
			st.setLong(1, item.getID());
			st.setLong(2, l.getID());
			try {
				res = st.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException s) {
				return false;
			}
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
		return res == 1;
	}

}
