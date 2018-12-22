import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class RoundRobinProcessHandler extends ReadyQueue {
	double rr_quantum;
	 public static Deque<ProcessControlBlock> RRReadyQueue = new LinkedList<ProcessControlBlock>();
	 public double CPUUtlizationTime=0;
	 
	 RoundRobinProcessHandler()
	 {
		 //Set quantum value to 20
		 rr_quantum=5;
		 System.out.println("The quantum value used : "+rr_quantum);
		for(int i=0;i<turnAroundTime.length;i++)
		{
			turnAroundTime[i]=0;
			waitingTime[i]=0;
		}

	 }

		public boolean isQueueEmpty()
		{
			if(RRReadyQueue.size() == 0)
				return true;
			else
				return false;
		}
		
		public void insert(ProcessControlBlock process)
		{
			RRReadyQueue.add(process);
		}
		
		public ProcessControlBlock remove()
		{
			return RRReadyQueue.remove();
		}
		
		public void printProcesses()
		{
			for(ProcessControlBlock process : RRReadyQueue)
			{
				System.out.print(process.processNumber+"\t\t");
			}
			System.out.print("]");
			System.out.println();
		}
		
		double findMin(double remainingProcessBurstTime,double rr_quantum,double timeToIO,ProcessControlBlock process)
		{
			if(remainingProcessBurstTime < rr_quantum && remainingProcessBurstTime<timeToIO)
			{
				process.operationType="SmallerBurstTime";
				return remainingProcessBurstTime;
			}
				
			else if(rr_quantum < timeToIO)
			{
				process.operationType="SmallerQuantum";
				return rr_quantum;
			}
				
			else
			{
				process.operationType="SmallerIOTime";
				return timeToIO;
			}
				
				
		}
		public void CPUPickHandler(ProcessControlBlock process,double currentEventTime)
		{
			System.out.println("Prcess "+process.processNumber+" is inside the CPU");
			
			double remainingProcessBurstTime=0;
			
			process.processState="running";
						
			remainingProcessBurstTime = process.processBurstTime - process.CPUServedTime;
			
			process.timeSlot = findMin(remainingProcessBurstTime,rr_quantum,process.timeToIO,process);
			
			process.CPUServedTime+=process.timeSlot;
			CPUUtlizationTime+=process.timeSlot;
			
			EventStructure CPUEndEvent = new EventStructure(process, EventType.CPU_END, (currentEventTime+process.timeSlot), "RR");
			EventStructure.generateEvent(CPUEndEvent);
		}


		public void CPUEndHandler(ProcessControlBlock process, double currentEventTime)
		{
			if(process.processBurstTime == process.CPUServedTime)
			{
				ProcessSimulator.noOfRemainingProcess -- ;
				
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SS");  
				Date resultdate = new Date((long) currentEventTime);
				System.out.println("Job number "+process.processNumber+" finished at "+currentEventTime);
				System.out.println("The job number "+process.processNumber+" finished it's execution in the system at timestamp "+sdf.format(resultdate));
				turnAroundTime[(process.processNumber-1)] = currentEventTime - process.processArrivalTime;
				waitingTime[(process.processNumber-1)] = turnAroundTime[(process.processNumber-1)] - process.processBurstTime - process.ioServedTime;
				
			}
			else if(process.operationType.equals("SmallerQuantum"))
			{
				process.timeToIO-=rr_quantum;
				RRReadyQueue.add(process);
				System.out.println("Added Process "+process.processNumber+" to ready  queue");
				System.out.println("Remaining IO trigger time for process "+process.processNumber+" is "+process.timeToIO);
			}
			else if(process.operationType.equals("SmallerIOTime"))
			{
				process.processState="IO";
				EventStructure IOEvent = new EventStructure(process,EventType.IO_ENQUE, currentEventTime,"RR");
				EventStructure.generateEvent(IOEvent);
			}
				
		}
		
		public void printStatistics(double endTime)
		{
			double processBeginningTime = RoundRobin.eTime;
			double totalDuration = endTime - processBeginningTime;
			double throughPut = (totalDuration*0.001 )/ProcessSimulator.noOfJobs;
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			double cpuUtilization = (CPUUtlizationTime/totalDuration)*100;
			
			System.out.println("=============================================================================================================");
			System.out.println("Total Statistics of Round Robin Execution ");
			System.out.println("-------------------------------------------------------------------------------------------------------------");
			System.out.println("ProcessNumber --- || --- TurnAroundTime (hh:mm:ss)--- || --- WaitingTime (hh:mm:ss)--- || Throughput (process/sec) ---|| --- CPU Utilization (%)");
			System.out.println("-------------------------------------------------------------------------------------------------------------");
			for(int i=0;i<turnAroundTime.length;i++)
			{
				String hms1 = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours((long) turnAroundTime[i]),
					    TimeUnit.MILLISECONDS.toMinutes((long) turnAroundTime[i]) % TimeUnit.HOURS.toMinutes(1),
					    TimeUnit.MILLISECONDS.toSeconds((long) turnAroundTime[i]) % TimeUnit.MINUTES.toSeconds(1));
				
				String hms2 = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours((long) waitingTime[i]),
					    TimeUnit.MILLISECONDS.toMinutes((long) waitingTime[i]) % TimeUnit.HOURS.toMinutes(1),
					    TimeUnit.MILLISECONDS.toSeconds((long) waitingTime[i]) % TimeUnit.MINUTES.toSeconds(1));
				System.out.println((i+1)+" --- || "+hms1+" --- || --- "+hms2+" --- || --- "+df.format(throughPut)+" --- || --- "+df.format(cpuUtilization));
				System.out.println("-----------------------------------------------------------------------------------------------------------");
			}
		}


}
