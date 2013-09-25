package databaseTester;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import databaseBackendInterface.IItem;
import databaseBackendInterface.IUser;
import databaseBackendInterface.NoSuchTagException;
import databaseBackendInterface.NullTagsException;
import databaseBackendInterface.NoSuchUserException;
import fakeDatabase.fakeItem;
import fakeDatabase.fakeUser;

public class FakeItemTester {
	public static final int FAKE_ITEM_COST = 100;
	public static final String FAKE_ITEM_LOCATION = "Location";
	public static final String FAKE_ITEM_DESCRIPTION = "Description";
	public static final String FAKE_ITEM_NAME = "Name";
	public static final String[] tags = new String[] {"A", "BC", "DEF", "GH I"};
	private IItem item;
	private IUser u;

	@Before
	public void setUp() throws Exception {
		u = FakeUserTester.makeUser();
		item = makeItem(u);
	}

	public static fakeItem makeItem(IUser u) throws NullTagsException, NoSuchUserException {
		return new fakeItem(FAKE_ITEM_NAME, FAKE_ITEM_DESCRIPTION, FAKE_ITEM_LOCATION, new ArrayList<String>(Arrays.asList(tags)), FAKE_ITEM_COST, u);
	}

	@Test
	public void testFakeItem() {
		try {
			new fakeItem("", "", "", null, 0, u);
			fail("Null tags list doesn't throw an exception!");
		} catch (Exception e) {
			assertTrue(e instanceof NullTagsException);
		}
		try {
			new fakeItem("", "", "", new ArrayList<String>(Arrays.asList(tags)), 0, null);
			fail("Null seller doesn't throw an exception!");
		} catch (Exception e) {
			assertTrue(e instanceof NoSuchUserException);
		}		
	}

	@Test
	public void testGetTitle() {
		assertEquals(item.getTitle(), FAKE_ITEM_NAME);
	}

	@Test
	public void testSetTitle() {
		String newTitle = "Test123";
		item.setTitle(newTitle);
		assertEquals(item.getTitle(), newTitle);
	}

	@Test
	public void testGetDescription() {
		assertEquals(item.getDescription(), FAKE_ITEM_DESCRIPTION);
	}

	@Test
	public void testSetDescription() {
		String newDescription = "Test123";
		item.setDescription(newDescription);
		assertEquals(item.getDescription(), newDescription);
	}

	@Test
	public void testGetLocation() {
		assertEquals(item.getLocation(), FAKE_ITEM_LOCATION);
	}

	@Test
	public void testSetLocation() {
		String newLocation = "Test123";
		item.setLocation(newLocation);
		assertEquals(item.getLocation(), newLocation);
	}

	@Test
	public void testSetPrice() {
		assertEquals(item.getPriceInCents(), FAKE_ITEM_COST);
	}

	@Test
	public void testIsBought() {
		assertFalse(item.beenBought());
	}

	@Test
	public void testSetBought() {
		item.setBought();
		assertTrue(item.beenBought());
	}

	@Test
	public void testGetTags() {
		assertArrayEquals(item.getTags(), tags);
	}

	@Test
	public void testGetSeller() {
		assertEquals(item.getSeller(), u);
	}

	@Test
	public void testAddTag() {
		String added = "JKLMN";
		item.addTag(added);
		for (String t : item.getTags())
			assertTrue(Arrays.asList(tags).contains(t) || t.equals(added));
		assertTrue(item.hasTag(added));
	}

	@Test
	public void testRemoveTag() {
		Collection<String> removed = new ArrayList<String>();
		for (int i = 0; i < tags.length; i++) {
			String toRemove = (String) tags[i];
			removed.add(toRemove);
			if (!item.removeTag(toRemove))
				fail("This tag should exist!");
			for (String t : tags) {
				boolean removedContains = removed.contains(t);
				boolean itemContains = Arrays.asList(item.getTags()).contains(t);
				if (!removedContains && !itemContains)
					fail("Deleted wrong tag!: " + t + "(" + 
							Arrays.toString(removed.toArray()) + ", " + 
							Arrays.toString(item.getTags()));
				else if (removedContains && itemContains)
					fail ("Didn't delete item!: " + t + "(" + 
							Arrays.toString(removed.toArray()) + ", " + 
							Arrays.toString(item.getTags()));
			}
			for (String t : removed) {
				if (item.removeTag(t))
						fail("Removed non-existant tag!");
			}
		}
	}

	@Test
	public void testGetPriceInCents() {
		assertEquals(item.getPriceInCents(), FAKE_ITEM_COST);
	}

	@Test
	public void testSetPriceInCents() {
		item.setPriceInCents(FAKE_ITEM_COST+100);
		assertEquals(item.getPriceInCents(), FAKE_ITEM_COST+100);
	}

	@Test
	public void testHasTag() {
		for (String tag : tags) {
			assertTrue(item.hasTag(tag));
		}
	}

}
