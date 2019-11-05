package hu.pazsit.keysearch;

import java.util.Iterator;
import java.util.ListIterator;

/**
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @param <K>
 */
public interface MultiKey<K> extends Iterable<K> {
    public static final String ANY = "*";

    /**
     * add/set a new key value to the key list
     * @param key
     * @return
     */
    MultiKey<K> add(K key);

    /**
     * add/set a predefined wildcard value to the key list
     * @return
     */
    MultiKey<K> addAny();

    /**
     * Gets the predefined wildcard value
     * @return
     */
    K any();

    boolean isEqual(MultiKey<K> k);

    int getKeySize();

    /**
     * @param fromEnd true if you want a reverse {@link ListIterator}
     * @return
     */
    ListIterator<K> iterator(boolean fromEnd);

    /**
     * Key Length Check Strategy
     * @return
     */
    default KeyLengthRule<MultiKey<K>> getKeyRule() {
        return KeyLengthRule.matchIdentical();
    }

    /**
     * Key Comparison Type Strategy
     * @return
     */
    default KeyComparisonRelation<K> getComparisonRelation() {
        return KeyComparisonRelation.equalAndAny(this.any());
    }

    @Override
    default Iterator<K> iterator() {
        return iterator(false);
    }
}
