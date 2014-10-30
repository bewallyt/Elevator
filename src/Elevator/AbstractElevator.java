package Elevator;

public abstract class AbstractElevator {

	protected int numFloors; 
	protected int elevatorId;
	protected int maxOccupancyThreshold;

	/**
	 * Other variables/data structures as needed goes here 
	 */

	public AbstractElevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		this.numFloors = numFloors;
		this.elevatorId = elevatorId;
		this.maxOccupancyThreshold = maxOccupancyThreshold;
	}

	/**
	 * Elevator.Elevator control inferface: invoked by Elevator.Elevator thread.
 	 */

	/* Signal incoming and outgoing riders */
	public abstract void OpenDoors(); 	

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
 	 */
	public abstract void ClosedDoors();

	/* Go to a requested floor */
	public abstract void VisitFloor(int floor);


	/**
	 * Elevator.Elevator rider interface (part 1): invoked by rider threads.
  	 */

	/* Enter the elevator */
	public boolean Enter(Rider rider, int elevatorID, int floor) {
		// TODO Auto-generated method stub
		return false;
	}

	/* Exit the elevator */
	public void Exit(Rider rider, int elevatorID, int floor) {
		// TODO Auto-generated method stub
		
	}

	/* Request a destination floor once you enter */
 	public abstract void RequestFloor(int floor, int riderId, boolean goUp);
}