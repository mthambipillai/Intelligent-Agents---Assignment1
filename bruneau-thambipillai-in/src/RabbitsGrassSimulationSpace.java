import uchicago.src.sim.space.Object2DGrid;

/**
 * Class that implements the simulation space of the rabbits grass simulation.
 * @author 
 */

public class RabbitsGrassSimulationSpace {
	private static final int NBINSERTTRIES = 10;
	private Object2DGrid grassSpace;
	private Object2DGrid rabbitsSpace; 
	 
	public RabbitsGrassSimulationSpace(int xSize, int ySize) { 
		grassSpace = new Object2DGrid(xSize, ySize); 
		rabbitsSpace = new Object2DGrid(xSize, ySize); 
		for(int i = 0; i < xSize; i++) { 
			for(int j = 0; j < ySize; j++){ 
				grassSpace.putObjectAt(i,j,new Integer(0)); 
			} 
		} 
	}
	
	public void spreadGrass(int grass){
		int x,y;
		for(int i=0;i<grass;i++){
			x = (int)(Math.random()*grassSpace.getSizeX());
			y = (int)(Math.random()*grassSpace.getSizeY());
			grassSpace.putObjectAt(x, y, new Integer(getGrassValueAt(x,y)+1));
		}
	}
	
	public int getGrassValueAt(int x, int y){
		Integer valueAt = (Integer)grassSpace.getObjectAt(x, y);
		if(valueAt!=null){
			return valueAt.intValue();
		}else{
			return 0;
		}
	}
	
	public Object2DGrid getGrass(){
		return grassSpace;
	}
	
	public boolean isRabbitPresentAt(int x, int y){
		return rabbitsSpace.getObjectAt(x, y)!=null;
	}
	
	public boolean addRabbit(RabbitsGrassSimulationAgent rabbit){
		int xMax = rabbitsSpace.getSizeX();
		int yMax = rabbitsSpace.getSizeY();
		int maxTries = NBINSERTTRIES * xMax * yMax;
		
		int x,y;
		for(int i=0;i<maxTries;i++){
			x = (int)(Math.random()*xMax);
			y = (int)(Math.random()*yMax);
			if(!isRabbitPresentAt(x,y)){
				rabbitsSpace.putObjectAt(x, y, rabbit);
				rabbit.setXY(x,y);
				rabbit.setSpace(this);
				return true;
			}
		}
		return false;
	}
	
	public Object2DGrid getRabbitsSpace(){
		return rabbitsSpace;
	}
	
	public void removeRabbitAt(int x, int y){
		rabbitsSpace.putObjectAt(x,y,null);
	}
	
	public int takeGrassAt(int x, int y){
		int grass = getGrassValueAt(x,y);
		grassSpace.putObjectAt(x, y, new Integer(0));
		return grass;
	}
	
	public boolean moveRabbitAt(int x, int y, int newX, int newY){
		if(!isRabbitPresentAt(newX,newY)){
			RabbitsGrassSimulationAgent rabbit = (RabbitsGrassSimulationAgent)rabbitsSpace.getObjectAt(x, y);
			removeRabbitAt(x,y);
			rabbit.setXY(newX, newY);
			rabbitsSpace.putObjectAt(newX, newY, rabbit);
			return true;
		}
		return false;
	}
}
