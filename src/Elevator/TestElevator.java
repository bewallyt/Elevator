package Elevator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class TestElevator {

    public static PrintStream out;

	public static void main(String[] args) throws FileNotFoundException {
		
		/* Run elevator */
        out = new PrintStream(new FileOutputStream("Elevator.log"));
 //       System.setOut(out);
		Parser parser = new Parser();
		parser.parse("ElevatorInputFile.txt");
		
	}

}
