import java.awt.Color;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;


/**
 * Class that implements the simulation agent for the rabbits grass simulation.

 * @author
 */

public class RabbitsGrassSimulationAgent implements Drawable {
	private static final int INITENERGY = 5;
	private static final int REPRODUCTIONENERGY = 8;
	private static final Random r = new Random();
	
	private int x;
	private int y;
	private int vX;
	private int vY;
	private int energy;
	private static int IDNumber = 0;
	private int ID;
	private RabbitsGrassSimulationSpace space;
	
	public RabbitsGrassSimulationAgent(){
		x = -1;
		y = -1;
		energy = INITENERGY;
		setVxVy();
		IDNumber++;
		ID = IDNumber;
	}

	public void draw(SimGraphics arg0) {
		//arg0.drawFastRoundRect(Color.blue);
		ImageIcon rabbitImage= new ImageIcon("rabbitIcon.png");
		arg0.drawImageToFit(rabbitImage.getImage());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setXY(int newX, int newY){
		x = newX;
		y = newY;
	}
	
	private void setVxVy(){
		vX = r.nextInt(3)-1;
		if(vX==0){
			if(r.nextBoolean()){
				vY = -1;
			}else{
				vY = 1;
			}
		}else{
			vY=0;
		}
	}
	
	public String getID(){
		return "Rabbit "+ID;
	}
	
	public int getEnergy(){
		return energy;
	}
	
	public void report(){
		System.out.println(getID()+" at ("+x+","+y+") has "+energy+" energy points.");
	}
	
	public void step(){
		int newX = x+vX;
		int newY = y+vY;
		
		Object2DGrid rabbitsSpace = space.getRabbitsSpace();
		int sizeX = rabbitsSpace.getSizeX();
		int sizeY = rabbitsSpace.getSizeY();
		newX = (newX + sizeX) % sizeX;
		newY = (newY + sizeY) % sizeY;
		
		if(tryMove(newX, newY)){
			int grass = space.takeGrassAt(x, y);
			energy += grass;
			System.out.println("rabbit "+ID+" gained "+grass);
		}
		setVxVy();
		energy--;
	}
	
	public void setSpace(RabbitsGrassSimulationSpace newSpace){
		space = newSpace;
	}
	
	private boolean tryMove(int newX, int newY){
		return space.moveRabbitAt(x, y, newX, newY);
	}
	
	public void loseReproductionEnergy(){
		energy -= REPRODUCTIONENERGY;
	}
}
