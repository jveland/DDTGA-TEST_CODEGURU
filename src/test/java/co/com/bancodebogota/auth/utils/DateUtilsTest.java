package co.com.bancodebogota.auth.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application-dev.properties")
public class DateUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFormat() {
		String dateExpected = "201901011200";
		String dateNotExpected = "201901011201";
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,12);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.YEAR, 2019);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 01);
		Date d = cal.getTime();
		
		String dateRecieved = DateUtils.dateFormat(d);
		
		assertEquals(dateExpected, dateRecieved);
	}

}
