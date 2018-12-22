import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

enum EventType
{
	CPU_ENQUE,CPU_END,IO_ENQUE,IO_END;
}

class TimeStampComparator implements Comparator<EventStructure>
{
	public int compare(EventStructure e1, EventStructure e2) 
	{
		if(e1.eventTimeStamp<e2.eventTimeStamp) 
			return -1;
		else if(e1.eventTimeStamp>e2.eventTimeStamp) 
			return 1;
		else 
		{
			if(e1.updateTimeStamp < e2.updateTimeStamp)
				return -1;
			else
				return 1;
		}
	}
}

public class EventStructure {
	ProcessControlBlock process;
	EventType eventType;
	double eventTimeStamp;
	double updateTimeStamp;
	public static int eventCounter=0;
	String schedulerType;
	
	public static PriorityQueue<EventStructure> eventQueue =new PriorityQueue<EventStructure>(100000000,new TimeStampComparator());
		
	EventStructure(ProcessControlBlock process,EventType eventType,double eventTimeStamp,String schedulerType)
	{
		this.process = process;
		this.eventType = eventType;
		this.eventTimeStamp = eventTimeStamp;
		this.schedulerType=schedulerType;
		this.updateTimeStamp = eventCounter++;
		
	}
	
	
    public static void generateEvent(EventStructure event) 
    {
	   //adding events to the priority queue
	    eventQueue.add(event);
	    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SS");  
		Date resultdate = new Date((long) event.eventTimeStamp);
		System.out.println("The next "+event.eventType+" event occurs for the process number "+event.process.processNumber+" at timestamp "+sdf.format(resultdate));
	}

}
