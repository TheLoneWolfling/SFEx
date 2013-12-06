package DatabaseFrontend;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocationTest {
	User u;
	Item i;
	Location l;
	Location m;

	@Before
	public void setUp() throws Exception {
		u = User.getUserFromEmail("emailTESTING1234");
		if (u != null)
			u.deleteUser();
		u = User.makeUser("emailTESTING1234", "userNameTESTING1234",
					"contactInfoTESTING1234", "passwordHashTESTING1234",
					EnumSet.allOf(Permission.class), "saltTESTING1234");
		try {
			i = Item.getItemsByUserSelling(u).iterator().next();
		} catch (NoSuchElementException e) {
		}
		if (i != null)
			i.deleteItem();
		i = Item.makeItem(1, "NameTESTING1234", 0, u,
					"descriptionTESTING1234");
		l = Location.getLocationByName("LocationTESTING1234");
		if (l != null)
			l.deleteLocation();
		l = Location.makeLocation("LocationTESTING1234", null);
		i.addLocation(l);
		m = Location.getLocationByName("LocationTESTING12345");
		if (m != null)
			m.deleteLocation();
		m = Location.makeLocation("LocationTESTING12345", l);
		m.setParent(l);
		Location e = Location.getLocationByName("LocationTESTING123456");
		if (e != null)
			e.deleteLocation();
		
	}

	@After
	public void tearDown() throws Exception {
		if (i != null)
			i.deleteItem();
		if (u != null)
			u.deleteUser();
	}

	@Test
	public void testGetLocationByID() throws SQLException {
		assertEquals(l, Location.getLocationByID(l.getID()));
	}

	@Test
	public void testGetLocationByName() throws SQLException {
		assertEquals(l, Location.getLocationByName(l.getName()));
	}

	@Test
	public void testGetLocationForItem() throws SQLException {
		Set<Location> s = Location.getLocationForItem(i);
		assertTrue(s.contains(l));
		assertEquals(1, s.size());
	}

	@Test
	public void testGetTopLevelLocations() throws SQLException {
		Set<Location> ret = Location.getTopLevelLocations();
		assertTrue(ret.contains(l));
		assertFalse(ret.contains(m));
	}

	@Test
	public void testMakeLocation() throws SQLException {
		Location f = Location.makeLocation("LocationTESTING123456", m);
		assertNotNull(f);
	}

	@Test
	public void testDeleteLocation() throws SQLException {
		m.deleteLocation();
		assertNull(Location.getLocationByID(m.getID()));
	}

	@Test
	public void testGetID() {
		//Cannot test?
	}

	@Test
	public void testGetItems() throws SQLException {
		Set<Item> items = l.getItems();
		assertNotNull(items);
		assertTrue(items.contains(i));
		assertTrue(items.size() == 1);
	}

	@Test
	public void testGetName() {
		assertEquals("LocationTESTING1234", l.getName());
	}

	@Test
	public void testGetParent() throws SQLException {
		assertEquals(l, m.getParent());
	}

	@Test
	public void testSetName() throws SQLException {
		assertTrue(l.setName("LocationTESTING123456"));
		assertEquals("LocationTESTING123456", l.getName());
	}

	@Test
	public void testSetParent() throws SQLException {
		assertFalse(l.setParent(m));
		assertNull(l.getParent());
		assertTrue(Location.getTopLevelLocations().contains(l));
		assertTrue(m.setParent(null));
		assertNull(l.getParent());
		assertTrue(Location.getTopLevelLocations().contains(m));
		assertTrue(l.setParent(m));
		assertFalse(Location.getTopLevelLocations().contains(l));
		assertTrue(m.getChildLocations().contains(l));
		assertEquals(m, l.getParent());
		assertNull(m.getParent());
	}

	@Test
	public void testGetChildLocations() throws SQLException {
		assertTrue(l.getChildLocations().contains(m));
		assertTrue(m.getChildLocations().isEmpty());
	}

}
