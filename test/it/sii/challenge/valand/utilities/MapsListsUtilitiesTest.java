package it.sii.challenge.valand.utilities;

import static org.junit.Assert.assertTrue;
import it.sii.challenge.valand.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MapsListsUtilitiesTest {

//	private Map<String, Integer> map1;
//	private Map<String, Integer> map2;
//	private MapsListsUtilities utilities;
//	private int map1length;
//	private int map2length;
//	private int numberOfEqualTerms;
//	
//	
//	@Before
//	public void setUp() throws Exception {
//		map1length = 20;
//		map2length = 20;
//		numberOfEqualTerms = 5;
//		map1 = new HashMap<String, Integer>();
//		map2 = new HashMap<String, Integer>();
//		utilities = new MapsListsUtilities();
//		
//		for(int i=0; i<map1length-numberOfEqualTerms; i++){
//			map1.put(utilities.randomStringGenerator(6), (int)(Math.random()*10));
//		}
//		for(int i=0; i<map2length-numberOfEqualTerms; i++){
//			map2.put(utilities.randomStringGenerator(6), (int)(Math.random()*10));
//		}
//		for(int i=0; i<numberOfEqualTerms; i++){
//			String equalkey = utilities.randomStringGenerator(7); 
//			map1.put(equalkey, (int)(Math.random()*10));
//			map2.put(equalkey, (int)(Math.random()*10));
//		}
//		
//	}
//
//	@Test
//	public void testUnionListOfKeySets() {
//		Set<String> setTest = utilities.unionListOfKeySets(map1, map2);
//		assertTrue(setTest.size() == map1length+map2length-numberOfEqualTerms);
//		for (String s : setTest)
//			if(s.length() == 7)
//			System.out.println(s);
//	}
//	
//	@Test
//	public void testCopyMap(){
//		User user = new User("abc", 3, 3);
//		
//		Map<String, User> mappa = new HashMap<String, User>();
//		
//		
//		mappa.put(user.getId(), user);
//		
//		user.setId("idmodificato");
//		
//		System.out.println("IDMODIFICATO: " + user.getId() + " IDNellaMappa: " + mappa.get("abc").getId());
//		
//	}
//	

}
