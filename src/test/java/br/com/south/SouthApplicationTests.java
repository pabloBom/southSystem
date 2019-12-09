package br.com.south;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SouthApplicationTests {

	@Test
	void contextLoads() {
		SouthApplication.main(new String[] {});
		assertTrue(true);
	}

}
