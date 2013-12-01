package DatabaseFrontend;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import DatabaseFrontend.GarbageCollectingConcurrentMap.GarbageReference;

public class ConnectionCache {

	private final ConcurrentMap<WeakCompRef, Connection> map = new ConcurrentHashMap<WeakCompRef, Connection>();

	private final ReferenceQueue<Thread> referenceQueue = new ReferenceQueue<Thread>();

	public Connection get(final Thread key) {
		cleanupStep();
		return map.get(new WeakCompRef(key));
	}

	private void cleanupStep() {
		WeakCompRef w = (WeakCompRef) referenceQueue.poll();
		if (w == null)
			return;
		Connection c = map.remove(w);
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getOrPut(final Thread key, final Connection value) {
		cleanupStep();
		if (key == null || value == null)
			throw new NullPointerException();
		final Connection old = map.putIfAbsent(new WeakCompRef(key,
				referenceQueue), value);
		if (old != null) {
			try {
				value.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return old;
		}
		return value;
	}

	private class WeakCompRef extends WeakReference<Thread> {

		private final int hash;
		private final long id;

		public WeakCompRef(Thread referent) {
			super(referent);
			id = referent.getId();
			hash = Long.valueOf(id).hashCode();
		}

		public WeakCompRef(Thread key, ReferenceQueue<Thread> referenceQueue) {
			super(key, referenceQueue);
			id = key.getId();
			hash = Long.valueOf(id).hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof WeakCompRef) {
				return id == ((WeakCompRef) obj).id;
			} else {
				return get().equals(obj);
			}
		}

		@Override
		public int hashCode() {
			return hash;
		}

	}
}
