package Elevator;

import java.util.*;

public class ElevatorControl {

    private Queue<Elevator> mElevatorQueue;
    
    public ElevatorControl(Queue<Elevator> mElevatorQueue, Building bc) {
        this.mElevatorQueue = mElevatorQueue;
    }


    public Elevator returnBestElevator(int fromFloor, boolean goUp) {

        Elevator elevator = mElevatorQueue.poll();
        if (elevator.peopleinElevator.size() == 0) {
            mElevatorQueue.add(elevator);
            return elevator;
        } else if (elevator.peopleinElevator.size() == elevator.maxOccupancyThreshold) {
            mElevatorQueue.add(elevator);
            returnBestElevator(fromFloor, goUp);
        } else {
            if (goUp) {
                spaceAvailableElevatorsUp(elevator, mElevatorQueue, fromFloor);
            } else {
                spaceAvailableElevatorsDown(elevator, mElevatorQueue, fromFloor);
            }
        }

        return null;
    }

    private Elevator spaceAvailableElevatorsUp(Elevator elevator, Queue<Elevator> elevatorQueue, int fromFloor) {
        boolean elevatorExists = false;

        if (elevator.currentfloor <= fromFloor && elevator.currentfloor > elevatorQueue.peek().currentfloor) {
              elevatorExists = true;
        }
        elevatorQueue.add(elevator);
        if (elevatorExists) {
            return elevator;
        } else {
            spaceAvailableElevatorsDown(elevatorQueue.poll(), elevatorQueue, fromFloor);
        }
       return null;
    }

    private Elevator spaceAvailableElevatorsDown(Elevator elevator, Queue<Elevator> elevatorQueue, int fromFloor) {
        boolean elevatorExists = false;

        if (elevator.currentfloor >= fromFloor && elevator.currentfloor < elevatorQueue.peek().currentfloor) {
            elevatorExists = true;
        }
        elevatorQueue.add(elevator);
        if (elevatorExists) {
            return elevator;
        } else {
            spaceAvailableElevatorsUp(elevatorQueue.poll(), elevatorQueue, fromFloor);
        }
        return null;

    }
}


