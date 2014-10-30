package Elevator;

import java.util.*;

public class ElevatorControl {

    private Queue<Elevator> mElevatorQueue;
    private Queue<Elevator> aboveQueue;
    private Queue<Elevator> belowQueue;

    public ElevatorControl(Queue<Elevator> mElevatorQueue, Building bc) {
        this.mElevatorQueue = mElevatorQueue;
        this.aboveQueue = new LinkedList<Elevator>();
        this.belowQueue = new LinkedList<Elevator>();
    }


    public Elevator returnBestElevator(int fromFloor, boolean goUp) {

        if (mElevatorQueue.size()==1) {
            return mElevatorQueue.peek();
        } else {
            Elevator elevator = mElevatorQueue.poll();
            if (elevator.peopleinElevator == null || elevator.peopleinElevator.size() == 0) {
                mElevatorQueue.add(elevator);
                return elevator;
            } else if (elevator.peopleinElevator.size() == elevator.maxOccupancyThreshold) {
                mElevatorQueue.add(elevator);
                returnBestElevator(fromFloor, goUp);
            } else {
                pruneElevators(fromFloor);
                if (goUp) {
                    upElevator(aboveQueue);
                } else {
                    downElevator(belowQueue);
                }
            }
        }
        return null;
    }

    private void pruneElevators(int fromFloor) {
        for (Elevator e : mElevatorQueue) {
            if (e.currentfloor >= fromFloor) {
                aboveQueue.add(e);
            } else {
                belowQueue.add(e);
            }
        }
    }

    private Elevator upElevator(Queue<Elevator> elevatorQueue) {
        boolean elevatorExists = false;
        Elevator headElevator = elevatorQueue.poll();
        for (Elevator e : elevatorQueue) {
            if (headElevator.currentfloor <= e.currentfloor) {
                elevatorExists = true;
            }
        }
        if (elevatorExists) {
            return headElevator;
        } else {
            elevatorQueue.add(headElevator);
            upElevator(elevatorQueue);
        }
        return null;
    }

    private Elevator downElevator(Queue<Elevator> elevatorQueue) {
        boolean elevatorExists = false;
        Elevator headElevator = elevatorQueue.poll();
        for (Elevator e : elevatorQueue) {
            if (headElevator.currentfloor >= e.currentfloor) {
                elevatorExists = true;
            }
        }
        if (elevatorExists) {
            return headElevator;
        } else {
            elevatorQueue.add(headElevator);
            downElevator(elevatorQueue);
        }
        return null;
    }

}


