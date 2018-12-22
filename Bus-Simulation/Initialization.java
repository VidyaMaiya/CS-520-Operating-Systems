package busSimulation;

public class Initialization {
	public static int numberOfBuses=5;
	public static int numberOfBusStops=15;
	public static double busDrivingTime=300000; //5 minutes
	public static double busBoardingTime=2000; //2 seconds
	public static double passengerMeanArrivalRate=5/60000; //5people/min
	public static double passengerInterArrivalTime=12000;   //0.2min
	public static EventType person=EventType.Person;
	public static EventType arrival = EventType.Arrival;
	public static EventType boarder = EventType.Boarder;
	public static long eTime=System.currentTimeMillis();
	public static double busStopTime=(eTime+2.88e+7);// 8 hours

	/* Initialize buses to plot them at bus stops spaced in a uniform manner.
	 * Bus 1 at Bus Stop 1
	 * Bus 2 at Bus Stop 4
	 * Bus 3 at Bus Stop 7
	 * Bus 4 at Bus Stop 10
	 * Bus 5 at Bus Stop 13
	 */
	static void initializeBusArrialEvent()
	{
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("Inside initialization bus Arrival event");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
		EventStructure busArrival1 = new EventStructure(eTime+Initialization.busDrivingTime, arrival, 1, 1);
		EventStructure.generateEvent(busArrival1);
		EventStructure busArrival2 = new EventStructure(eTime+Initialization.busDrivingTime, arrival, 4, 2);
		EventStructure.generateEvent(busArrival2);
		EventStructure busArrival3 = new EventStructure(eTime+Initialization.busDrivingTime, arrival, 7, 3);
		EventStructure.generateEvent(busArrival3);
		EventStructure busArrival4 = new EventStructure(eTime+Initialization.busDrivingTime, arrival, 10, 4);
		EventStructure.generateEvent(busArrival4);
		EventStructure busArrival5 = new EventStructure(eTime+Initialization.busDrivingTime, arrival, 13, 5);
		EventStructure.generateEvent(busArrival5);
	}
	
	/* Populate person event at each bus stop
	 */
	static void initializePersonEvent() 
	{
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("Inside initialization Person event");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
		for(int i=0;i<15;i++)
		{
			EventStructure personEvent = new EventStructure(eTime, person, i+1);
			EventStructure.generateEvent(personEvent);
			BusSimulation.passengerQueue[i]++;
			System.out.println("No of Person at bus stop "+(i+1)+" is "+BusSimulation.passengerQueue[i]);
		}
			
		
	}

}
