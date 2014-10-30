package Elevator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Parser {

	private int numFloors;
	private int numElevators;
	private int numRiders;
	private int maxCapacity;

    public static ElevatorControl ec;
    public static Building bc;
   

    protected ArrayList<ElevatorBarrier> elevatorBarrierListUP = new ArrayList<ElevatorBarrier>();
    protected ArrayList<ElevatorBarrier> elevatorBarrierListDOWN = new ArrayList<ElevatorBarrier>();
    protected ArrayList<ElevatorBarrier> elevatorBarrierListOUT = new ArrayList<ElevatorBarrier>();

    protected Queue<Elevator> mElevatorQueue;
    protected ArrayList<Elevator> mElevatorList;
	protected ArrayList<Rider> riderList;
	private Scanner input;
	private Scanner lineScanner;

	
	public Parser(){
		riderList = new ArrayList<Rider>();
        mElevatorQueue = new LinkedList<Elevator>();
        mElevatorList = new ArrayList<Elevator>();

	}

    public ArrayList<Rider> getRiderList() {
        return riderList;
    }
	
	public void parse(String filename) throws FileNotFoundException{

		input = new Scanner(new FileReader(filename));
		
		//Parse F E R N
		numFloors = input.nextInt();
		numElevators = input.nextInt();
		numRiders = input.nextInt();
		maxCapacity = input.nextInt();


		//Create 3F Elevator Barriers and put into elevator barrier list
		
		for(int i =0; i <numFloors; i++){
			ElevatorBarrier tempEB1 = new ElevatorBarrier(i, 0);
			elevatorBarrierListOUT.add(tempEB1);
			
			ElevatorBarrier tempEB2 = new ElevatorBarrier(i, 1);
			elevatorBarrierListUP.add(tempEB2);
			
			ElevatorBarrier tempEB3 = new ElevatorBarrier(i, 2);
			elevatorBarrierListDOWN.add(tempEB3);
			
		}
		
		//Initialize elevator control and Building
        
        bc = new Building(numFloors, numElevators, elevatorBarrierListOUT, elevatorBarrierListUP, elevatorBarrierListDOWN);

        testParser();
        
		/* Create rider threads
		 * Benson: Replaced number of riders with number of requests.
		 * There can be fewer request than the number of riders */
		for(int i = 0; i < findNumLines(filename); i++){
			
			int riderID = input.nextInt();
			int startFloor = input.nextInt();
			int destFloor = input.nextInt();
			
			//ElevatorBarrier eb = new ElevatorBarrier(startFloor, destFloor);
			
			Rider r = new Rider(bc, riderID, startFloor, destFloor);
			riderList.add(r);
			//Thread t = new Thread(r);
			//t.start();
		}
		
//		testParser();

		for(int i = 0; i < numElevators; i++){
			Elevator elevator = new Elevator(numFloors, i+1, maxCapacity, bc);
            mElevatorQueue.add(elevator);
            mElevatorList.add(elevator);
        }
		
		ec = new ElevatorControl(mElevatorQueue, bc);
		
		for (Rider r:riderList){
			Thread t = new Thread(r);
			t.start();
		}

//        for (Elevator e : mElevatorQueue) {
//            Thread t = new Thread(e);
//            t.start();
//        }

        for (Elevator e: mElevatorList) {
            Thread t = new Thread(e);
            t.start();
        }


	}
	
	
	private int findNumLines(String filename) throws FileNotFoundException{
		lineScanner = new Scanner(new FileReader(filename));
		/* Benson: lineCounter starts at -1 because we aren't considering first line in text file */
		int lineCounter = -1;
		while(lineScanner.hasNextLine()){
			lineCounter++;
			lineScanner.nextLine();
		}
		return lineCounter;
	}

    public void testParser(){
        System.out.println("Number of Floors: " + numFloors);
        System.out.println("Number of Elevators: " + numElevators);
        System.out.println("Number of Riders: " + numRiders);
        System.out.println("Max Capacity per Elevator: " + maxCapacity);
        for(Rider r : riderList){
            System.out.println("Rider " + r.riderID + ": " + "Starts from floor " + r.startFloor + " And ends at floor " + r.destFloor);
        }
        System.out.println();
    }
	
	
}
