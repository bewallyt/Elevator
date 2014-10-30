package Elevator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Building extends AbstractBuilding {
	
	protected int numFloors;
	private int numElevators;
    protected HashMap<Integer,ArrayList<Integer>> floorRequestMap;
	
	ArrayList<ElevatorBarrier> ebListOUT = new ArrayList<ElevatorBarrier>();
	ArrayList<ElevatorBarrier> ebListUP = new ArrayList<ElevatorBarrier>();
	ArrayList<ElevatorBarrier> ebListDOWN = new ArrayList<ElevatorBarrier>();
	
	private Set<ElevatorBarrier> upBarriers = new HashSet<ElevatorBarrier>();
	private Set<ElevatorBarrier> downBarriers = new HashSet<ElevatorBarrier>();

    public Building(int numFloors, int numElevators, ArrayList<ElevatorBarrier> ebListOUT , ArrayList<ElevatorBarrier> ebListUP , ArrayList<ElevatorBarrier> ebListDOWN) {
        super(numFloors, numElevators);
        this.numFloors = numFloors;
        this.numElevators = numElevators;
        this.ebListOUT = ebListOUT;
        this.ebListUP = ebListUP;
        this.ebListDOWN = ebListDOWN;
        this.floorRequestMap = new HashMap<Integer, ArrayList<Integer>>();
        makeFloorArrays(numFloors);
    }

    private void makeFloorArrays(int numFloors) {
        for (int i = 0; i < numFloors; i++) {
            ArrayList<Integer> requestListOfFloor = new ArrayList<Integer>();
            floorRequestMap.put(i + 1, requestListOfFloor);
        }
    }

    /** Called by Rider Threads, returns the elevator that can efficiently serve rider. */
	@Override
	public synchronized AbstractElevator CallUp(int fromFloor, int riderID, ElevatorBarrier eb) {
		System.out.println("Rider"+riderID+" pushes UP "+fromFloor);

		Elevator tempElevator = Parser.ec.returnBestElevator(fromFloor, true);
		tempElevator.stopfloorsUP[fromFloor] = true;
		return tempElevator;
    }

	@Override
	public synchronized AbstractElevator CallDown(int fromFloor, int riderID, ElevatorBarrier eb) {
		System.out.println("Rider"+riderID+" pushes DOWN "+fromFloor);

		Elevator tempElevator = Parser.ec.returnBestElevator(fromFloor, false);
		tempElevator.stopfloorsDOWN[fromFloor] = true;
        return tempElevator;
    }

    public int getNumElevators() {
		return numElevators;
	}
    
    public void removeDownBarrier(ElevatorBarrier eb){
    	downBarriers.remove(eb);
    }
    
    public void removeUpBarrier(ElevatorBarrier eb){
    	upBarriers.remove(eb);
    }

	@Override
	public AbstractElevator CallUp(int fromFloor, int riderID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractElevator CallDown(int fromFloor, int riderID) {
		// TODO Auto-generated method stub
		return null;
	}

}
