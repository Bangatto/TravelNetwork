import java.util.Arrays;
import java.util.Random;

public class TrainLine {

	private TrainStation leftTerminus;
	private TrainStation rightTerminus;
	private String lineName;
	private boolean goingRight;
	public TrainStation[] lineMap;
	public static Random rand;

	public TrainLine(TrainStation leftTerminus, TrainStation rightTerminus, String name, boolean goingRight) {
		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		this.lineMap = this.getLineArray();
	}

	public TrainLine(TrainStation[] stationList, String name, boolean goingRight)
	/*
	 * Constructor for TrainStation input: stationList - An array of TrainStation
	 * containing the stations to be placed in the line name - Name of the line
	 * goingRight - boolean indicating the direction of travel
	 */
	{
		TrainStation leftT = stationList[0];
		TrainStation rightT = stationList[stationList.length - 1];

		stationList[0].setRight(stationList[stationList.length - 1]);
		stationList[stationList.length - 1].setLeft(stationList[0]);

		this.leftTerminus = stationList[0];
		this.rightTerminus = stationList[stationList.length - 1];
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		for (int i = 1; i < stationList.length - 1; i++) {
			this.addStation(stationList[i]);
		}

		this.lineMap = this.getLineArray();
	}

	public TrainLine(String[] stationNames, String name,
			boolean goingRight) {/*
									 * Constructor for TrainStation. input: stationNames - An array of String
									 * containing the name of the stations to be placed in the line name - Name of
									 * the line goingRight - boolean indicating the direction of travel
									 */
		TrainStation leftTerminus = new TrainStation(stationNames[0]);
		TrainStation rightTerminus = new TrainStation(stationNames[stationNames.length - 1]);

		leftTerminus.setRight(rightTerminus);
		rightTerminus.setLeft(leftTerminus);

		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;
		for (int i = 1; i < stationNames.length - 1; i++) {
			this.addStation(new TrainStation(stationNames[i]));
		}

		this.lineMap = this.getLineArray();

	}

	// adds a station at the last position before the right terminus
	public void addStation(TrainStation stationToAdd) {
		TrainStation rTer = this.rightTerminus;
		TrainStation beforeTer = rTer.getLeft();
		rTer.setLeft(stationToAdd);
		stationToAdd.setRight(rTer);
		beforeTer.setRight(stationToAdd);
		stationToAdd.setLeft(beforeTer);

		stationToAdd.setTrainLine(this);

		this.lineMap = this.getLineArray();
	}

	public String getName() {
		return this.lineName;
	}

	public int getSize() {
		//want to know how many stations are in the array of TrainStations
		TrainStation currStation = this.leftTerminus;
		int size = 0;
		while(currStation !=null) {
			size++;
			currStation=currStation.getRight();
		}
		return size;
	}

	public void reverseDirection() {
		this.goingRight = !this.goingRight;
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation travelOneStation(TrainStation current, TrainStation previous) {
		//check if current station is in this line
		if(current.getLine() != this) {
			throw new StationNotFoundException("Station not on this line");
		}
		if(!current.hasConnection) {
			return this.getNext(current);
		}
		TrainStation nextStation = current.getTransferStation();
		TrainLine nextLine = current.getTransferLine();
		if(nextStation==previous) {
			return this.getNext(current);
		}
		return nextStation;
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation getNext(TrainStation station) {
		//call find station to handle the exception
		findStation(station.getName());
		if(!this.goingRight) {
			if(this.leftTerminus==station) {
				this.reverseDirection();
				
				return station.getRight();
			}else {
				return station.getLeft();
			}
		}else {
			if(this.rightTerminus==station) {
				
				this.reverseDirection();
				return station.getLeft();
			}else {
				return station.getRight();
			}
		}
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation findStation(String name) {
		//check whether the station given is this.station.
		TrainStation currStation = this.leftTerminus;
		TrainStation end = this.rightTerminus;
		while (currStation != end) {
			if(currStation.getName().equals(name)) {
				return currStation;
			}
			currStation = currStation.getRight();
		}
		if(currStation.getName().equals(name)) {
			return currStation;
		}else {
			throw new StationNotFoundException("This station is not on this line");
		}
		
	}

	public void sortLine() {
		TrainStation[] trainStations = getLineArray();
		int size  = getSize();
		for(int i=0; i < size; i++) {
			for(int j=i+1; j < size; j++) {
				if(trainStations[i].getName().compareTo(trainStations[j].getName()) > 0){
					TrainStation temp=trainStations[j];
					trainStations[j]=trainStations[i];
					trainStations[i]=temp;
				}
			
			}
		}
		//update line map
		this.lineMap = trainStations;
		for(int i=0; i < size; i++) {
			//check whether it is the first station
			if(i ==0) {
				trainStations[0].setLeft(null);
				trainStations[0].setRight(trainStations[1]);
				trainStations[0].setLeftTerminal();
				this.leftTerminus=trainStations[0];
				//check whether it is the last station
			} else if(i==size-1) {
				trainStations[i].setRight(null);
				trainStations[i].setLeft(trainStations[i-1]);
				trainStations[i].setRightTerminal();
				this.rightTerminus=trainStations[i];
			}else {
				trainStations[i].setNonTerminal();
				trainStations[i].setRight(trainStations[i+1]);
				trainStations[i].setLeft(trainStations[i-1]);
			}
		}

	}
	
	public TrainStation[] getLineArray() {
		int size = getSize();
		TrainStation[] trainArr =new TrainStation[size];
		TrainStation current = this.leftTerminus;
		for(int index=0; index <size ; index++) {
			trainArr[index]=current;
			current = current.getRight();
		}
		return trainArr;
	}

	private TrainStation[] shuffleArray(TrainStation[] array) {
		Random rand = new Random();
		rand.setSeed(11);
		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			TrainStation temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		this.lineMap = array;
		return array;
	}

	public void shuffleLine() {

		// you are given a shuffled array of trainStations to start with
		TrainStation[] lineArray = this.getLineArray();
		TrainStation[] shuffledArray = shuffleArray(lineArray);
		int size =  getSize();
		for(int i=0; i < size; i++) {
			if(i ==0) {
				shuffledArray[0].setLeft(null);
				shuffledArray[0].setRight(shuffledArray[1]);
				shuffledArray[0].setNonTerminal();
				shuffledArray[0].setLeftTerminal();
				this.leftTerminus=shuffledArray[0];
			} else if(i==size-1) {
				shuffledArray[i].setRight(null);
				shuffledArray[i].setLeft(shuffledArray[i-1]);
				shuffledArray[i].setNonTerminal();
				shuffledArray[i].setRightTerminal();
				this.rightTerminus=shuffledArray[i];
			}else {
				shuffledArray[i].setNonTerminal();
				shuffledArray[i].setRight(shuffledArray[i+1]);
				shuffledArray[i].setLeft(shuffledArray[i-1]);
			}
		}
	}

	public String toString() {
		TrainStation[] lineArr = this.getLineArray();
		String[] nameArr = new String[lineArr.length];
		for (int i = 0; i < lineArr.length; i++) {
			nameArr[i] = lineArr[i].getName();
		}
		return Arrays.deepToString(nameArr);
	}

	public boolean equals(TrainLine line2) {

		// check for equality of each station
		TrainStation current = this.leftTerminus;
		TrainStation curr2 = line2.leftTerminus;

		try {
			while (current != null) {
				if (!current.equals(curr2))
					return false;
				else {
					current = current.getRight();
					curr2 = curr2.getRight();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public TrainStation getLeftTerminus() {
		return this.leftTerminus;
	}

	public TrainStation getRightTerminus() {
		return this.rightTerminus;
	}
}

//Exception for when searching a line for a station and not finding any station of the right name.
class StationNotFoundException extends RuntimeException {
	String name;

	public StationNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "StationNotFoundException[" + name + "]";
	}
}
