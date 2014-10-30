package EventBarrier;

public class EventBarrier extends AbstractEventBarrier {

	public int numWaiters;
	public boolean eventSignaled;
	public boolean barrierOpen;
	
	public EventBarrier(){
		numWaiters = 0;
		eventSignaled = false;
		barrierOpen = false;
	}
	
	@Override
	public synchronized void arrive() {
		// TODO Auto-generated method stub
		numWaiters++;
		System.out.println("Thread arrived, waiters: " + numWaiters);
		while(!eventSignaled){
			try {
				notifyAll();
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//complete();
		System.out.println("arrive return");
		notifyAll();
	}

	@Override
	public synchronized void raise() {
		// TODO Auto-generated method stub
		eventSignaled = true;
		
		System.out.println("Barrier raised");
		
		notifyAll();
		while(numWaiters!=0){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		eventSignaled = false;
		
		System.out.println("Barrier down");
		notifyAll();
		
	}

	@Override
	public synchronized void complete() {
		// TODO Auto-generated method stub
		numWaiters--;
		System.out.println("Thread completed, waiters: "+ numWaiters);
		while(numWaiters!=0){
			try {
				notifyAll();
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("All threads completed");
		notifyAll(); //necessary? 
	}

	@Override
	public int waiters() {
		// TODO Auto-generated method stub
		return numWaiters;
	}
	

}
