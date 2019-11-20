package hu.pazsit.keysearch;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * KeyComparisonRelation.java
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
@SuppressWarnings("unchecked")
public class KeyComparisonRelation<K> {
    private static final KeyComparisonRelation<Object> equalRelation = new KeyComparisonRelation<>(
    		(k1, k2) -> Objects.equals(k1, k2)
	);
    private static final Function<Object, KeyComparisonRelation<? extends Object>> equalAndAnyRelation = anyValue ->
		    new KeyComparisonRelation<>(
		    		(k1, k2) -> 
		    		Objects.equals(k1, anyValue)
		    		|| Objects.equals(k2, anyValue)
		    		|| Objects.equals(k1, k2)
    		);
    private static final KeyComparisonRelation<? extends Object> equalAndNullAnyRelation = equalAndAnyRelation.apply(null);

    private final BiPredicate<K, K> rule;

    public KeyComparisonRelation(BiPredicate<K, K> rule) {
        this.rule = rule;
    }

    public BiPredicate<K, K> getRule() {
        return this.rule;
    }

    /** 
     * Be aware to use the proper anyValue for the expected behaviour!
     * @param <T>
     * @param anyValue Should source from {@link MultiKey#any}
     * @return
     */
    public static <T> KeyComparisonRelation<T> equalAndAny(T anyValue) {
    	if (Objects.isNull(anyValue)) {
    		return (KeyComparisonRelation<T>) equalAndNullAnyRelation;
    	}
    		
    	return (KeyComparisonRelation<T>) equalAndAnyRelation.apply(anyValue);
    }

    public static <T> KeyComparisonRelation<T> equal() {
		return (KeyComparisonRelation<T>) equalRelation;
    }
}
