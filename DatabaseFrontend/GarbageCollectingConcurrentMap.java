package DatabaseFrontend;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GarbageCollectingConcurrentMap<K, V> {

	static class CleanupThread<K, V> extends Thread {
		CleanupThread() {
			setPriority(Thread.MAX_PRIORITY);
			setName("GarbageCollectingConcurrentMap-cleanupthread");
			setDaemon(true);
		}

		@Override
		public void run() {
			while (true)
				try {
					GarbageReference<K, ?> ref = (GarbageReference<K, V>) referenceQueue.remove();
					while (true) {
						ref.map.remove(ref.key);
						ref = (GarbageReference<K, ?>) referenceQueue.remove();
					}
				} catch (final InterruptedException e) {
					// ignore
				}
		}
	}

	static class GarbageReference<K, V> extends WeakReference<V> {
		final K key;
		final ConcurrentMap<K, V> map;

		GarbageReference(final K key, final V value, final ConcurrentMap<K, V> map) {
			super(value, referenceQueue);
			this.key = key;
			this.map = map;
		}
	}

	private final static ReferenceQueue referenceQueue = new ReferenceQueue();

	static {
		new CleanupThread<Object, Object>().start();
	}

	private final ConcurrentMap<K, GarbageReference<K, V>> map = new ConcurrentHashMap<K, GarbageReference<K, V>>();

	public void clear() {
		map.clear();
	}

	public V get(final K key) {
		final GarbageReference<K, V> ref = map.get(key);
		return ref == null ? null : ref.get();
	}

	public V getOrPut(final K key, final V value) {
		if (key == null || value == null)
			throw new NullPointerException();
		final GarbageReference<K, V> reference = new GarbageReference(key, value, map);
		final GarbageReference<K, V> old = map.putIfAbsent(key, reference);
		if (old != null) {
			final V temp = old.get();
			if (temp != null)
				return temp;
		}
		return reference.get();
	}

	public Collection<K> keySet() {
		return map.keySet();
	}

	public void put(final K key, final V value) {
		if (key == null || value == null)
			throw new NullPointerException();
		final GarbageReference<K, V> reference = new GarbageReference(key, value, map);
		map.put(key, reference);
	}

	public void remove(final long id) {
		map.remove(map.get(id));
	}
}
