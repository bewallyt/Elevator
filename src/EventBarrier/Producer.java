package EventBarrier;

public class Producer implements Runnable{

	public EventBarrier eventBarrier;
	
	public Producer(EventBarrier eb){
		eventBarrier = eb;		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//while(true){
			eventBarrier.raise();
			//System.out.println("EventBarrier.Producer has raised...");
		//}
		
	}

	
	
}
