/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.pazsit.keysearch;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 */
public interface MultiKeyMap<K, V> {

    /**
     * It creates a new key with the predefined configuration to fit  in correctly into the map
     * This is the safe and advised way to instantiate a new key
     * (You could create/insert keys manually, however keys with different configuration can be misbehave during comparison selection and ordering!)
     * @return
     */
    MultiKey<K> newKey();

    /**
     * Put a key into the map
     * @param key
     * @param value
     * @return
     */
    V put(MultiKey<K> key, V value);

    /**
     * Get exact match on the standard way
     * @param key
     * @return
     */
    V get(MultiKey<K> key);

    /**
     * Standard get with a fallback option defined on the function
     * @param key
     * @param function
     * @return
     */
    V getIfAbsent(MultiKey<K> key, Function<MultiKey<K>, V> function);

    /**
     * Standard get with a fallback option, gets the first of the {@link #getAny(hu.pazsit.keysearch.MultiKey)}
     * (the first depends on the {@link MultiKeyMap} Sorter and the {@link MultiKey} Rules)
     * @param key
     * @return
     */
    V getIfAbsentFirst(MultiKey<K> key);

    /**
     * Gets all of the metching values applied the defined/given {@link MultiKey} Rules and {@link MultiKeyMap} Sorter
     * @param key
     * @return
     */
    List<V> getAny(MultiKey<K> key);

    default Comparator<MultiKey<K>> getSorter() {
        return KeyValueSorter.revertMatch();
    }

    V remove(MultiKey<K> key);
}
