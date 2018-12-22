public class ProcessControlBlock {
	int processNumber;
	String processState;
	double processBurstTime;
	double ioMeanInterArrivalTime;
	double CPUServedTime;
	double processArrivalTime;
	double processWaitingTime;
	double processTurnAroundTime;
	double ioServedTime;
	double timeToIO;
	double timeSlot;
	String operationType;
	double expAvg=0;
	
	ProcessControlBlock(int processNumber, String processState,double processBurstTime,double ioMeanInterArrivalTime,double CPUServedTime,double processArrivalTime)
	{
		this.processNumber = processNumber;
		this.processState = processState;
		this.processBurstTime = processBurstTime;
		this.ioMeanInterArrivalTime =  ioMeanInterArrivalTime;
		this.CPUServedTime = CPUServedTime;		
		this.processArrivalTime = processArrivalTime;
		this.processWaitingTime = 0;
		this.processTurnAroundTime = 0;
		this.timeToIO=ioMeanInterArrivalTime;
		this.ioServedTime=0;
		
	}

}
