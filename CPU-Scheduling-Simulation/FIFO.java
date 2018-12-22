import java.util.Random;

public class FIFO {
	 static long eTime=System.currentTimeMillis();
	 static EventType cpu_enque = EventType.CPU_ENQUE;
	 static EventType cpu_end = EventType.CPU_END;
	 static EventType io_enque = EventType.IO_ENQUE;
	 static EventType io_end = EventType.IO_END;

	FIFO()
	{
		
	}
	static ProcessControlBlock process1,process2,process3,process4,process5,process6,process7,process8,process9,process10;
	
	public void processCreation()
	{
		Random ran = new Random();
		System.out.println("Initialize process burst time between 2 to 4 minutes");
		//Generate random numbers between 2 and 4 and convert minutes to milliseconds
		 process1 = new ProcessControlBlock(1, "start", ((ran.nextDouble()*(4-2))+2)*60000, 30, 0,eTime);
		 System.out.println("Process1 process burst time (in ms): "+process1.processBurstTime);
		 
		 process2 = new ProcessControlBlock(2, "start",((ran.nextDouble()*(4-2))+2)*60000, 35, 0,eTime); 
		 System.out.println("Process2 process burst time (in ms): "+process2.processBurstTime);
		 
		 process3 = new ProcessControlBlock(3, "start",((ran.nextDouble()*(4-2))+2)*60000, 40, 0,eTime); 
		 System.out.println("Process3 process burst time (in ms): "+process3.processBurstTime);
		 
		 process4 = new ProcessControlBlock(4, "start",((ran.nextDouble()*(4-2))+2)*60000, 45, 0,eTime); 
		 System.out.println("Process4 process burst time (in ms): "+process4.processBurstTime);
		 
		 process5 = new ProcessControlBlock(5, "start",((ran.nextDouble()*(4-2))+2)*60000, 50, 0,eTime); 
		 System.out.println("Process5 process burst time (in ms): "+process5.processBurstTime);
		 
		 process6 = new ProcessControlBlock(6, "start",((ran.nextDouble()*(4-2))+2)*60000, 55, 0,eTime); 
		 System.out.println("Process6 process burst time (in ms): "+process6.processBurstTime);
		 
		 process7 = new ProcessControlBlock(7, "start",((ran.nextDouble()*(4-2))+2)*60000, 60, 0,eTime); 
		 System.out.println("Process7 process burst time (in ms): "+process7.processBurstTime);
		 
		 process8 = new ProcessControlBlock(8, "start",((ran.nextDouble()*(4-2))+2)*60000, 65, 0,eTime); 
		 System.out.println("Process8 process burst time (in ms): "+process8.processBurstTime);
		 
		process9 = new ProcessControlBlock(9, "start", ((ran.nextDouble()*(4-2))+2)*60000, 70, 0,eTime);
		System.out.println("Process9 process burst time (in ms): "+process9.processBurstTime);
		
		process10 = new ProcessControlBlock(10, "start", ((ran.nextDouble()*(4-2))+2)*60000,75, 0,eTime);
		System.out.println("Process10 process burst time (in ms): "+process10.processBurstTime);
	}
	
	public void addToEventQueue()
	{
		EventStructure event1 = new EventStructure(process1, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event1);
	
		EventStructure event2 = new EventStructure(process2, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event2);
		
		EventStructure event3 = new EventStructure(process3, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event3);

		EventStructure event4 = new EventStructure(process4, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event4);
		
		EventStructure event5 = new EventStructure(process5, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event5);
		
		EventStructure event6 = new EventStructure(process6, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event6);
		
		EventStructure event7 = new EventStructure(process7, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event7);
		
		EventStructure event8 = new EventStructure(process8, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event8);
		
		EventStructure event9 = new EventStructure(process9, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event9);
		
		EventStructure event10 = new EventStructure(process10, cpu_enque, eTime,"fcfs");
		EventStructure.generateEvent(event10);
	}
}
