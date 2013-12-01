package DatabaseFrontend;

import java.util.EnumSet;
import java.util.Set;

public enum Permission {
	DeleteBids(0), EditCategories(1), EditKeywords(2), EditLocations(3), EditOtherItems(
			4), EditOtherUsers(5), EditOwnItems(6), EditOwnUser(7), EditUserPermissions(
			8), MakeBid(9), MakeItem(10), ViewBids(11), ViewContactInfo(12), ViewEmail(
			13);

	private static Permission[] bitMap = new Permission[64];

	static {
		for (final Permission p : Permission.values()) {
			if (bitMap[p.i] != null)
				throw new RuntimeException("Internal error: " + bitMap[p.i]
						+ " has the same bit (" + p.i + ")assigned as " + p);
			bitMap[p.i] = p;
		}
	}

	static EnumSet<Permission> fromLong(final long input) {
		final EnumSet<Permission> e = EnumSet.noneOf(Permission.class);
		for (int i = 63; i >= 0; i--)
			if ((input & (1L << i)) != 0) {
				final Permission p = bitMap[i];
				if (p == null)
					throw new RuntimeException(
							"Internal error: invalid bit set: " + i
									+ "(value: " + input + ")");
				e.add(p);
			}
		return e;
	}

	static long toLong(final Set<Permission> permissions) {
		long res = 0;
		for (final Permission p : permissions)
			res += 1L << p.i;
		return res;
	}

	public final int i;

	private Permission(final int i) {
		if (i > 63 || i < 0)
			throw new RuntimeException(
					"Internal error: Invalid bit for permission " + this + ": "
							+ i);
		this.i = i;
	}
}