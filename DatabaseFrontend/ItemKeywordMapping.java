package DatabaseFrontend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemKeywordMapping {

	public static final String ITEM_ID_FIELD_NAME = "ItemId";
	public static final String KEYWORD_ID_FIELD_NAME = "KeywordId";
	public static final String TABLE_NAME = "KeywordItemMapping";
	public static final String KEYWORD_ID_DOTTED = TABLE_NAME + "." + KEYWORD_ID_FIELD_NAME;
	public static final String ITEM_ID_DOTTED = TABLE_NAME + "." + ITEM_ID_FIELD_NAME;

	public static void addMapping(Item item, Keyword k) throws SQLException {
		final String sql = "insert into " + TABLE_NAME + " (" + ITEM_ID_FIELD_NAME + ", " + KEYWORD_ID_FIELD_NAME + ") values (?, ?);";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		st.setQueryTimeout(5);
		final int res;
		try {
			st.setLong(1, item.getID());
			st.setLong(2, k.getID());
			res = st.executeUpdate();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : " + e.getMessage());
			}
		}
		assert (res == 1);
	}

}
