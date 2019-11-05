package hu.pazsit.keysearch;

import java.util.Comparator;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntBiFunction;

/**
 * KeyValueSorter.java
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
public class KeyValueSorter {
    private static IntUnaryOperator weightIncrementalLikeBinary = cnt -> cnt * 2;
    private static IntUnaryOperator weightIncrementalForContainmentCombined = cnt -> cnt + 1;

    /** compares the number of Any's in a keylist */
    public static final <K> ToIntBiFunction<MultiKey<K>, MultiKey<K>> sortByAnyCount() {
        return (k1, k2) -> Integer.compare(getAnyCount(k1), getAnyCount(k2));
    }
    /** compares by the size of the keylist */
    public static final <K> ToIntBiFunction<MultiKey<K>, MultiKey<K>> keySizeSorter() {
        return (k1, k2) -> Integer.compare(k1.getKeySize(), k2.getKeySize());
    }
    /** compares keys like a binary value representation based on the place of the Any values */
    public static final <K> ToIntBiFunction<MultiKey<K>, MultiKey<K>> sortByAnyWeightBinary() {
        return (k1, k2) -> Integer.compare(
                anyValueWeight(k1, weightIncrementalLikeBinary),
                anyValueWeight(k2, weightIncrementalLikeBinary)
        );
    }
    /** compares keys like a binary value representation based on the place of the Any values */
    public static final <K> ToIntBiFunction<MultiKey<K>, MultiKey<K>> sortByAnyWeightIncremental() {
        return (k1, k2) -> Integer.compare(
                anyValueWeight(k1, weightIncrementalForContainmentCombined),
                anyValueWeight(k2, weightIncrementalForContainmentCombined)
        );
    }

    public static <K> Comparator<MultiKey<K>> sortBy(ToIntBiFunction<MultiKey<K>, MultiKey<K>>... sorters) {
        return (k1, k2) -> {
            int result = 0;
            for (ToIntBiFunction<MultiKey<K>, MultiKey<K>> sorter : sorters) {
                result = sorter.applyAsInt(k1, k2);
                if (result != 0) {
                    return result;
                }
            }
            return result;
        };
    }

    /**
     * Sort by: KeySize, Any Containment number
     * @param <K>
     * @return
     */
    public static <K> Comparator<MultiKey<K>> closesMatch() {
        return KeyValueSorter.sortBy(keySizeSorter(), sortByAnyWeightBinary());
    }

    /**
     * Sort by: KeySize, Any weight
     * (Any weight: like a binary value representation based on the place of the any values)
     * @param <K>
     * @return
     */
    public static <K> Comparator<MultiKey<K>> revertMatch() {
        return KeyValueSorter.sortBy(keySizeSorter(), sortByAnyWeightBinary());
    }

    /**
     * Sort by: weightIncremental, anyContainment, Keysize
     * (weightIncremental: applies incremental values to the any values, allows weight equality between patterns (*,a2,a3 == a1,*,*))
     * @param <K>
     * @return
     */
    public static <K> Comparator<MultiKey<K>> anyWeightAndContainmentCombined() {
        return KeyValueSorter.sortBy(sortByAnyWeightIncremental(), sortByAnyCount(), keySizeSorter());
    }

    private static <K> int anyValueWeight(MultiKey<K> k1, IntUnaryOperator f) {
        int anyWeight = 0;
        ListIterator<K> it = k1.iterator(true);
        int cnt = 1;
        while (it.hasPrevious()) {
            if (Objects.equals(it.previous(), k1.any())) {
                anyWeight += cnt;
            }
            cnt = f.applyAsInt(cnt);
        }

        return anyWeight;
    }

    private static <K> int getAnyCount(MultiKey<K> k1) {
        AtomicInteger index = new AtomicInteger();
        k1.iterator().forEachRemaining(t -> {
            if (Objects.equals(t, k1.any())) {
                index.incrementAndGet();
            }
        });

        return index.get();
    }
}
