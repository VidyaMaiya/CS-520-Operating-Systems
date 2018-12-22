import java.util.Deque;
import java.util.LinkedList;


public class IOQueueHandler {
	public Deque<EventStructure> IOQueue = new LinkedList<EventStructure>();
	double IOServingTime;
	int[] iowaitingTime = new int[ProcessSimulator.noOfJobs];

	
	public IOQueueHandler() 
	{
		IOServingTime=60;
	}
	
	public boolean isIOQueueEmpty()
	{
		return (IOQueue.size() == 0);
	}
	
	public void IOQueuePickHadler(EventStructure event,double currentTime)
	{
		event.process.ioServedTime+=IOServingTime;
		EventStructure IOEvent = new EventStructure(event.process,EventType.IO_END, (currentTime+IOServingTime),event.schedulerType);
		EventStructure.generateEvent(IOEvent);
	}
	
}
