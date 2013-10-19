package net.minecraft.client;

public class PlayerAI_PathNode {
	/** The x coordinate of this point */
    public int xCoord;

    /** The y coordinate of this point */
    public int yCoord;

    /** The z coordinate of this point */
    public int zCoord;
    
    /** Whether or not this point is the destination point */
    public boolean destination = false;
    
    public int destx = 0;
    public int desty = 0;
    public int destz = 0;
    public Integer hashvalue;
    
    public int costSoFar;
    public double estTotalCost;
    public PlayerAI_PathNode parent;
    public boolean jumpneeded = false;
    public boolean isFalling = false;
    public boolean destroy = false;
    public double distance = 0;
    public int size = 1;

	public PlayerAI_PathNode(int x, int y, int z, int destx, int desty, int destz) {
		xCoord = x;
		yCoord = y;
		zCoord = z;
		this.destx = destx;
		this.desty = desty;
		this.destz = destz;
		int dx = destx - xCoord;
		int dy = desty - yCoord;
		int dz = destz - zCoord;
		costSoFar = 0;
		estTotalCost = 0;
		hashvalue = (xCoord*yCoord*zCoord)%16;
		distance = Math.sqrt((dx*dx) + (dy*dy) + (dz*dz));
		updateEstCost();
	}
	public double updateEstCost(){
		estTotalCost = costSoFar + (1 + getWeight())*distance;
		return estTotalCost;
	}
	private double getWeight(){
		double weight = 1;
		if(size == 1){
			if(distance > 100){
				weight = 30;
			}
			else{
				weight = 5;
			}
		}
		else if(size == 2){
			if(distance > 100){
				weight = 60;
			}
			else{
				weight = 30;
			}
		}
		else if(size == 3){
			if(distance > 100){
				weight = 1000;
			}
			else{
				weight = 60;
			};
		}
		return weight;
	}
	public boolean isDestination(){
		if(destx == xCoord && desty == yCoord && destz == zCoord){
			destination = true;
			return true;
		}
		else{
			return false;
		}
	}
	public boolean equals(PlayerAI_PathNode node){
		if(node.xCoord == this.xCoord && node.yCoord == this.yCoord && node.zCoord == this.zCoord){
			return true;
		}
		else{
			return false;
		}
	}
	
	public String toString(){
		String temp = "Coordinates (x,y,z): ";
		temp = temp + xCoord + ", " + yCoord + ", " + zCoord + " | Jump Needed? " + jumpneeded;
		System.out.println(temp);
		return temp;
	}

}
