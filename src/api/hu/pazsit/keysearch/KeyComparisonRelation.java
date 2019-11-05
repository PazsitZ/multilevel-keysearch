package hu.pazsit.keysearch;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * KeyComparisonRelation.java
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
public class KeyComparisonRelation<K> {
    private final BiPredicate<K, K> rule;

    public KeyComparisonRelation(BiPredicate<K, K> rule) {
        this.rule = rule;
    }

    public BiPredicate<K, K> getRule() {
        return this.rule;
    }

    /** Be aware to use the proper anyValue for the expected behaviour!
     *
     * @param <T>
     * @param anyValue Should source from {@link MultiKey#any}
     * @return
     */
    public static <T> KeyComparisonRelation<T> equalAndAny(T anyValue) {
        return new KeyComparisonRelation(
            (k1, k2) -> 
                Objects.equals(k1, anyValue)
                    || Objects.equals(k2, anyValue)
                    || Objects.equals(k1, k2)
        );
    }

    public static <T> KeyComparisonRelation<T> equal() {
        return new KeyComparisonRelation(
            (k1, k2) -> 
                Objects.equals(k1, k2)
        );
    }
}
