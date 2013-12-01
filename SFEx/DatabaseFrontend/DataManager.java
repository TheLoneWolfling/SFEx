package DatabaseFrontend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataManager {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (final Exception e) {
			System.err.println(e.toString());
		}
	}
	private static final ConnectionCache cache = new ConnectionCache();
	public static DataManager INSTANCE;
	public static DataManager getINSTANCE() {
		return INSTANCE;
	}

	public Connection con;

	public static Connection getCon() throws SQLException {
		final Thread thisThread = Thread.currentThread();
		Connection i = cache.get(thisThread);
		if (i == null)
			i = cache.getOrPut(thisThread, makeCon());
		return i;
	}
	
	private static Connection makeCon() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://isel.cs.unb.ca:3306/cs2043team16DB?jdbcCompliantTruncation=false", "cs2043team16", "cs2043team16");
	}
}