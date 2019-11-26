package hu.pazsit.keysearch;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;

import org.junit.Assert;
import org.junit.Test;

import hu.pazsit.keysearch.collection.SimpleMultiKeyMap;

public class SearchMatches {
	private static final String ANIMAL = "Animal";
	private static final String BLACK_WIDOW = "Black Widow";
	private static final String AVENGERS = "Avengers";
    private static final String GOTG = "Guardians of the Galaxy";
    private static final String EARTHLINGS = "Earthlings";
    private static final String HUMAN = "Human";
    private static final String TREE = "Tree";
    private static final String GOD = "God";

    private static final String HAWKEYE = "Hawkeye";
    private static final String CLINT_BARTON = "Clint Barton";
    private static final String THOR = "Thor";
    private static final String HULK = "Hulk";
    
    private ToIntBiFunction<MultiKey<String>, MultiKey<String>> lexicalCompare = (k1,k2) -> k1.toString().compareTo(k2.toString());
    private Supplier<SimpleMultiKey<String>> lteLengthAnyMatchWithNullKeyValue = () ->
        new CustomMultiKey(
            KeyLengthRule.matchSmaller(),
            KeyComparisonRelation.equalAndAny(null),
            null
        );
    private SimpleMultiKeyMap<String, String> createMap() {
    	return new SimpleMultiKeyMap<String, String>(
    			lteLengthAnyMatchWithNullKeyValue,
    			KeyValueSorter.sortBy(lexicalCompare));
    }

    @Test
    public void findAllHumans() {
		MultiKeyMap<String, String> map = mapValues(createMap());
    	List<String> results = map.getAny(map.newKey().addAny().add(HUMAN));
        
    	Assert.assertEquals(6, results.size());
    	Assert.assertEquals("Natasha Romanoff", results.get(0));
    	Assert.assertEquals("Peter Quill", results.get(5));
    }
    
    @Test
    public void findAllGOTG() {
    	MultiKeyMap<String, String> map = mapValues(createMap());
    	List<String> results = map.getAny(map.newKey().add(GOTG));
    	
    	Assert.assertEquals(4, results.size());
    	Assert.assertEquals("Rocket Raccoon", results.get(0));
    	Assert.assertEquals("Grut", results.get(3));
    }
    
    @Test
    public void findThor() {
    	MultiKeyMap<String, String> map = mapValues(createMap());
    	List<String> results = map.getAny(map.newKey().addAny().addAny().add(THOR));
    	
    	Assert.assertEquals(3, results.size());
    	Assert.assertEquals(THOR, results.get(0));
    	Assert.assertEquals(THOR + " with the Guardians", results.get(2));
    	
    	List<String> singleResult = map.getAny(map.newKey().add("Asgardians").addAny().add(THOR));
    	
    	Assert.assertEquals(1, singleResult.size());
    	Assert.assertEquals(THOR, singleResult.get(0));
    }
    
    @Test
    public void findAll() {
    	MultiKeyMap<String, String> map = mapValues(createMapWithLengthSorter());
    	List<String> results = map.getAny(map.newKey().addAny().addAny().addAny());
    	
    	Assert.assertEquals(11, results.size());
    	Assert.assertEquals(THOR + " with the " + AVENGERS, results.get(0));
    	Assert.assertEquals("Rocket Raccoon", results.get(10));
    }

    private SimpleMultiKeyMap<String, String> createMapWithLengthSorter() {
    	return new SimpleMultiKeyMap<String, String>(
    			lteLengthAnyMatchWithNullKeyValue,
    			KeyValueSorter.sortBy( (k1, k2) ->
					Integer.compare(k1.toString().length(), k2.toString().length())
				)
		);
	}
    
	private MultiKeyMap<String, String> mapValues(SimpleMultiKeyMap<String, String> simpleMultiKeyMap) {
        MultiKeyMap<String, String> map = simpleMultiKeyMap;

        map.put(map.newKey().add(EARTHLINGS).add(HUMAN).add("Ben Parker"), "Ben Parker");
        map.put(map.newKey().add(AVENGERS).add(HUMAN).add(HAWKEYE), CLINT_BARTON);
        map.put(map.newKey().add(AVENGERS).add(HUMAN).add("SpiderMan"), "Peter Parker");
        map.put(map.newKey().add(AVENGERS).add(HUMAN).add(BLACK_WIDOW), "Natasha Romanoff");
        map.put(map.newKey().add(AVENGERS).add(HUMAN).add(HULK), HULK);
        map.put(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord"), "Peter Quill");
        map.put(map.newKey().add(GOTG).add(TREE).add("Grut"), "Grut"); 
        map.put(map.newKey().add(GOTG).add(ANIMAL).add("Rocket Raccoon"), "Rocket Raccoon");
        map.put(map.newKey().add(GOTG).add(GOD).add(THOR), THOR + " with the Guardians");
        map.put(map.newKey().add(AVENGERS).add(GOD).add(THOR), THOR + " with the " + AVENGERS);
        map.put(map.newKey().add("Asgardians").add("Asgardian").add(THOR), THOR);

        return map;
    }
}
