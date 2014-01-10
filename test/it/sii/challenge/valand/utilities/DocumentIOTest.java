package it.sii.challenge.valand.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import it.sii.challenge.valand.model.Checkin;
import it.sii.challenge.valand.model.Review;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DocumentIOTest {
	
	String businessTestPath;
	String checkingTestPath;
	String userTestPath;
	String reviewTestPath;
	
	DocumentIO io;
	
	@Before
	public void initTest(){
		String path = "dataset/";
		this.businessTestPath = path + "business_test.json";
		this.checkingTestPath = path + "checkin_test.json";
		this.userTestPath = path + "user_test.json";
		this.reviewTestPath = path + "review_test.json";
		
		this.io = new DocumentIO(this.businessTestPath, this.checkingTestPath, 
				this.reviewTestPath, this.userTestPath);
	}

	@Test
	public void testDocumentIO() {
		assertNotNull(this.io);
	}

	@Test
	public void testGetBusinessFromFile() {
		fail();
	}

	@Test
	public void testGetUsersFromFile() {
		fail();
	}

	@Test
	public void testGetCheckinFromFile() {
		List<Checkin> checkinList = this.io.getCheckinFromFile();				
		assertEquals(checkinList.size(), 4);
	}

	@Test
	public void testGetReviewFromFile() {
		List<Review> reviewList = this.io.getReviewFromFile();				
		assertEquals(reviewList.size(), 4);
	}

	
	
}
