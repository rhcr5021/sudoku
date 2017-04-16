package simualted_anealing;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class driverTest {

	@Test
	public void test() throws IOException {
		
		String[] diff = new String[4];
		diff[0] = "simple";
		diff[1] = "easy";
		diff[2] = "intermediate";
		diff[3] = "expert";
		int num_threads = 10;
		for (int i = 0; i < 4; i++)
		{
			for (int puz = 1; puz <= 20; puz++)
			{
				driver d = new driver(diff[i],puz,num_threads);
				assert(d.concurrent());
			}
		}
		
		//fail("Not yet implemented");
	}

}
