package DatabaseFrontend;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BidTest {
	User u;
	Item i;
	Bid b;

	@Before
	public void setUp() throws Exception {
		u = User.getUserFromEmail("emailTESTING1234");
		if (u == null)
			u = User.makeUser("emailTESTING1234", "userNameTESTING1234",
					"contactInfoTESTING1234", "passwordHashTESTING1234",
					EnumSet.allOf(Permission.class), "saltTESTING1234");
		try {
			i = Item.getItemsByUserSelling(u).iterator().next();
		} catch (NoSuchElementException e) {
		}
		if (i == null)
			i = Item.makeItem(1, "NameTESTING1234", 0, u,
					"descriptionTESTING1234");
		if (b == null)
			b = Bid.makeBid(u, i, 20);
		try {
			b = Bid.getBidsByUser(u).iterator().next();
		} catch (NoSuchElementException e) {
		}
	}

	@After
	public void tearDown() throws Exception {
		if (i != null)
			i.deleteItem();
		if (u != null)
			u.deleteUser();
	}

	@Test
	public void testGetBidFromId() throws SQLException {
		assertEquals(b, Bid.getBidFromId(b.getID()));
	}

	@Test
	public void testGetBidsByItem() throws SQLException {
		Set<Bid> bids = i.getBids();
		assertNotNull(bids);
		assertTrue(bids.contains(b));
		assertTrue(bids.size() == 1);
	}

	@Test
	public void testGetBidsByUser() throws SQLException {
		Set<Bid> bids = u.getBidsMade();
		assertNotNull(bids);
		assertTrue(bids.contains(b));
		assertTrue(bids.size() == 1);
	}

	@Test
	public void testDeleteBid() throws SQLException {
		b.deleteBid();
		assertNull(Bid.getBidFromId(b.getID()));
	}

	@Test
	public void testGetItem() throws SQLException {
		assertEquals(i, b.getItem());
	}

	@Test
	public void testGetPriceInCents() {
		assertEquals(20, b.getPriceInCents());
	}

	@Test
	public void testGetUser() throws SQLException {
		assertEquals(u, b.getUser());
	}

}
