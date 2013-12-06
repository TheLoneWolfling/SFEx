package DatabaseFrontend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class Bid {
	private static GarbageCollectingConcurrentMap<Long, Bid> bidCache = new GarbageCollectingConcurrentMap<Long, Bid>();
	private static final String ID_FIELD_NAME = "Id";
	private static final String ITEM_ID_FIELD_NAME = "ItemId";
	private static final String PRICE_FIELD_NAME = "BidInCents";
	private static final String TABLE_NAME = "Bids";
	private static final String USER_ID_FIELD_NAME = "UserId";

	private static Bid getBidFromCache(final ResultSet res) throws SQLException {
		final long bidID = res.getLong(ID_FIELD_NAME);
		Bid b = bidCache.get(bidID);
		if (b == null)
			b = bidCache.getOrPut(bidID, new Bid(res));
		return b;
	}

	public static Bid getBidFromId(final long id) throws SQLException {
		final String sql = "select * from " + TABLE_NAME + " where "
				+ ID_FIELD_NAME + " = ?";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Bid bid;
		try {
			st.setLong(1, id);
			final ResultSet res = st.executeQuery();
			if (!res.next())
				return null;
			bid = getBidFromCache(res);
			assert(!res.next());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return bid;
	}

	public static Set<Bid> getBidsByItem(final Item item) throws SQLException {
		assert (item != null);
		final String sql = "select * from " + TABLE_NAME + " where "
				+ ITEM_ID_FIELD_NAME + " = ?";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Bid> bids = new HashSet<Bid>();
		try {
			st.setLong(1, item.getID());
			final ResultSet res = st.executeQuery();
			while (res.next())
				bids.add(getBidFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return bids;
	}

	public static Set<Bid> getBidsByUser(final User user) throws SQLException {
		assert (user != null);
		final String sql = "select * from " + TABLE_NAME + " where "
				+ USER_ID_FIELD_NAME + " = ?";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final Set<Bid> bids = new HashSet<Bid>();
		try {
			st.setLong(1, user.getID());
			final ResultSet res = st.executeQuery();
			while (res.next())
				bids.add(getBidFromCache(res));
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		return bids;
	}

	public static Bid makeBid(final User user, final Item item,
			final long priceInCents) throws SQLException {
		assert (user != null);
		assert (item != null);
		assert (priceInCents >= 0); // Not needed, but w/e
		final String sql = "insert into " + TABLE_NAME + " ("
				+ USER_ID_FIELD_NAME + ", " + ITEM_ID_FIELD_NAME + ", "
				+ PRICE_FIELD_NAME + ") values (?, ?, ?);";
		final PreparedStatement st = DataManager.getCon().prepareStatement(sql);
		final long bidId;
		final int res;
		try {
			st.setLong(1, user.getID());
			st.setLong(2, item.getID());
			st.setLong(3, priceInCents);
			try {
				res = st.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException s) {
				assert(false);
				return null;
			}
			final ResultSet rs = st.getGeneratedKeys();
			if (!rs.next())
				throw new SQLException(
						"Internal error: No key returned for generated category");
			bidId = rs.getLong(1);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (final SQLException e) {
				System.out.println("Error closing prepared statement : "
						+ e.getMessage());
			}
		}
		assert(res == 1);
		final Bid bid = getBidFromId(bidId);
		assert(bid != null);
		return bid;
	}

	private final long id;
	private final long itemID;
	private final long priceInCents;
	private final long userID;

	private Bid(final ResultSet res) throws SQLException {
		id = res.getLong(ID_FIELD_NAME);
		userID = res.getLong(USER_ID_FIELD_NAME);
		itemID = res.getLong(ITEM_ID_FIELD_NAME);
		priceInCents = res.getLong(PRICE_FIELD_NAME);
	}

	public boolean deleteBid() throws SQLException {
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
		assert(res <= 1);
		return res == 1;
	}

	public Item getItem() throws SQLException {
		return Item.getItemFromID(itemID);
	}

	public long getPriceInCents() {
		return priceInCents;
	}

	public User getUser() throws SQLException {
		return User.getUserFromID(userID);
	}

	@Override
	public String toString() {
		return "Bid [id=" + id + ", itemID=" + itemID + ", priceInCents="
				+ priceInCents + ", userID=" + userID + " addr="
				+ super.toString().split("@")[1] + "]";
	}

	public long getID() {
		return id;
	}
}