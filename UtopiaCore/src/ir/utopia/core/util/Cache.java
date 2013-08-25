/**
 * 
 */
package ir.utopia.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author salarkia
 *
 */
public class Cache<K,V> extends HashMap<K,V> {

	/**
	 * 
	 */
	public Cache() {
	}

	/**
	 * @param initialCapacity
	 */
	public Cache(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * @param m
	 */
	public Cache(Map<K,V> m) {
		super(m);
	}

	/**
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public Cache(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

}
