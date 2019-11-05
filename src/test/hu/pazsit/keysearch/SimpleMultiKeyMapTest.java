package hu.pazsit.keysearch;

import hu.pazsit.keysearch.CustomMultiKey;
import hu.pazsit.keysearch.KeyComparisonRelation;
import hu.pazsit.keysearch.KeyLengthRule;
import hu.pazsit.keysearch.MultiKeyMap;
import hu.pazsit.keysearch.SimpleMultiKey;
import hu.pazsit.keysearch.collection.SimpleMultiKeyMap;

import java.util.function.Supplier;
import org.junit.Assert;
import org.junit.Test;

/**
 * SimpleMultiKeyMapTest.java
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
public class SimpleMultiKeyMapTest {
    private static final String AVENGERS = "Avengers";
    private static final String GOTG = "Guardians of the Galaxy";
    private static final String EARTHLING = "earthlings";

    private static final String HUMAN = "Human";
    private static final String TREE = "Tree";

    @Test
    public void testMapExactMatch() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(sameLengthAnyMatchWithNullKeyValue()));

        Assert.assertEquals("Peter Quill", map.get(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord")));
        Assert.assertEquals("Peter Quill", map.get(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord")));
        //System.out.println("StarLord - Peter Quill" + map.get(map.newKey().add(AVENGERS).add(HUMAN).add("Star-Lord")));
    }

    @Test
    public void testMapWithAny() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(sameLengthAnyMatchWithNullKeyValue()));

        Assert.assertEquals("Peter Quill", map.getIfAbsentFirst(map.newKey().add(GOTG).add(HUMAN).addAny()));
        Assert.assertEquals("Grut", map.getIfAbsentFirst(map.newKey().addAny().addAny().add("Grut")));
        Assert.assertEquals("Grut", map.getIfAbsentFirst(map.newKey().addAny().add(TREE).addAny()));
    }

    @Test
    public void testMapShorterKey() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(sameLengthAnyMatchWithNullKeyValue()));

        final String gotgHuman = (String) map.getIfAbsentFirst(map.newKey().add(GOTG).add(HUMAN));
        Assert.assertEquals("Peter Quill", gotgHuman);

        final Object tree = map.getIfAbsentFirst(map.newKey().addAny().add(TREE));
        Assert.assertEquals("Grut", tree);
    }

    @Test
    public void testMapMatchAny() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(sameLengthAnyMatchWithNullKeyValue()));

        final String anyVal = (String) map.getIfAbsentFirst(map.newKey().add(HUMAN));
        Assert.assertEquals(COMIC_CHARACTERS, anyVal);
    }
    
    @Test
    public void testMapNoMatchButAllAny() {
    	MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(sameLengthAnyMatchWithNullKeyValue()));
    	
    	final String anyVal = (String) map.getIfAbsentFirst(map.newKey().add("asd"));
    	Assert.assertEquals(COMIC_CHARACTERS, anyVal);
    }
    
    @Test
    public void testMapNoMatch() {
    	MultiKeyMap<String, Object> map = mapValues(
    			new SimpleMultiKeyMap<String, Object>(sameLengthAnyMatchWithNullKeyValue()),
    			false
		);
    	
    	final String anyVal = (String) map.getIfAbsentFirst(map.newKey().add("asd"));
    	Assert.assertEquals(null, anyVal);
    }
    
    @Test
    public void testMapLonger() {
    	MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(sameLengthAnyMatchWithNullKeyValue()));
    	
    	final String anyVal = (String) map.getIfAbsentFirst(map.newKey().add(EARTHLING).add(HUMAN).add("Ben Parker").add("noSuchKeyItem"));
    	Assert.assertEquals(null, anyVal);
    }

    private Supplier<SimpleMultiKey<String>> sameLengthAnyMatchWithNullKeyValue() {
        return () -> {
            return new CustomMultiKey(
                KeyLengthRule.matchSmaller(),
                KeyComparisonRelation.equalAndAny(null),
                null
            );
        };
    }

    private MultiKeyMap<String, Object> mapValues(SimpleMultiKeyMap<String, Object> simpleMultiKeyMap) {
    	return mapValues(simpleMultiKeyMap, true);
    }
    
    private MultiKeyMap<String, Object> mapValues(SimpleMultiKeyMap<String, Object> simpleMultiKeyMap, boolean allAny) {
        MultiKeyMap<String, Object> map = simpleMultiKeyMap;

        if (allAny) {
        	map.put(map.newKey().addAny().addAny().addAny(), COMIC_CHARACTERS);
        }
        map.put(map.newKey().add(AVENGERS).addAny().addAny(), "Thor"); // weightBinary:3; wieghtInc:3, anyContainment: 2
        map.put(map.newKey().add(EARTHLING).add(HUMAN).add("Ben Parker"), "Ben Parker"); // weightBinary:4; wieghtInc:3, anyContainment: 1
        map.put(map.newKey().add(GOTG).add(TREE).add("Grut"), "Grut");
        map.put(map.newKey().add(GOTG).add("Animal").add("Rocket Raccoon"), "Rocket Raccoon");
        map.put(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord"), "Peter Quill");
        map.put(map.newKey().add(AVENGERS).add(HUMAN).add("SpiderMan"), "Peter Parker");

        return map;
    }
    private static final String COMIC_CHARACTERS = "Comic Characters";
}
