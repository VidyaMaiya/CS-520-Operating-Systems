import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class FCFSReadyQueueHandler extends ReadyQueue {
	public Deque<ProcessControlBlock> FCFSReadyQueue = new LinkedList<ProcessControlBlock>();
	public double CPUUtlizationTime=0;

	FCFSReadyQueueHandler()
	{

	}
	/* 
	 * @see ReadyQueue#isQueueEmpty()
	 */
	public boolean isQueueEmpty()
	{
		if(FCFSReadyQueue.size() == 0)
			return true;
		else
			return false;
	}
	
	/*
	 * Insert process into the ready queue
	 */
	public void insert(ProcessControlBlock process)
	{
		FCFSReadyQueue.add(process);
	}
	
	/*
	 * Remove the process from the ready queue
	 */
	public ProcessControlBlock remove()
	{
		return FCFSReadyQueue.remove();
	}
	
	/*
	 * Print the the processes waiting in the ready queue
	 */
	public void printProcesses()
	{
		for(ProcessControlBlock process : FCFSReadyQueue)
		{
			System.out.print(process.processNumber+"\t\t");
		}
		System.out.print("]");
		System.out.println();
	}
	
	/*
	 * The process picked from the ready queue will be processed by CPU based on process's CPU Served time.
	 */
	public void CPUPickHandler(ProcessControlBlock process,double currentEventTime)
	{
		double remainingProcessBurstTime=0;
		
		process.processState="running";
		
		EventStructure CPUEndEvent;
		//Calculate the remaining burst time of the process each time after served by CPU.On first execution, CPUServedTime of the process is 0.
		remainingProcessBurstTime = process.processBurstTime - process.CPUServedTime;
		if(remainingProcessBurstTime < process.ioMeanInterArrivalTime)
		{
			process.CPUServedTime+=remainingProcessBurstTime;
			CPUUtlizationTime+=remainingProcessBurstTime;
			CPUEndEvent = new EventStructure(process,EventType.CPU_END, (currentEventTime+remainingProcessBurstTime),"FCFS");
			EventStructure.generateEvent(CPUEndEvent);
			
		}
		else
		{
			process.CPUServedTime += process.ioMeanInterArrivalTime;
			CPUUtlizationTime+=process.ioMeanInterArrivalTime;
			CPUEndEvent = new EventStructure(process,EventType.CPU_END,(currentEventTime+process.ioMeanInterArrivalTime),"FCFS");
			EventStructure.generateEvent(CPUEndEvent);
		}
		

	}
	public void CPUEndHandler(ProcessControlBlock process,double currentTime)
	{	
	   /*
	    * Check if the process has completed it's total burst time in CPU. 
	    */
		
		//If yes, then process will be completed and would be terminated.
		if(process.processBurstTime == process.CPUServedTime)
		{ 
			process.processState="terminated";
			ProcessSimulator.noOfRemainingProcess -- ;
			
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SS");  
			Date resultdate = new Date((long) currentTime);
			
			System.out.println("The job number "+process.processNumber+" finished it's execution in the system at timestamp "+sdf.format(resultdate));
			
			turnAroundTime[(process.processNumber-1)] = currentTime - process.processArrivalTime;
			waitingTime[(process.processNumber-1)] = turnAroundTime[(process.processNumber-1)] - process.processBurstTime - process.ioServedTime;
		}
		else
		{
			//If not, then send the process to IO Queue
		   process.processState="IOWaiting";
		   EventStructure IOEvent = new EventStructure(process,EventType.IO_ENQUE, currentTime,"FCFS");
		   EventStructure.generateEvent(IOEvent);
		}
	}
	
	public void printStatistics(double endTime)
	{
		double processBeginningTime = FIFO.eTime;
		double totalDuration = endTime - processBeginningTime;
		double throughPut = (totalDuration * 0.001) /ProcessSimulator.noOfJobs;
		
		DecimalFormat df1 = new DecimalFormat();
		df1.setMaximumFractionDigits(2);
		
		DecimalFormat df2 = new DecimalFormat();
		df2.setMaximumFractionDigits(2);
		
		double cpuUtilization = (CPUUtlizationTime/totalDuration)*100;
		
		System.out.println("===========================================================================================================");
		System.out.println("Total Statistics of FCFS Execution ");
		System.out.println("-----------------------------------------------------------------------------------------------------------");
		System.out.println("ProcessNumber --- || --- TurnAroundTime (hh:mm:ss) --- || --- WaitingTime (hh:mm:ss) --- || --- Throughput (process/sec) --- || CPU Utilization (%)");
		System.out.println("-----------------------------------------------------------------------------------------------------------");
		for(int i=0;i<turnAroundTime.length;i++)
		{
			String hms1 = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours((long) turnAroundTime[i]),
				    TimeUnit.MILLISECONDS.toMinutes((long) turnAroundTime[i]) % TimeUnit.HOURS.toMinutes(1),
				    TimeUnit.MILLISECONDS.toSeconds((long) turnAroundTime[i]) % TimeUnit.MINUTES.toSeconds(1));
			
			String hms2 = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours((long) waitingTime[i]),
				    TimeUnit.MILLISECONDS.toMinutes((long) waitingTime[i]) % TimeUnit.HOURS.toMinutes(1),
				    TimeUnit.MILLISECONDS.toSeconds((long) waitingTime[i]) % TimeUnit.MINUTES.toSeconds(1));
			System.out.println((i+1)+" --- || "+hms1+" --- || --- "+hms2+" --- || --- "+df1.format(throughPut)+" --- || ---"+df2.format(cpuUtilization));
			
			System.out.println("---------------------------------------------------------------------------------------------------------");
		}
	}

}
