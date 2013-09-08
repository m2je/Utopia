/**
 * 
 */
package ir.utopia.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author salarkia
 *
 */
public class Cache<K,V> {
	private static Logger logger=Logger.getLogger(Cache.class.getName());
	private Map<K,V>map;
	private LoadingCache<K,V> localCache;
	/**
	 * 
	 */
	public Cache() {
		this.map=new ConcurrentHashMap<K, V>();
	}

	/**
	 * @param initialCapacity
	 */
	public Cache(int initialCapacity) {
		this.map=new ConcurrentHashMap<K, V>(initialCapacity);
	}

	/**
	 * @param m
	 */
	public Cache(Map<K,V> m) {
			this.map=new ConcurrentHashMap<K, V>(m);
	}

	/**
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public Cache(int initialCapacity, float loadFactor) {
		this.map=new ConcurrentHashMap<K, V>(initialCapacity, loadFactor);
	}
	/**
	 * 
	 * @param maximumsize
	 * @param expireAfter
	 * @param loader
	 */
	public Cache(Long maximumsize,Long expireAfter,final ir.utopia.core.util.CacheLoader<K, V> loader){
		LoadingCache<K, V> localCache = CacheBuilder.newBuilder().
				expireAfterAccess(expireAfter, TimeUnit.MILLISECONDS).maximumSize(maximumsize)
				.build(new CacheLoader<K, V>(){

			@Override
			public V load(K k) throws Exception {
				
				return loader.load(k);
			}});
		
		this.localCache=localCache;
	}
	public int size() {
		return map!=null?map.size():(int)localCache.size();
	}

	public boolean isEmpty() {
		return map!=null?map.isEmpty():localCache.size()==0;
	}

	



	public V get(Object key) {
		try {
			return map!=null?map.get(key):localCache.get((K)key);
		} catch (ExecutionException e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}

	public void put(K key, V value) {
		if(map!=null){
			map.put(key, value);
		}else{
			localCache.put(key, value);
		}
	}

	public V remove(K key) {
		if(map!=null){
			 return map.remove(key);
		}else{
			V value=null;
			try {
				value = localCache.get(key);
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
			}
			localCache.invalidate(key);
			return value;
		}
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<V> values() {
		return map.values();
	}

	public void clear(){
		map.clear();
	}


}
