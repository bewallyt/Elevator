package Elevator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Elevator extends AbstractElevator implements Runnable{

    protected int numFloors;
    protected int elevatorId;
    protected int currentfloor;
    protected List<ElevatorBarrier> ebList;
    private int maxOccupancy;
    protected Queue<Rider> peopleinElevator;
    boolean[] stopfloorsUP;
    boolean[] stopfloorsDOWN;
    boolean[] stopfloorsOUT; 
    protected Building bc;
    boolean directionUp;
    boolean directionDown;
    
    /**
     * Other variables/data structures as needed goes here
     *
     * @param numFloors
     * @param elevatorId
     * @param maxOccupancyThreshold
     */
    public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold, Building bc) {
        super(numFloors, elevatorId, maxOccupancyThreshold);
        this.numFloors = numFloors;
        this.elevatorId = elevatorId;
        this.maxOccupancy = maxOccupancyThreshold;
        this.peopleinElevator = new LinkedList<Rider>();
        this.currentfloor = 1;
        this.ebList = new ArrayList<ElevatorBarrier>();
        this.bc = bc;
        stopfloorsOUT = new boolean[numFloors];
        stopfloorsUP = new boolean[numFloors];
        stopfloorsDOWN = new boolean[numFloors];
        directionUp = false;
        directionDown = false;
        
        
        
    }

    public List<ElevatorBarrier> getEbList() {
        return ebList;
    }
    
    public void addElevatorBarrier(ElevatorBarrier eb){
    	ebList.add(eb);
    }


    @Override
	public void run() {
    	while(true){	
    		if(currentfloor==numFloors){						//ADD IN THE TWO CASES WHERE THE ELEVATOR IS ON THE EXTREME FLOORS
    			directionDown = true;
    			directionUp = false;
    		}else if(currentfloor==1){
    			directionDown = false;
    			directionUp = true;
    		}
    		
    		if(stopfloorsUP[currentfloor]) {					//any calls from riders in building to go up on current floor
    			OpenDoors();
    			bc.ebListUP.get(currentfloor).raise();			//raise event barrier on that level, open doors
				ClosedDoors();
				directionUp = true;								//set direction to go up
				directionDown = false;
				stopfloorsUP[currentfloor] = false;				//!!! MUST CHANGE THIS --> IF NOT ALL RIDERS ABLE TO ENTER 
				//stopfloorsDOWN[currentfloor] = false;			//remove all call signals from current floor - NOT NECESSARILY FOR UP AND DOWN
				//stopfloorsOUT[currentfloor] = false;
    		}
    		else if (stopfloorsDOWN[currentfloor]) {			//any calls from riders in building to go down on current floor
    			OpenDoors();
    			bc.ebListDOWN.get(currentfloor).raise();		//raise event barrier on that level, open doors
				ClosedDoors();
				directionDown = true;							//set direction to go down
				directionUp = false;
				//stopfloorsUP[currentfloor] = false;				//remove all call signals from current floor
				stopfloorsDOWN[currentfloor] = false;
				//stopfloorsOUT[currentfloor] = false;
    		}
    		else if (stopfloorsOUT[currentfloor]) {				//any calls from riders in elevator to exit on that floor
    			OpenDoors();
    			bc.ebListOUT.get(currentfloor).raise();			//raise event barrier on that level, open doors 
				ClosedDoors();	
				//stopfloorsUP[currentfloor] = false;
				//stopfloorsDOWN[currentfloor] = false;
				stopfloorsOUT[currentfloor] = false;
				if(!this.peopleinElevator.isEmpty()) {			//if there are still people in elevator who have not exited
    				Rider rider = this.peopleinElevator.peek();	// check destination of next rider in queue
    				if(rider.destFloor < currentfloor) {		//set direction (going down)
    					directionUp = false;
    					directionDown = true;
    				}
    				else {										//set direction (going up)
    					directionUp = true;
    					directionDown = false;
    				}
				}
				else {											//if elevator empty, everyone has exited elevator 
					directionUp = false;						//set no direction, temporarily idle(?)
					directionDown = false;
					
					boolean remainingRequests = false;
					int requestFloor = 0;
					for(int i=0; i<bc.numFloors; i++){			//loop through all the floors --> recheck if there are any more requests
						if(stopfloorsUP[i]){					//still exists requests going up
							remainingRequests = true;
							requestFloor = i+1;					//record that floor (functional, but might want to use int[] instead since may have multiple requests
						}
						if(stopfloorsDOWN[i]){					//still exists requests going down
							remainingRequests = true;
							requestFloor = i+1;					//record that floor	
						}
					}
					if(remainingRequests) {						//if still exists requests, set direction towards that floor and go to that floor
						if(requestFloor == 0) {
							directionUp = false;				//IDLE
							directionDown = false;
						}
						else if(requestFloor > currentfloor) {
							directionUp = true;
						}
						else if(requestFloor < currentfloor) {
							directionDown = true;
						}
					}
				}
    		}//end of OUT 
    		
    		if(directionUp && !directionDown) {							//if direction set up, elevator goes up
    			VisitFloor(currentfloor+1);
    		}
    		else if(!directionUp && directionDown) {					//if direction set down, elevator does down
    			VisitFloor(currentfloor-1);
    		}
    		else {														//if idle? doesnt do anything yet 
				for(Rider r : bc.peopleinBuilding){			//check if still have people waiting on elevators in building
					if(r.startFloor<r.destFloor){			//if rider is going up
						bc.CallUp(r.startFloor, r.riderID);	
					}else if(r.startFloor>r.destFloor){
						bc.CallDown(r.startFloor, r.riderID);
					}
				}
    		
    		}
    	}
    }

    @Override
    public void OpenDoors() {
    	System.out.println("Elevator"+this.elevatorId+" on Floor"+currentfloor+" opens");    	
    }

    @Override
    public void ClosedDoors() {
    	System.out.println("Elevator"+this.elevatorId+" on Floor"+currentfloor+" closes");
    }

    @Override
    public synchronized void VisitFloor(int floor) {
        currentfloor = floor;
        System.out.println("Elevator"+this.elevatorId+" moves to Floor"+floor);
    }

    @Override
    public synchronized boolean Enter(Rider rider, int elevatorID, int floor) {
        if (peopleinElevator.size() < maxOccupancy) {
        	bc.peopleinBuilding.remove(rider);
            peopleinElevator.add(rider);				//add rider to elevator (queue)
            System.out.println("Rider"+rider.riderID+" enters Elevator"+elevatorID+" on Floor"+floor);
            return true;
        } else {
        	System.out.println("Rider"+rider.riderID+" cannot enter");
            return false;
        }
    }

    @Override
    public synchronized void Exit(Rider rider, int elevatorID, int floor) {
        peopleinElevator.remove(rider);					//remove rider to elevator (queue)
        System.out.println("*****Rider"+rider.riderID+" exits Elevator"+elevatorID+" on Floor"+floor);
    }

    @Override
    public synchronized void RequestFloor(int floor, int riderId, boolean goUp) {
    	stopfloorsOUT[floor] = true;
    	System.out.println("Rider"+riderId+" pushes Elevator"+this.elevatorId+" Button"+floor);
    }
}
