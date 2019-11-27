# multilevel-keysearch
provides storage and search capabilities on values with multi level hierarchy of keys

# Features:
	- Unlimited level of keys "chained"
	- using pre-specified custom wildcard with keys on add or on search-get
	- able to fine tune search by
		- key length
		- key comparison 
		- result ordering options (weight, any containment, or custom rules...)
  
```java
  SimpleMultiKeyMap map = new SimpleMultiKeyMap();
  map.put(map.newKey().add(AVENGERS).add(HUMAN).add("SpiderMan"), "Peter Parker");
  map.put(map.newKey().add(GOTG).add("Animal").add("Rocket Raccoon"), "Rocket Raccoon");
  map.put(map.newKey().add(AVENGERS).add("Animal").add("Rocket Raccoon"), "Rocket Raccoon");
  String peterParker = map.get(map.newKey().addAny().add(HUMAN).addAny());
  String rocketRackoon = map.get(map.newKey().add(GOTG).addAny().addAny());
  String rocketRackoon = map.get(map.newKey().addAny().addAny().add("Rocket Raccoon"));
```  
  
## Jar package available from:

http://pazsitz.hu/repo/hu/pazsitz/keysearch/1.1-SNAPSHOT/keysearch.1.1-SNAPSHOT.source.jar
