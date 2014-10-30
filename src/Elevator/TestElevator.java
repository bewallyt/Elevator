package Elevator;

import java.io.FileNotFoundException;

public class TestElevator {

	public static void test(String filename) throws FileNotFoundException {
		
		/* Run elevator */
		Parser parser = new Parser();
		parser.parse(filename);
		
	}

}
