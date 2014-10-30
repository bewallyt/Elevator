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
		
		if(isGoingUp) {													//rider going up
			eb = bc.ebListUP.get(startFloor);							//get event barrier on the rider's start floor
			elevator = (Elevator) bc.CallUp(startFloor, riderID);		//CALLUP and return elevator from scheduling algorithm
			bc.peopleinBuilding.add(this);
		}
		else {															//rider going down
			eb = bc.ebListDOWN.get(startFloor);							//get event barrier on the rider's start floor
			elevator = (Elevator) bc.CallDown(startFloor, riderID);		//CALLDOWN and return elevator from scheduling algorithm
			bc.peopleinBuilding.add(this);
		}
		
		if(elevator.currentfloor < startFloor) {						//set elevator's current direction UP	
			elevator.directionUp = true;
			elevator.directionDown = false;
		}
		else if (elevator.currentfloor > startFloor) {					//set elevator's current direction DOWN
			elevator.directionUp = false;
			elevator.directionDown = true; 
		}
		
		eb.arrive();													//ARRIVE and wait at barrier
		if (elevator.Enter(this, elevator.elevatorId, startFloor)) {	//if elevator not full, rider will ENTER
			eb.complete();												
			elevator.RequestFloor(destFloor, riderID, isGoingUp);
			eb = bc.ebListOUT.get(destFloor);
			eb.arrive();
			elevator.Exit(this, elevator.elevatorId, destFloor);
			eb.complete();
		}else{																//rider cannot enter 
				//MUST CHANGE THIS: WHAT HAPPENS IF FALSE, RIDER CANNOT ENTER?
				//rider not removed from queue, still in peopleinBuilding 
			eb.complete();
			System.out.println("Rider"+riderID+" cannot enter");
		}
	}

}
