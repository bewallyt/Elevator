package Elevator;

public class Rider implements Runnable{

	protected int riderID;
	protected int startFloor;
	protected int destFloor;
	protected Building bc;
	protected Elevator elevator;
	protected ElevatorBarrier eb;
	
	public Rider(Building bc, int id, int start, int dest){
		this.bc = bc;
		this.riderID = id;
		this.startFloor = start;
		this.destFloor = dest;
	}
	
	@Override
	public void run() {
		boolean isGoingUp = startFloor < destFloor;
		
		if(isGoingUp) {
			eb = bc.ebListUP.get(startFloor);							//get event barrier on the rider's start floor
			elevator = (Elevator) bc.CallUp(startFloor, riderID, eb);	//CALLUP and return elevator from scheduling algorithm
		}
		else {
			eb = bc.ebListDOWN.get(startFloor);
			elevator = (Elevator) bc.CallDown(startFloor, riderID, eb);	//CALLDOWN and return elevator from scheduling algorithm
		}
		
		if(elevator.currentfloor < startFloor) {
			elevator.directionUp = true;
			elevator.directionDown = false;
		}
		else if (elevator.currentfloor > startFloor) {
			elevator.directionUp = false;
			elevator.directionDown = true;
		}
		
		eb.arrive();												//ARRIVE and wait at barrier
		if (elevator.Enter(this, elevator.elevatorId, startFloor)) {	//if elevator not full and possible to ENTER
			eb.complete();
			elevator.RequestFloor(destFloor, riderID, isGoingUp);
			eb = bc.ebListOUT.get(destFloor);
			eb.arrive();
			elevator.Exit(this, elevator.elevatorId, destFloor);
			eb.complete();
		}	
		
	}

}
