import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ProcessSimulator {
static Scanner scan = new Scanner(System.in);

static boolean isCPUBusy = false;
static int noOfJobs = 10;
static int noOfRemainingProcess=10;

public static void main(String[] args)
{
	
	boolean isIOBusy = false;
	ReadyQueue cpuAlgorithm;
	System.out.println("Enter your choice of scheduling algorithm :");
	System.out.println("1. FCFS");
	System.out.println("2. SJF ");
	System.out.println("3. RR ");
	String choice = scan.next();
	if(choice.equalsIgnoreCase("FCFS"))
	{
		FIFO fifo = new FIFO();
		fifo.processCreation();
		fifo.addToEventQueue();
		cpuAlgorithm = new FCFSReadyQueueHandler();
    }
	else if(choice.equalsIgnoreCase("SJF"))
	{
		SJF sjf = new SJF();
		sjf.processCreation();
		sjf.addToEventQueue();
		cpuAlgorithm = new SJFProcessHandler();
	}
	else if(choice.equalsIgnoreCase("RR"))
	{
		RoundRobin rr = new RoundRobin();
		rr.processCreation();
		rr.addToEventQueue();
		cpuAlgorithm = new RoundRobinProcessHandler();
	}
	else
	{
		System.out.println("Incorrect choice");
		return;
	}

	IOQueueHandler io_queue = new IOQueueHandler();
	EventStructure event;
	do
	{
		event = EventStructure.eventQueue.remove();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SS");  
		Date resultdate = new Date((long) event.eventTimeStamp);
		System.out.println("------------------------------------------------------------------------------------------------------------------");
		System.out.println("The removed eventQueue event details :  Eventy Type "+event.eventType+" Process number "+event.process.processNumber+" it's Timestamp "+sdf.format(resultdate));
		System.out.println("------------------------------------------------------------------------------------------------------------------");
		switch(event.eventType)
		{
		  case CPU_ENQUE : System.out.println("Process number "+event.process.processNumber+" arrived at CPU_ENQUE");
			               if(isCPUBusy == false)
			   			   {
					   			 isCPUBusy = true;
					   			 System.out.println("Inside CPU_ENQUE when CPU is idle and ready queue is empty ");
					   			 cpuAlgorithm.CPUPickHandler(event.process, event.eventTimeStamp);
			   			   }
			   			  	else
			   			  	{
			   				  cpuAlgorithm.insert(event.process);
			   				  System.out.println("Added Process number "+event.process.processNumber+" to the ready queue");
			   				  System.out.print("Content of the ready queue is : [ ");
			   				  cpuAlgorithm.printProcesses();	
			   				  event.process.processState="ReadyQWaiting";
			   				  
			   			    }
			   			  break;
			   					
			   case CPU_END  : System.out.println("Process number "+event.process.processNumber+" arrived at CPU_END");
			   				   cpuAlgorithm.CPUEndHandler(event.process, event.eventTimeStamp);
			   					
			   					isCPUBusy = false;
			   					if(!cpuAlgorithm.isQueueEmpty())
			   					{
			   						isCPUBusy = true;
			   						ProcessControlBlock readyQProcess = cpuAlgorithm.remove();
			   						System.out.print("Processes in Ready Queue after removing the process "+readyQProcess.processNumber+" is : [ ");
			   						cpuAlgorithm.printProcesses();
			   						
			   						cpuAlgorithm.CPUPickHandler(readyQProcess, event.eventTimeStamp);
			   					}

			   				    break;
			   				    
			   case IO_ENQUE :  System.out.println("Process number "+event.process.processNumber+" arrived at IO_ENQUE");
				                if(isIOBusy == false)
			   					{
				    				isIOBusy = true;
				    				io_queue.IOQueuePickHadler(event,event.eventTimeStamp);
				    				
			   					}
			   					else
			   					{
			   						io_queue.IOQueue.add(event);
			   						
			   						System.out.println("Added process number "+event.process.processNumber+" to the IO queue");
			   						
			   						event.process.processState="IOQWaiting";
			   					}
			   				    break;
			   				    
			   case IO_END  :   System.out.println("Process number "+event.process.processNumber+" arrived at IO_END");
				 
			   					if(event.schedulerType.equalsIgnoreCase("rr")) //Reset the remainingIO time to ioMeanInterArrivalTime
			   					{
			   						event.process.timeToIO = event.process.ioMeanInterArrivalTime;
			   					}
		        				EventStructure nextreadyQEvent = new EventStructure(event.process,EventType.CPU_ENQUE,event.eventTimeStamp,event.schedulerType);
		        				EventStructure.generateEvent(nextreadyQEvent);
			   					
			                    isIOBusy = false;
			                	if(!io_queue.isIOQueueEmpty())
			        			{
			        			  isIOBusy = true;
			        			  double nextTimeStamp = event.eventTimeStamp;
			        			  EventStructure ioQEvent = io_queue.IOQueue.remove();
			        			  ioQEvent.process.processState="IOQRunning";
			        			  io_queue.IOQueuePickHadler(ioQEvent,nextTimeStamp);
			        			  
			        			}
			   				    break;
			   				    
			   default      :   System.out.println("Invalid event type");
			                    break;
			}
		}while(noOfRemainingProcess>0);
	    double endTimeofExecution = event.eventTimeStamp;
	    cpuAlgorithm.printStatistics(endTimeofExecution);
	}
}
