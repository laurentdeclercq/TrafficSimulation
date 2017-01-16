/**
 * SimulationReader.java
 * @version 30/12/16
 * @author Unknown, adaptations by Laurent Declercq
 */

import java.io.*;
import traffic.*;
import traffic.CrossRoad.Orientation;
import traffic.TrafficLight.State;

/**
 * SimulationReader class is aimed at reading input-files for CITY and CAR configuration.
 * It contains a CityLineParser and a VehicleLineParser.
 * Both are taking the necessary steps to setup the appropriate things, with the help of the TrafficFlowSimulation class.
 */
public class SimulationReader {

	public TrafficFlowSimulation simulation;

	private interface LineParser {
		public void parse(String line, int lineIndex);
	}

	private class CityLineParser implements LineParser {
		public void parse(String line, int lineIndex) {
			String[] args = line.split(" ");
			if (args[0].equals("$")) {
				String cityName = args[1];
				int nrCols = Integer.parseInt(args[2]);
				int nrRows = Integer.parseInt(args[3]);
				
				simulation.initiateCity(cityName, nrRows, nrCols); // name, rows, cols
			} else if (args[0].equals("@")) {
				String from = args[1];
				String to = args[2];
				simulation.addRoadWork(from, to);
				
				// here no case for '#' needed since this is taken into account inside read(). 
				// Also the lineindex is only incremented for NON-comment lines
			
			} else {
				int row = lineIndex;	// here we need to do -1 since w
				int nrCols = args.length / 19;	// 19 arguments per crossroad on the same line
				for (int column = 0; column < nrCols; column += 1) {
					int offset = column * 19;
					String crossName = args[0 + offset];
					
					int horizontalGreen = Integer.parseInt(args[1 + offset]);
					int verticalGreen = Integer.parseInt(args[2 + offset]);
					int horizontalRed = verticalGreen + 1;
					int verticalRed = horizontalGreen + 1;
					
					int lightFlow_North = Integer.parseInt(args[7 + offset]);
					int lightFlow_East = Integer.parseInt(args[8 + offset]);
					int lightFlow_South = Integer.parseInt(args[9 + offset]);
					int lightFlow_West = Integer.parseInt(args[10 + offset]);
					
					int lightCap_North = Integer.parseInt(args[15 + offset]);
					int lightCap_East = Integer.parseInt(args[16 + offset]);
					int lightCap_South = Integer.parseInt(args[17 + offset]);
					int lightCap_West = Integer.parseInt(args[18 + offset]);
					
					int streetTime_North = Integer.parseInt(args[3 + offset]);
					int streetTime_East = Integer.parseInt(args[4 + offset]);
					int streetTime_South = Integer.parseInt(args[5 + offset]);
					int streetTime_West = Integer.parseInt(args[6 + offset]);
					
					int streetCap_North = Integer.parseInt(args[11 + offset]);
					int streetCap_East = Integer.parseInt(args[12 + offset]);
					int streetCap_South = Integer.parseInt(args[13 + offset]);
					int streetCap_West = Integer.parseInt(args[14 + offset]);
					

					// Create a default CR here
					System.out.println("Creating Default CR with name: " + crossName);
					CrossRoad newCR = new CrossRoad(crossName);
					
					// change all the CR values
					newCR.changeCRValuesInOrientation(Orientation.North, lightFlow_North, lightCap_North, streetTime_North, streetCap_North);
					newCR.changeCRValuesInOrientation(Orientation.East, lightFlow_East, lightCap_East, streetTime_East, streetCap_East);
					newCR.changeCRValuesInOrientation(Orientation.South, lightFlow_South, lightCap_South, streetTime_South, streetCap_South);		
					newCR.changeCRValuesInOrientation(Orientation.West, lightFlow_West, lightCap_West, streetTime_West, streetCap_West);
					
					System.out.println("All values of " + crossName + " changed to fit input-file");

					
					// change the timing of the lights at the CR
					for(Orientation o : Orientation.values()){
						if(o == Orientation.North || o == Orientation.South){
							newCR.getLight(o).setTimeForState(State.GREEN, horizontalGreen);
							newCR.getLight(o).setTimeForState(State.RED, horizontalRed);
						}
						else{
							newCR.getLight(o).setTimeForState(State.GREEN, verticalGreen);
							newCR.getLight(o).setTimeForState(State.RED, verticalRed);
						}
					}
					for(Orientation o : Orientation.values()){
						System.out.println("New CR : " + newCR.getName() +"," + o + newCR.getLight(o).getGreenFlow());
					}
					
					System.out.println("Replacing CR at : r"+ row + " , c" + column + " with newCR:" + newCR);
					simulation.setUpCrossroad(row, column, newCR);
				}
			}
		}
	}

	private class VehicleLineParser implements LineParser {
		public void parse(String line, int lineIndex) {
			String[] args = line.split(" ");
			String name = args[0];
			String start = args[1];
			String destination = args[2];
			boolean priority = (args[3].equals("1"));
			boolean dynamicPath = (args[4].equals("1"));	// this is passed to the Vehicle class
			Vehicle v = new Vehicle(name, destination, priority, dynamicPath);
			System.out.println("new vehicle: " + v);
			simulation.addVehicle(v, start);
		}
	}

	public SimulationReader(TrafficFlowSimulation simulation) {
		this.simulation = simulation;
	}

	void read(String fileName, LineParser lineParser) {
		File inputFile = new File(fileName);
		try {
			BufferedReader input = new BufferedReader(new FileReader(inputFile));
			try {
				String line = null;
				int lineIndex = 0;
				while ((line = input.readLine()) != null) {
					if (!line.startsWith("#")) {
						lineParser.parse(line, lineIndex);
						if(!line.startsWith("$"))
							lineIndex += 1;
					}
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	void readCity(String fileName) {
		read(fileName, new CityLineParser());
	}

	void readVehicles(String fileName) {
		read(fileName, new VehicleLineParser());
	}

}
