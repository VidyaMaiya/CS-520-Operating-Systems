package busSimulation;

import java.text.SimpleDateFormat;

import java.util.Date;

public class BusSimulation{

	public static int[] passengerQueue = new int[15];
	
	public static void PersonEvent (EventStructure event,double eventTime)
	{
		 System.out.println("Inside bus simulation person event");

		  //busStopNumber starts from 1 to 15 whereas index of passengerQueue[] starts from 0 - 14. 
		  passengerQueue[(event.busStopNumber-1)]++;
	      System.out.println("The number of passengers at each bus stop : ");
		   for(int i=0;i<15;i++)
			  {
				  System.out.print("passengerQueue["+(i+1)+"]="+passengerQueue[i]+"\t");
			  }
		  System.out.println();
		  System.out.println("No of Passengers at busStop number "+event.busStopNumber+" is "+passengerQueue[(event.busStopNumber-1)]);
		  /*
		   * Generate the next person event at the same stop at event time + (mean_inter-arrival_rate) * random()
		   */
		  double nextEventTime=eventTime+Initialization.passengerInterArrivalTime * (Math.random()* 10+1); //Generate random number between 1 and 10
		  EventStructure newPerson = new EventStructure(nextEventTime, EventType.Person, event.busStopNumber);
		  EventStructure.generateEvent(newPerson);
		  
	}
	
	public static void ArrivalEvent(EventStructure event,double eventTime)
	{
		System.out.println("Insdie bus simulation arrival event");
		System.out.println("No of People standing at bus stop number "+event.busStopNumber+" = "+passengerQueue[(event.busStopNumber-1)]);
		/*
		 * Check if passenger queue is empty. If it is empty then bus moves to the next bus stop. 
		 * If the current bus stop is 15, then next bus stop would be 1
		 */
        if(passengerQueue[(event.busStopNumber-1)] == 0)
        {
        	/*
        	 * Generate the arrival event at the next bus stop at event time + bus_drive_time
        	 */
         EventStructure nextArrivalEvent = new EventStructure(eventTime+Initialization.busDrivingTime, EventType.Arrival, (event.busStopNumber+1 > 15? 1:event.busStopNumber+1), event.busNumber);
         EventStructure.generateEvent(nextArrivalEvent);
        }
        else
        {
        	/*
        	 * Generate the boarding event at the bus stop at event time.
        	 */
       	 EventStructure nextBoarderEvent = new EventStructure(eventTime, EventType.Boarder, event.busStopNumber, event.busNumber);
       	 EventStructure.generateEvent(nextBoarderEvent);
        }
	}

	public static void BoarderEvent(EventStructure event,double eventTime) 
	{
		System.out.println("Inside bus simulation boarder event");
		//Boarding takes place as long as there exists atleast one person in the passenger waiting queue
			if(passengerQueue[(event.busStopNumber-1)] > 0)
			{
				passengerQueue[(event.busStopNumber-1)]--;
			}
			System.out.println("No of People remaining at bus stop number "+event.busStopNumber+" = "+passengerQueue[(event.busStopNumber-1)]);
			/*
			 * The last passenger has boarded the bus. No more passenger is waiting in the queue
			 * Check if passenger queue is empty. If it is empty then bus moves to the next bus stop. 
			 * If the current bus stop is 15, then next bus stop would be 1
			 */
	        if(passengerQueue[(event.busStopNumber-1)] == 0)
	        {
	        	/*
	        	 * Generate the arrival event at the next bus stop at event time + bus_drive_time
	        	 */
	        	EventStructure nextArrivalEvent = new EventStructure(eventTime+Initialization.busDrivingTime,EventType.Arrival, (event.busStopNumber+1>15? 1:event.busStopNumber+1), event.busNumber);
	        	EventStructure.generateEvent(nextArrivalEvent);
	        }
	        else
	        {
	        	/*
	        	 * Generate the boarder event at event time + bus_boarding_time
	        	 */
	        	EventStructure nextBoardEvent = new EventStructure(eventTime+Initialization.busBoardingTime, EventType.Boarder, event.busStopNumber, event.busNumber);
	        	EventStructure.generateEvent(nextBoardEvent);
	        }
    
	}
	
	public static void main(String[] args) 
	{
		for(int i=0;i<passengerQueue.length;i++)
		{
			passengerQueue[i]=0;
		}
		Initialization.initializePersonEvent();
		Initialization.initializeBusArrialEvent();
		double eventTime=0;
		do
		{
				EventStructure event=EventStructure.eventQueue.remove();
				eventTime=event.timeStamp;
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");  
				Date resultdate = new Date((long) event.timeStamp);
				
				System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
				if(event.eventType != EventType.Person)
					System.out.println("event strucutre details: Event Type is "+event.eventType+", Event time stamp "+sdf.format(resultdate)+", Bus stop number "+event.busStopNumber+", Bus Number "+event.busNumber);
				else
					System.out.println("event strucutre details: Event Type is "+event.eventType+", Event time stamp "+sdf.format(resultdate)+", Bus stop number "+event.busStopNumber);
				
				switch(event.eventType)
				{
					case Person: PersonEvent(event,eventTime); 
			        			  break;
				    case Arrival: ArrivalEvent(event, eventTime);
					             break;
				    case Boarder : BoarderEvent(event,eventTime);
					               break;
				    default: System.out.println("Event type is invalid/null");
					              break;
			   }
		}while(eventTime<=Initialization.busStopTime);
		
	}
;}
