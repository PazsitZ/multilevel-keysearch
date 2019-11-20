package hu.pazsit.keysearch;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Base Key implementation with the (interface defined) default Rules
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
public class SimpleMultiKey<K> implements MultiKey<K> {
    private final List<K> keys = new LinkedList<>();

    @Override
    public MultiKey<K> add(K key) {
        keys.add(key);
        return this;
    }

    @Override
    public MultiKey<K> addAny() {
        keys.add(any());
        return this;
    }

    @Override
    public K any() {
        return null;
    }

    @Override
    public boolean isEqual(MultiKey<K> k) {
        if (!getKeyRule().getRule().test(this, k)) {
            return false;
        }

        boolean identicalPattern = true;
        BiPredicate<K, K> compRule = getComparisonRelation().getRule();

        Iterator<K> it = k.iterator();
        for (K key : keys) {
            if (it.hasNext()) {
	        	K kItem = it.next();
				if (!(compRule.test(key, kItem))) {
	                return false;
	            }
        	}
        }

        return identicalPattern;
    }

    @Override
    public int getKeySize() {
        return keys.size();
    }

    @Override
    public ListIterator<K> iterator(boolean fromEnd) {
        return fromEnd ? keys.listIterator(keys.size()) : keys.listIterator();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime + result + ((keys == null) ? 0 : keys.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SimpleMultiKey<K> other = (SimpleMultiKey<K>) obj;

        return this.isEqual(other);
    }

    @Override
    public String toString() {
        return keys.stream()
                .map(v -> Objects.equals(v, any()) ? MultiKey.ANY : v.toString())
                .collect(Collectors.joining(", "));
    }
}
