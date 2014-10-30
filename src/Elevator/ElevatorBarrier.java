package Elevator;

public class ElevatorBarrier extends AbstractEventBarrier {

	public int numWaiters;
	public boolean eventSignaled;
	public boolean barrierOpen;
    public int floor;
    public int key;
	
	public ElevatorBarrier(int floor, int key){
        this.floor = floor;
        this.key = key;				//key--> 0 = exit, 1 = going up, 2 = going down 
		numWaiters = 0;
		eventSignaled = false;
		barrierOpen = false;
	}
	
	@Override
	public synchronized void arrive() {
		numWaiters++;
		//System.out.println("Thread arrived, waiters: " + numWaiters);
		while(!eventSignaled){
			try {
				notifyAll();
				wait();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		//complete();
		//System.out.println("arrive return");
		notifyAll();
	}

	@Override
	public synchronized void raise() {

		eventSignaled = true;
		
		//System.out.println("Barrier raised");
		
		notifyAll();
		while(numWaiters!=0){
			try {
				wait();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		eventSignaled = false;
		
		//System.out.println("Barrier down");
		notifyAll();
		
	}

	@Override
	public synchronized void complete() {
		numWaiters--;
		//System.out.println("Thread completed, waiters: "+ numWaiters);
		while(numWaiters!=0){
			try {
				notifyAll();
				wait();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		//System.out.println("All threads completed");
		notifyAll(); //necessary? 
	}

	@Override
	public int waiters() {

		return numWaiters;
	}

    public int getDestFloor() {
        return floor;
    }
}
