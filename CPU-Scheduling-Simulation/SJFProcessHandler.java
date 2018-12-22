import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

class ExponentialAverage {
	private double alpha;
	//Initial Tau0 value = 100000
	private double oldValue = 100000;
	double newValue;
	ExponentialAverage(double alpha)
	{
		this.alpha = alpha;
	}
	public double calculateAverage(double value)
	{

		newValue = (alpha * value) + (1-alpha)*oldValue;
		oldValue = newValue;
		return newValue;
			
	}
}

class ExpAvergeComparator implements Comparator<ProcessControlBlock> {
	public int compare(ProcessControlBlock p1, ProcessControlBlock p2)
	{
		if(p1.expAvg < p2.expAvg)
		{
			return -1;
		}
		else if(p1.expAvg > p2.expAvg)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
}
public class SJFProcessHandler extends ReadyQueue
{	
	public PriorityQueue<ProcessControlBlock> sjfCPUQueue = new PriorityQueue<ProcessControlBlock>(100, new ExpAvergeComparator());
	public double CPUUtlizationTime=0;
	
	ProcessControlBlock process;

	
	SJFProcessHandler()
	{
		
	}
	
	SJFProcessHandler(ProcessControlBlock process)
	{
		this.process = process;
	}
	
	/* 
	 * @see ReadyQueue#isQueueEmpty()
	 */
	public boolean isQueueEmpty()
	{
		if(sjfCPUQueue.size() == 0)
			return true;
		else
			return false;
	}
	
	/*
	 * Insert process into the ready queue
	 */
	public void insert(ProcessControlBlock process)
	{
		ExponentialAverage ExpAvg = new ExponentialAverage(0.5);
		process.expAvg = ExpAvg.calculateAverage(process.processBurstTime);
		sjfCPUQueue.add(process);
		
	}
	
	/*
	 * Remove the process from the ready queue
	 */
	public ProcessControlBlock remove()
	{
		return sjfCPUQueue.remove();
	}
	
	/*
	 * Print the the processes waiting in the ready queue
	 */
	public void printProcesses()
	{
		for(ProcessControlBlock process : sjfCPUQueue)
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
			CPUEndEvent = new EventStructure(process,EventType.CPU_END, (currentEventTime+remainingProcessBurstTime),"SJF");
			EventStructure.generateEvent(CPUEndEvent);	
		}
		else
		{
			process.CPUServedTime += process.ioMeanInterArrivalTime;
			CPUUtlizationTime+= process.ioMeanInterArrivalTime;
			CPUEndEvent = new EventStructure(process,EventType.CPU_END,(currentEventTime+process.ioMeanInterArrivalTime),"SJF");
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
		   process.processState="IO";
		   EventStructure IOEvent = new EventStructure(process,EventType.IO_ENQUE, currentTime,"SJF");
		   EventStructure.generateEvent(IOEvent);
		}
	}
	
	public void printStatistics(double endTime)
	{
		double processBeginningTime = SJF.eTime;
		double totalDuration = endTime - processBeginningTime;
		double throughPut = (totalDuration *0.001)/ProcessSimulator.noOfJobs;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		double cpuUtilization = (CPUUtlizationTime/totalDuration)*100;
		
		System.out.println("================================================================================================================================");
		System.out.println("Total Statistics of SJF Execution ");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("ProcessNumber --- || --- TurnAroundTime (hh:mm:ss) --- || --- WaitingTime (hh:mm:ss) --- || --- Throughput (process/sec) --- || --- CPU Utilization (%)");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
		for(int i=0;i<turnAroundTime.length;i++)
		{
			String hms1 = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours((long) turnAroundTime[i]),
				    TimeUnit.MILLISECONDS.toMinutes((long) turnAroundTime[i]) % TimeUnit.HOURS.toMinutes(1),
				    TimeUnit.MILLISECONDS.toSeconds((long) turnAroundTime[i]) % TimeUnit.MINUTES.toSeconds(1));
			
			String hms2 = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours((long) waitingTime[i]),
				    TimeUnit.MILLISECONDS.toMinutes((long) waitingTime[i]) % TimeUnit.HOURS.toMinutes(1),
				    TimeUnit.MILLISECONDS.toSeconds((long) waitingTime[i]) % TimeUnit.MINUTES.toSeconds(1));
			System.out.println((i+1)+" --- || "+hms1+" --- || --- "+hms2+" --- || --- "+df.format(throughPut)+" --- || --- "+df.format(cpuUtilization));
			System.out.println("---------------------------------------------------------------------------------------------------------------------");
		}
		
	}
}