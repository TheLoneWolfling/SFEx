package ApplicationLogic;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import DatabaseFrontend.Keyword;
import DatabaseFrontend.Location;
import DatabaseFrontend.Permission;

public class LocationControl {
	public final Control p;

	public LocationControl(Control p) {
		this.p = p;
	}

	public boolean renameLocation(String location, String newName) {
		if (!p.accountControl.isLoggedInUserAllowed(Permission.EditLocations))
			return false;
		try {
			Location l = unwrap(location);
			if (l == null)
				return false;
			return l.setName(newName);
		} catch (SQLException e) {
			return false;
		}

	}

	public Set<String> getChildren(String location) {
		try {
			Location l = Location.getLocationByName(location);
			if (l != null)
				return wrap(l.getChildLocations());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	public Set<String> getValidSubLocations(String location) {
		Set<String> locations = new HashSet<String>();
		Queue<Location> toCheck = new ArrayDeque<>();
		Location l = unwrap(location);
		do {
			if (l != null) {
				locations.add(l.getName());
				try {
					for (Location m : l.getChildLocations())
						toCheck.add(m);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			l = toCheck.poll();
		} while (!toCheck.isEmpty());
		return locations;
	}

	public Set<String> getTopLevelLocations() {
		try {
			return wrap(Location.getTopLevelLocations());
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}

	public Location unwrap(String location) {
		try {
			return Location.getLocationByName(location);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	Set<String> wrap(Set<Location> locations) {
		Set<String> toRet = new HashSet<String>();
		for (Location l : locations)
			toRet.add(l.getName());
		return toRet;
	}

	public Set<Location> unwrap(Set<String> locations) {
		Set<Location> toRet = new HashSet<Location>();
		for (String l : locations)
			toRet.add(unwrap(l));
		return toRet;
	}
}