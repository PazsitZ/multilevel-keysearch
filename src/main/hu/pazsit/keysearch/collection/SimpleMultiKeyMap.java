package hu.pazsit.keysearch.collection;

import hu.pazsit.keysearch.KeyValueSorter;
import hu.pazsit.keysearch.MultiKey;
import hu.pazsit.keysearch.MultiKeyMap;
import hu.pazsit.keysearch.SimpleMultiKey;

import static hu.pazsit.keysearch.MultiKey.pool;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * SimpleMultiKeyMap
 * Base implementation of the {@link MultiKeyMap} interface
 * Provides constructor to configure the {@link MultiKey} supplier and the Key Sorter: {@link KeyValueSorter}
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
public class SimpleMultiKeyMap<K, V> implements MultiKeyMap<K, V> {
    private volatile Map<MultiKey<K>, V> internalMap = new ConcurrentHashMap<>();

    private final Supplier<SimpleMultiKey<K>> keyValueCreator;
    private final Comparator<MultiKey<K>> sorter;

    public final Function<MultiKey<K>, V> toNull = k -> null;
    public final Function<MultiKey<K>, V> toAnyFirstOrNull = k -> getAny(k).stream().findFirst().orElse(null);

    /**
     * @uses {@link SimpleMultiKey} and {@link KeyValueSorter#revertMatch()}
     */
    public SimpleMultiKeyMap() {
        this(SimpleMultiKey::new);
    }

    /**
     * @uses {@link KeyValueSorter#revertMatch()}
     * @param keyValue
     */
    public SimpleMultiKeyMap(Supplier<SimpleMultiKey<K>> keyValue) {
        this(keyValue, KeyValueSorter.revertMatch());
    }

    public SimpleMultiKeyMap(Supplier<SimpleMultiKey<K>> keyValue, Comparator<MultiKey<K>> sorter) {
        this.keyValueCreator = keyValue;
        this.sorter = sorter;
    }

    @Override
    public MultiKey<K> newKey() {
        return keyValueCreator.get();
    }

    @Override
    public V put(MultiKey<K> key, V value) {
        return internalMap.put(key, value);
    }

    @Override
    public V get(MultiKey<K> key) {
        return getIfAbsent(key, toNull);
    }

    @Override
    public V getIfAbsent(MultiKey<K> key, Function<MultiKey<K>, V> function) {
        return internalMap.getOrDefault(key, function.apply(key));
    }

    @Override
    public V getIfAbsentFirst(MultiKey<K> key) {
        return getIfAbsent(key, toAnyFirstOrNull);
    }

    @Override
    public List<V> getAny(MultiKey<K> key) {
    	List<MultiKey<K>> keySet = Collections.emptyList();
    	try {
    		keySet = pool.submit(() -> internalMap.keySet().stream().parallel()
		                .filter(k -> k.isEqual(key))
		                .sorted(getSorter())
		                .collect(Collectors.toList())
			).get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
        return keySet.stream()
                .map(k -> internalMap.get(k))
                .collect(Collectors.toList());
    }

    @Override
    public V remove(MultiKey<K> key) {
        return internalMap.remove(key);
    }

    @Override
    public Comparator<MultiKey<K>> getSorter() {
        return sorter;
    }

}
