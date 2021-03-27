package scixtracerj;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSxDate {

	protected int value1 = 3; 
	protected int value2 = 3;
	
	@Test
	public void testAdd() {
		
		try {
			SxDate date = new SxDate("2021-03-27");
			boolean t1 = false;
			if ( date.get_day() == 27) {
				t1 = true;
			}
			boolean t2 = false;
			if ( date.get_month() == 3) {
				t2 = true;
			}
			boolean t3 = false;
			if ( date.get_year() == 2021) {
				t3 = true;
			}
			assertTrue(t1 && t2 && t3);
			
		} catch (Exception e) {
			fail("Cannot create SxDate");
			e.printStackTrace();
		}
		
	    //double result = value1 + value2;
	    //System.out.println(result);
	    //assertTrue(result == 6);
	}

}
