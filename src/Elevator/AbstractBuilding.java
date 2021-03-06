package Elevator;

public abstract class AbstractBuilding {

	protected int numFloors;
	protected int numElevators;
	
	/**
	 * Other variables/data structures as needed goes here 
	 */


	public AbstractBuilding(int numFloors, int numElevators) {
		this.numFloors = numFloors;
		this.numElevators = numElevators;
	}

	/**
	 * Elevator.Elevator rider interface (part 2): invoked by rider threads.
 	 */

    /**
     * Signal an elevator that we want to go up
     *
     * @param fromFloor  floor from which elevator is called
     * @return           instance of the elevator to use to go up
     */
	public abstract AbstractElevator CallUp(int fromFloor, int riderID);

    /**
     * Signal an elevator that we want to go down
     *
     * @param fromFloor  floor from which elevator is called
     * @return           instance of the elevator to use to go down
     */
	public abstract AbstractElevator CallDown(int fromFloor, int riderID);

	public AbstractElevator CallUp(int fromFloor, int riderID,
			ElevatorBarrier eb) {
		// TODO Auto-generated method stub
		return null;
	}

	public AbstractElevator CallDown(int fromFloor, int riderID,
			ElevatorBarrier eb) {
		// TODO Auto-generated method stub
		return null;
	} 
    
	/* Other methods as needed goes here */
}