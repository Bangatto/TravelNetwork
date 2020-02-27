public class TrainNetwork {
	final int swapFreq = 2;
	TrainLine[] networkLines;

    public TrainNetwork(int nLines) {
    	this.networkLines = new TrainLine[nLines];
    }
    
    public void addLines(TrainLine[] lines) {
    	this.networkLines = lines;
    }
    
    public TrainLine[] getLines() {
    	return this.networkLines;
    }
  
    public void dance() {
    	System.out.println("The tracks are moving!");
    	//loop through the train line array and 
    	TrainLine[] lines = getLines();
    	for(TrainLine line: lines) {
    		line.shuffleLine();
    	}
    	
    }
    
    public void undance() {
    	//loop through each trainLin, sort each line
    	TrainLine[] lines = getLines();
    	for(TrainLine line: lines) {
    		line.sortLine();
    	}
    }
    
    public int travel(String startStation, String startLine, String endStation, String endLine) {
    	
    	TrainLine curLine = getLineByName(startLine); //use this variable to store the current line.
    	TrainStation curStation= curLine.findStation(startStation); //use this variable to store the current station. 
    	TrainLine endTrainLine = getLineByName(endLine);
    	TrainStation endTrainStation = endTrainLine.findStation(endStation);
    	TrainStation prevStation = curStation;
    	
    	int hoursCount = 0;
    	System.out.println("Departing from "+startStation);
    	while(!curStation.equals(endTrainStation)) {
    		TrainStation nextStation = curLine.travelOneStation(curStation, prevStation);
    		prevStation = curStation;
    		curStation = nextStation;
    		curLine =curStation.getLine(); 
    		hoursCount++;
    		//check whether 2hrs passed and dance the lines every two hours.
    		if(hoursCount %2 ==0) {
    			this.dance();
    		}
    		if(hoursCount == 168) {
				System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
				return hoursCount;
    		}
    		//prints an update on your current location in the network.
	    	System.out.println("Traveling on line "+curLine.getName()+":"+curLine.toString());
	    	System.out.println("Hour "+hoursCount+". Current station: "+curStation.getName()+" on line "+curLine.getName());
	    	System.out.println("=============================================");
	    	
    	}
	    System.out.println("Arrived at destination after "+hoursCount+" hours!");
	    return hoursCount;
    }
    
    //you can extend the method header if needed to include an exception. You cannot make any other change to the header.
    public TrainLine getLineByName(String lineName){
    	//loop through the network lines
    	//check if the current equals the line name and if not throw an error
    	TrainLine[] lines = getLines();
    	for (TrainLine curLine: lines) {
    		if (curLine.getName().equals(lineName)) {
    			return curLine;
    			}
    	}
    	throw new LineNotFoundException("this line is not in this network");
   
    }
    
    
  //prints a plan of the network for you.
    public void printPlan() {
    	System.out.println("CURRENT TRAIN NETWORK PLAN");
    	System.out.println("----------------------------");
    	for(int i=0;i<this.networkLines.length;i++) {
    		System.out.println(this.networkLines[i].getName()+":"+this.networkLines[i].toString());
    		}
    	System.out.println("----------------------------");
    }
}

//exception when searching a network for a LineName and not finding any matching Line object.
class LineNotFoundException extends RuntimeException {
	   String name;

	   public LineNotFoundException(String n) {
	      name = n;
	   }

	   public String toString() {
	      return "LineNotFoundException[" + name + "]";
	   }
	}