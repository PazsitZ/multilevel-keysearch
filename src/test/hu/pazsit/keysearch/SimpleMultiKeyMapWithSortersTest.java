package hu.pazsit.keysearch;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

import hu.pazsit.keysearch.collection.SimpleMultiKeyMap;

public class SimpleMultiKeyMapWithSortersTest {
	private static final String COMIC_CHARACTERS = "Comic Characters";
	private static final String AVENGERS = "Avengers";
    private static final String GOTG = "Guardians of the Galaxy";
    private static final String EARTHLING = "earthlings";
    private static final String HUMAN = "Human";
    private static final String TREE = "Tree";

    private static final String HAWKEYE = "Hawkeye";
    private static final String CLINT_BARTON = "Clint Barton";
    private static final String THOR = "Thor";
    private static final String HULK = "Hulk";
    
    Supplier<SimpleMultiKey<String>> lteLengthAnyMatchWithNullKeyValue = () ->
        new CustomMultiKey(
            KeyLengthRule.matchSmaller(),
            KeyComparisonRelation.equalAndAny(MultiKey.ANY),
            MultiKey.ANY
        );
    private SimpleMultiKeyMap<String, Object> createMap(Comparator<MultiKey<String>> sorter) {
    	return new SimpleMultiKeyMap<String, Object>(
    			lteLengthAnyMatchWithNullKeyValue,
    			sorter);
    }

    @Test
    public void testWeightsRevertMatch() {
        MultiKeyMap<String, Object> map = mapValues(createMap(KeyValueSorter.revertMatch()));
    	List<Object> results = map.getAny(map.newKey().add(AVENGERS));
        
    	Assert.assertEquals(CLINT_BARTON, results.get(0));
    	Assert.assertEquals(THOR, results.get(1));
    	Assert.assertEquals(HULK, results.get(2));
    	Assert.assertEquals(COMIC_CHARACTERS, results.get(3));
    }


    @Test
    public void testWeightsClosesMatch() {
        MultiKeyMap<String, Object> map = mapValues(createMap(KeyValueSorter.closesMatch()));
        List<Object> results = map.getAny(map.newKey().add(AVENGERS));
        
        Assert.assertEquals(CLINT_BARTON, results.get(0));
//        Assert.assertEquals(HULK, results.get(1));	// undeterministic, same anyValue: 2
//        Assert.assertEquals(THOR, results.get(2));	// undeterministic, same anyValue: 2
    	Assert.assertEquals(COMIC_CHARACTERS, results.get(3));
    }
    
    @Test
    public void testWeightsAnyWeightAndContainmentCombined() {
    	MultiKeyMap<String, Object> map = mapValues(createMap(KeyValueSorter.anyWeightAndContainmentCombined()));
    	List<Object> results = map.getAny(map.newKey().add(AVENGERS));
    	
    	Assert.assertEquals(CLINT_BARTON, results.get(0));
    	Assert.assertEquals(THOR, results.get(1));
    	Assert.assertEquals(HULK, results.get(2));
    	Assert.assertEquals(COMIC_CHARACTERS, results.get(3));
    }


    private MultiKeyMap<String, Object> mapValues(SimpleMultiKeyMap<String, Object> simpleMultiKeyMap) {
    	return mapValues(simpleMultiKeyMap, true);
    }
    
    private MultiKeyMap<String, Object> mapValues(SimpleMultiKeyMap<String, Object> simpleMultiKeyMap, boolean allAny) {
        MultiKeyMap<String, Object> map = simpleMultiKeyMap;

    	map.put(map.newKey().addAny().addAny().addAny(), COMIC_CHARACTERS);			// weightBinary:7; weightInc:6, anyContainment: 3
        map.put(map.newKey().add(AVENGERS).addAny().addAny(), THOR); 				// weightBinary:3; weightInc:3, anyContainment: 2
        map.put(map.newKey().add(AVENGERS).addAny().add(HAWKEYE), CLINT_BARTON);	// weightBinary:2; weightInc:2, anyContainment: 1
        map.put(map.newKey().addAny().addAny().add(HULK), HULK);					// weightBinary:6; weightInc:5, anyContainment: 2
        map.put(map.newKey().add(EARTHLING).add(HUMAN).add("Ben Parker"), "Ben Parker"); 
        map.put(map.newKey().add(GOTG).add(TREE).add("Grut"), "Grut"); 
        map.put(map.newKey().add(GOTG).add("Animal").add("Rocket Raccoon"), "Rocket Raccoon");
        map.put(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord"), "Peter Quill");
//        map.put(map.newKey().add(AVENGERS).add(HUMAN).add("SpiderMan"), "Peter Parker");// weightBinary:0; weightInc:0, anyContainment: 0

        return map;
    }
}
