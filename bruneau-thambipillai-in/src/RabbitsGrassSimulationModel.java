import java.awt.Color;
import java.util.ArrayList;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.util.SimUtilities;

/**
 * Class that implements the simulation model for the rabbits grass
 * simulation.  This is the first class which needs to be setup in
 * order to run Repast simulation. It manages the entire RePast
 * environment and the simulation.
 *
 * @author 
 */


public class RabbitsGrassSimulationModel extends SimModelImpl {
	
	private static final int NUMRABBITS = 10;
	private static final int WORLDXSIZE = 20;
	private static final int WORLDYSIZE = 20;
	private static final int INITGRASS = 500;
	private static final int GRASSGROWTHRATE = 30;
	private static final int BIRTHTHRESHOLD = 20;

	private int numRabbits = NUMRABBITS;
	private int worldXSize = WORLDXSIZE;
	private int worldYSize = WORLDYSIZE;
	private int grass = INITGRASS;
	private int birthThreshold = BIRTHTHRESHOLD;
	
	private Schedule schedule;
	private RabbitsGrassSimulationSpace simSpace;
	private DisplaySurface displaySurface;
	
	private ArrayList<RabbitsGrassSimulationAgent> rabbitsList;

	public static void main(String[] args) {
		System.out.println("Rabbit skeleton");
		SimInit init = new SimInit();
		RabbitsGrassSimulationModel model = new RabbitsGrassSimulationModel();
		init.loadModel(model, "", false);
	}

	public void begin() {
		buildModel();
		buildSchedule();
		buildDisplay();
		displaySurface.display();
	}
	
	public void buildModel(){
		System.out.println("Building model");
		simSpace = new RabbitsGrassSimulationSpace(worldXSize, worldYSize);
		simSpace.spreadGrass(grass);
		for(int i=0;i<numRabbits;i++){
			addAndReportNewRabbit();
		}
	}
	public void buildSchedule(){
		System.out.println("Building schedule");
		
		class RabbitStep extends BasicAction{
			public void execute(){
				SimUtilities.shuffle(rabbitsList);
				for(RabbitsGrassSimulationAgent rabbit: rabbitsList){
					rabbit.step();
				}
				
				int deadRabbits = makeRabbitsDie();
				if(deadRabbits > 0){
					System.out.println(deadRabbits+" rabbits died.");
				}
				
				int bornRabbits = makeRabbitsReproduce();
				if(bornRabbits > 0){
					System.out.println(bornRabbits+" rabbits born.");
				}
				
				displaySurface.updateDisplay();
			}
		}
		
		class GrassStep extends BasicAction{
			public void execute(){
				simSpace.spreadGrass(GRASSGROWTHRATE);
				displaySurface.updateDisplay();
			}
		}
		
		schedule.scheduleActionBeginning(0, new RabbitStep());
		schedule.scheduleActionBeginning(0, new GrassStep());
	}
	
	private int makeRabbitsDie(){
		int count = 0;
		for(int i=(rabbitsList.size()-1);i>=0;i--){
			RabbitsGrassSimulationAgent rabbit = (RabbitsGrassSimulationAgent)rabbitsList.get(i);
			if(rabbit.getEnergy()<1){
				simSpace.removeRabbitAt(rabbit.getX(), rabbit.getY());
				rabbitsList.remove(i);
				count++;
			}
		}
		return count;
	}
	
	private int makeRabbitsReproduce(){
		int count = 0;
		for(int i=(rabbitsList.size()-1);i>=0;i--){
			RabbitsGrassSimulationAgent rabbit = (RabbitsGrassSimulationAgent)rabbitsList.get(i);
			if(rabbit.getEnergy()>=birthThreshold){
				rabbit.loseReproductionEnergy();
				addAndReportNewRabbit();
				count++;
			}
		}
		return count;
	}
	
	
	public void buildDisplay(){
		ColorMap map = new ColorMap();
		for(int i=1;i<16;i++){
			map.mapColor(i, new Color(0, (int)(i*8 + 127), 0));
		}
		map.mapColor(0, Color.white);
		
		Value2DDisplay displayGrass = new Value2DDisplay(simSpace.getGrass(), map);
		
		Object2DDisplay displayRabbits = new Object2DDisplay(simSpace.getRabbitsSpace());
		displayRabbits.setObjectList(rabbitsList);
		
		displaySurface.addDisplayableProbeable(displayGrass, "Grass");
		displaySurface.addDisplayableProbeable(displayRabbits, "Rabbits");
	}

	public String[] getInitParam(){
		String[] initParams = { "BirthThreshold", "Grass", "NumRabbits", "WorldXSize", "WorldYSize"};
		return initParams;
	}

	public String getName() {
		return "RabbitsGrassSimulationModel";
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setup() {
		System.out.println("Running setup");
		rabbitsList = new ArrayList<RabbitsGrassSimulationAgent>();
		simSpace = null;
		schedule = new Schedule(1);
		
		if(displaySurface!=null){
			displaySurface.dispose();
		}
		displaySurface = null;
		displaySurface = new DisplaySurface(this, "Rabbits Grass Simulation Window 1");
		registerDisplaySurface("Rabbits Grass Simulation Window 1", displaySurface);
	}

	public int getWorldXSize() {
		return worldXSize;
	}

	public void setWorldXSize(int worldXSize) {
		this.worldXSize = worldXSize;
	}

	public int getWorldYSize() {
		return worldYSize;
	}

	public void setWorldYSize(int worldYSize) {
		this.worldYSize = worldYSize;
	}

	public int getNumRabbits() {
		return numRabbits;
	}

	public void setNumRabbits(int numRabbits) {
		this.numRabbits = numRabbits;
	}
	
	private void addAndReportNewRabbit(){
		RabbitsGrassSimulationAgent newRabbit = new RabbitsGrassSimulationAgent();
		rabbitsList.add(newRabbit);
		simSpace.addRabbit(newRabbit);
		newRabbit.report();
	}
}
