package hu.pazsit.keysearch;

import hu.pazsit.keysearch.CustomMultiKey;
import hu.pazsit.keysearch.KeyComparisonRelation;
import hu.pazsit.keysearch.KeyLengthRule;
import hu.pazsit.keysearch.MultiKeyMap;
import hu.pazsit.keysearch.SimpleMultiKey;
import hu.pazsit.keysearch.collection.SimpleMultiKeyMap;

import java.util.Random;
import java.util.function.Supplier;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

/**
 * SimpleMultiKeyMapTest.java
 *
 * @author Zoltan Pazsit <pazsitz@pazsitz.hu>
 * @copyright Copyright (c) 2019, Zoltan Pazsit
 */
public class SimpleMultiKeyMapBenchmark {

	private static final int ITERATIONS = 1000;
	private static final String COMIC_CHARACTERS = "Comic Characters";
    private static final String AVENGERS = "Avengers";
    private static final String GOTG = "Guardians of the Galaxy";
    private static final String EARTHLING = "earthlings";

    private static final String HUMAN = "Human";
    private static final String TREE = "Tree";
    
    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();
    
    Supplier<SimpleMultiKey<String>> lteLengthAnyMatchWithNullKeyValue = () ->
        new CustomMultiKey(
            KeyLengthRule.matchSmaller(),
            KeyComparisonRelation.equalAndAny(null),
            null
        );
    
    @Test
    public void testMapRW() {
    	MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(lteLengthAnyMatchWithNullKeyValue));
    	
    	for (int i = 0; i < ITERATIONS/10; i++) {
    		String first = new Random().nextBoolean() ? RandomStringUtils.random(3, true, false) : map.newKey().any();
	        String second = new Random().nextBoolean() ? RandomStringUtils.random(3, true, false) : map.newKey().any();
	        String third = new Random().nextBoolean() ? RandomStringUtils.random(3, true, false) : map.newKey().any();
    		String anyVal = (String) map.getIfAbsentFirst(map.newKey().add(first).add(second).add(third));
    	}
    	System.out.print(".");
    }

    @Test
    public void testMapExactMatch() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(lteLengthAnyMatchWithNullKeyValue));

        for (int i = 0; i < ITERATIONS/10; i++) {
        Assert.assertEquals("Peter Quill", map.get(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord")));
        Assert.assertEquals("Peter Quill", map.get(map.newKey().add(GOTG).add(HUMAN).add("Star-Lord")));
        //System.out.println("StarLord - Peter Quill" + map.get(map.newKey().add(AVENGERS).add(HUMAN).add("Star-Lord")));
        }
    }

    @Test
    public void testMapWithAny() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(lteLengthAnyMatchWithNullKeyValue));

        for (int i = 0; i < ITERATIONS/10; i++) {
        Assert.assertEquals("Peter Quill", map.getIfAbsentFirst(map.newKey().add(GOTG).add(HUMAN).addAny()));
        Assert.assertEquals("Grut", map.getIfAbsentFirst(map.newKey().addAny().addAny().add("Grut")));
        Assert.assertEquals("Grut", map.getIfAbsentFirst(map.newKey().addAny().add(TREE).addAny()));
        }
    }

    @Test
    public void testMapShorterKey() {
        MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(lteLengthAnyMatchWithNullKeyValue));

        for (int i = 0; i < ITERATIONS/10; i++) {
        final String gotgHuman = (String) map.getIfAbsentFirst(map.newKey().add(GOTG).add(HUMAN));
        Assert.assertEquals("Peter Quill", gotgHuman);

        final Object tree = map.getIfAbsentFirst(map.newKey().addAny().add(TREE));
        Assert.assertEquals("Grut", tree);
        }
    }
    
    @Test
    public void testMapLonger() {
    	MultiKeyMap<String, Object> map = mapValues(new SimpleMultiKeyMap<String, Object>(lteLengthAnyMatchWithNullKeyValue));
    	
    	for (int i = 0; i < ITERATIONS/10; i++) {
    	final String anyVal = (String) map.getIfAbsentFirst(map.newKey().add(EARTHLING).add(HUMAN).add("Ben Parker").add("noSuchKeyItem"));
    	Assert.assertEquals(null, anyVal);
        }
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
        
        for (int i = 0; i < ITERATIONS*100; i++) {
	        String first = new Random().nextBoolean() ? RandomStringUtils.random(3, true, false) : map.newKey().any();
	        String second = new Random().nextBoolean() ? RandomStringUtils.random(3, true, false) : map.newKey().any();
	        String third = new Random().nextBoolean() ? RandomStringUtils.random(3, true, false) : map.newKey().any();
        	map.put(map.newKey().add(first).add(second).add(third), RandomStringUtils.random(3, true, false));
        }

        return map;
    }

}
