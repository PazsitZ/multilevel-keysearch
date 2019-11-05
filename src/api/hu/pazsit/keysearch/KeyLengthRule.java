package hu.pazsit.keysearch;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * KeyLengthRule.java
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @param <K extends MultiKey<?>>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
public class KeyLengthRule<K extends MultiKey<?>> {
    private final BiPredicate<K, K> rule;

    public KeyLengthRule(BiPredicate<K, K> rule) {
        this.rule = rule;
    }

    public BiPredicate<K, K> getRule() {
        return this.rule;
    }

    public static <T extends MultiKey<?>> KeyLengthRule<T> matchIdentical() {
        return new KeyLengthRule<>(
            (k1, k2) -> 
            	Objects.equals(k1.getKeySize(), k2.getKeySize())
        );
    }

    /**
     * smaller or equal
     * @return
     */
    public static <T extends MultiKey<?>> KeyLengthRule<T> matchSmaller() {
        return new KeyLengthRule<>(
            (k1, k2) ->  k1.getKeySize() >= k2.getKeySize()
        );
    }
}
