package busSimulation;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;


enum EventType
{
	Person, Arrival, Boarder
}

class TimeStampComparator implements Comparator<EventStructure>
{
	public int compare(EventStructure e1, EventStructure e2) 
	{
		if(e1.timeStamp<e2.timeStamp) 
			return -1;
		else if(e1.timeStamp>e2.timeStamp) 
			return 1;
		else return 0;
	}
}

public class EventStructure  
{
	double timeStamp;
	EventType eventType;
	int busStopNumber;
	int busNumber;
	/* Creating Priority queue to store all created events - Person/Arrival/Boarder in such a way that
	   events with smaller timestamp will get higher priority and will be executed first. This is achieved
	   by using comparator
	*/
	public static PriorityQueue<EventStructure> eventQueue =new PriorityQueue<EventStructure>(100000000,new TimeStampComparator());
	
	public EventStructure(double timeStamp,EventType eventType,int busStopNumber,int busNumber)
	{
		this.timeStamp=timeStamp;
		this.eventType=eventType;
		this.busStopNumber=busStopNumber;
		this.busNumber=busNumber;
		
	}
	public EventStructure(double timeStamp,EventType eventType,int busStopNumber)
	{
		this.timeStamp=timeStamp;
		this.eventType=eventType;
		this.busStopNumber=busStopNumber;
	}

   public static void generateEvent(EventStructure event) 
	{
	   //adding events to the priority queue
		eventQueue.add(event);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");  
		Date resultdate = new Date((long) event.timeStamp);

		if(event.eventType!= EventType.Person)
		{
			System.out.println("The next "+event.eventType+" event occurs for bus number "+event.busNumber+" at bus stop number "+event.busStopNumber+" at timestamp "+sdf.format(resultdate));
		}
		else
		{
			System.out.println("The next "+event.eventType+" event occurs at bus stop number "+event.busStopNumber+" at timestamp "+sdf.format(resultdate));
		}

	}

}
