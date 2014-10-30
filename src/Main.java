import java.io.FileNotFoundException;

import Elevator.TestElevator;
import EventBarrier.TestEventBarrier;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		if(args[0].equals("p1")){
			/* Runs EventBarrier Test */
			TestEventBarrier.test();
		}
		else{
			/* Runs Elevator Test where argument is test case */
			TestElevator.test(args[0]);
		}
	}
}
