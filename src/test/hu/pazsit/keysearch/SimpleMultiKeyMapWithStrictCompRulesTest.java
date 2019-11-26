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
 * SimpleMultiKeyMapWithStrictCompRulesTest.java <br>
 * The Comparison Rules defined as: <br>
 * {@link KeyLengthRule}: lengthwise it should be equal <br>  
 * {@link KeyComparisonRelation}: the comparison is using exact match only
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
public class SimpleMultiKeyMapWithStrictCompRulesTest {
    private static final String AVENGERS = "Avengers";
    private static final String GOTG = "Guardians of the Galaxy";
    private static final String EARTHLING = "earthlings";

    private static final String HUMAN = "Human";
    private static final String TREE = "Tree";
    
    Supplier<SimpleMultiKey<String>> identicalLengthEqualKeyMatchValue = () ->
        new CustomMultiKey(
            KeyLengthRule.matchIdentical(),
            KeyComparisonRelation.equal(),
            null
        );

    @Test
    public void testMapExactMatch() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(identicalLengthEqualKeyMatchValue));

        Assert.assertEquals("Peter Quill", map.get(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord")));
        Assert.assertEquals("Peter Quill", map.get(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord")));
        //System.out.println("StarLord - Peter Quill" + map.get(map.newKey().add(AVENGERS).add(HUMAN).add("Star-Lord")));
    }

    @Test
    public void testMapWithAny() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(identicalLengthEqualKeyMatchValue));

        Assert.assertEquals("Thor", map.getIfAbsentFirst(map.newKey().add(AVENGERS).addAny().addAny()));
    }

    @Test
    public void testMapShorterKey() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(identicalLengthEqualKeyMatchValue));

        final String gotgHuman = (String) map.getIfAbsentFirst(map.newKey().add(GOTG).add(HUMAN));
        Assert.assertNull(gotgHuman);
    }

    @Test
    public void testMapMatchAny() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(identicalLengthEqualKeyMatchValue));

        final String emptyVal = (String) map.getIfAbsentFirst(map.newKey().add(HUMAN));
        Assert.assertNull(emptyVal);
    }
    
    @Test
    public void testMapNoMatchButAllAny() {
    	MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(identicalLengthEqualKeyMatchValue));
    	
    	final String emptyVal = (String) map.getIfAbsentFirst(map.newKey().add("God"));
    	Assert.assertNull(emptyVal);
    }
    
    @Test
    public void testMapNoMatch() {
    	MultiKeyMap<String, Object> map = mapValues(
    			new SimpleMultiKeyMap<String, Object>(identicalLengthEqualKeyMatchValue),
    			false
		);
    	
    	final String anyVal = (String) map.getIfAbsentFirst(map.newKey().add("asd"));
    	Assert.assertNull(anyVal);
    }
    
    @Test
    public void testMapLonger() {
    	MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(identicalLengthEqualKeyMatchValue));
    	
    	final String anyVal = (String) map.getIfAbsentFirst(map.newKey().add(EARTHLING).add(HUMAN).add("Ben Parker").add("noSuchKeyItem"));
    	Assert.assertEquals(null, anyVal);
    }

    private MultiKeyMap<String, Object> mapValues(SimpleMultiKeyMap<String, Object> simpleMultiKeyMap) {
    	return mapValues(simpleMultiKeyMap, true);
    }
    
    private MultiKeyMap<String, Object> mapValues(SimpleMultiKeyMap<String, Object> simpleMultiKeyMap, boolean allAny) {
        MultiKeyMap<String, Object> map = simpleMultiKeyMap;

        if (allAny) {
        	map.put(map.newKey().addAny().addAny().addAny(), COMIC_CHARACTERS);
        }
        map.put(map.newKey().add(AVENGERS).addAny().addAny(), "Thor"); 					 // weightBinary:3; weightInc:3, anyContainment: 2
        map.put(map.newKey().add(EARTHLING).add(HUMAN).add("Ben Parker"), "Ben Parker"); // weightBinary:4; weightInc:3, anyContainment: 1
        map.put(map.newKey().add(GOTG).add(TREE).add("Grut"), "Grut");
        map.put(map.newKey().add(GOTG).add("Animal").add("Rocket Raccoon"), "Rocket Raccoon");
        map.put(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord"), "Peter Quill");
        map.put(map.newKey().add(AVENGERS).add(HUMAN).add("SpiderMan"), "Peter Parker");

        return map;
    }
    private static final String COMIC_CHARACTERS = "Comic Characters";
}
