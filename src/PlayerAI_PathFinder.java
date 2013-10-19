package net.minecraft.client;

import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.src.AxisAlignedBB;

public class PlayerAI_PathFinder {

	private Minecraft mc;
	private int x;
	private int y;
	private int z;
	private PlayerAI_PathNode tempnode;
	private Double maxval;
	public LinkedList<PlayerAI_PathNode> pathNodes = new LinkedList<PlayerAI_PathNode>();
	private LinkedList<PlayerAI_PathNode> openNodes = new LinkedList<PlayerAI_PathNode>();
	private LinkedList<PlayerAI_PathNode> closedNodes = new LinkedList<PlayerAI_PathNode>();
	private AxisAlignedBB AABB;
	private AxisAlignedBB AABB2;
	
	public PlayerAI_PathFinder() {
		maxval = 0.0;
		maxval = maxval.MAX_VALUE;
	}
	public void updateMC(Minecraft mcin, int x, int y, int z){
		mc = mcin;
		this.x = x;
		this.y = y;
		this.z = z;
		makePath();
		/*
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
		*/
	}
	public int distToDest(PlayerAI_PathNode current){
		return (int) current.distance;
	}
	public void makePath(){
		PlayerAI_PathNode current;
		openNodes.offerFirst(current = new PlayerAI_PathNode((int)mc.thePlayer.posX, (int)mc.thePlayer.posY, (int)mc.thePlayer.posZ, x, y, z));
		double bestCost = current.updateEstCost();
		
		while(openNodes.isEmpty() == false){
			double min = maxval;
			int iTemp = 0;
			Iterator<PlayerAI_PathNode> iter = openNodes.iterator();
			PlayerAI_PathNode tempopen1;
			for(int i = 0; i < openNodes.size(); i++){
				tempopen1 = iter.next();
				tempopen1.updateEstCost();
				double tempd = tempopen1.estTotalCost;
				if(min > tempd){
					min = tempd;
					iTemp = i;
					current = tempopen1;
				}
			}
			if(current.isDestination()){
				closedNodes.offerFirst(current);
				constructPath();
				return;
			}
			openNodes.remove(iTemp);
			closedNodes.push(current);
			int bs = 20;
			AABB = new AxisAlignedBB(current.xCoord- bs, current.yCoord-1, current.zCoord-bs, current.xCoord +bs, current.yCoord, current.zCoord +bs);
			AABB2 = new AxisAlignedBB(current.xCoord- bs/2, current.yCoord-1, current.zCoord-bs/2, current.xCoord +bs/2, current.yCoord, current.zCoord +bs/2);
			PlayerAI_PathNode temp;
			PlayerAI_PathNode temp2;
			PlayerAI_PathNode temp3;
			if(mc.theWorld.isAABBNonEmpty(AABB2) || distToDest(current) <= bs)
			{
				temp = new PlayerAI_PathNode(current.xCoord+1,current.yCoord,current.zCoord, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				checkNodes(temp, temp2, temp3, current);
				
				temp = new PlayerAI_PathNode(current.xCoord-1,current.yCoord,current.zCoord, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				checkNodes(temp, temp2, temp3, current);
				
				temp = new PlayerAI_PathNode(current.xCoord,current.yCoord,current.zCoord+1, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				checkNodes(temp, temp2, temp3, current);
				
				temp = new PlayerAI_PathNode(current.xCoord,current.yCoord,current.zCoord-1, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				checkNodes(temp, temp2, temp3, current);
			}
			else if(mc.theWorld.isAABBNonEmpty(AABB) == false) {
				temp = new PlayerAI_PathNode(current.xCoord+1 +bs,current.yCoord,current.zCoord, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp.size = 3;
				temp2.size = 3;
				temp3.size = 3;
				checkNodes(temp, temp2, temp3, current);
				
				temp = new PlayerAI_PathNode(current.xCoord-1-bs,current.yCoord,current.zCoord, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp.size = 3;
				temp2.size = 3;
				temp3.size = 3;
				checkNodes(temp, temp2, temp3, current);
				
				temp = new PlayerAI_PathNode(current.xCoord,current.yCoord,current.zCoord+1+bs, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp.size = 3;
				temp2.size = 3;
				temp3.size = 3;
				checkNodes(temp, temp2, temp3, current);
				
				temp = new PlayerAI_PathNode(current.xCoord,current.yCoord,current.zCoord-1-bs, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp.size = 3;
				temp2.size = 3;
				temp3.size = 3;
				checkNodes(temp, temp2, temp3, current);
			}
			else {
				temp = new PlayerAI_PathNode(current.xCoord+1 +bs/2,current.yCoord,current.zCoord, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp.size = 2;
				temp2.size = 2;
				temp3.size = 2;
				checkNodes(temp, temp2, temp3, current);
				
				temp = new PlayerAI_PathNode(current.xCoord-1-bs/2,current.yCoord,current.zCoord, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp.size = 2;
				temp2.size = 2;
				temp3.size = 2;
				checkNodes(temp, temp2, temp3, current);
				
				temp = new PlayerAI_PathNode(current.xCoord,current.yCoord,current.zCoord+1+bs/2, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp.size = 2;
				temp2.size = 2;
				temp3.size = 2;
				checkNodes(temp, temp2, temp3, current);
				
				temp = new PlayerAI_PathNode(current.xCoord,current.yCoord,current.zCoord-1-bs/2, current.destx, current.desty, current.destz);
				temp2 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord + 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp3 = new PlayerAI_PathNode(temp.xCoord,temp.yCoord - 1,temp.zCoord, temp.destx, temp.desty, temp.destz);
				temp.size = 2;
				temp2.size = 2;
				temp3.size = 2;
				checkNodes(temp, temp2, temp3, current);
			}
		}
		openNodes.clear();
		closedNodes.clear();
		return;
	}
	public boolean inClosed(PlayerAI_PathNode node){
		Iterator<PlayerAI_PathNode> iter1 = closedNodes.iterator();
		for (int i = 0; i < closedNodes.size(); i++){
			PlayerAI_PathNode tempclosed = iter1.next();
			if(tempclosed.equals(node)){
				return true;
			}
		}
		return false;
	}
	public boolean inOpen(PlayerAI_PathNode node){
		Iterator<PlayerAI_PathNode> iter1 = openNodes.iterator();
		for (int i = 0; i < openNodes.size(); i++){
			PlayerAI_PathNode tempopen = iter1.next();
			if(tempopen.equals(node)){
				tempnode = tempopen;
				return true;
			}
		}
		return false;
	}
	public void constructPath(){
		pathNodes.clear();
		PlayerAI_PathNode temp = closedNodes.peek();
		do{
			pathNodes.offerFirst(temp);
			temp = temp.parent;
		}while(temp != null);
		pathNodes.pollFirst();
		openNodes.clear();
		closedNodes.clear();
	}
	public boolean isAir(int x, int y, int z){
		return mc.theWorld.isAirBlock(x, y, z);
	}
	private void checkNodes(PlayerAI_PathNode temp, PlayerAI_PathNode temp2, PlayerAI_PathNode temp3, PlayerAI_PathNode current){
		int additionalCost = 1;
		if(Math.abs(temp.xCoord - current.xCoord) > Math.abs(temp.zCoord - current.zCoord)){
			additionalCost = Math.abs(temp.xCoord - current.xCoord);
		}
		else{
			additionalCost = Math.abs(temp.zCoord - current.zCoord);
		}
		System.out.println(additionalCost);
		if(isAir(temp.xCoord, temp.yCoord-1, temp.zCoord) == false && isAir(temp.xCoord, temp.yCoord, temp.zCoord) == false){
			//Do Nothing
		}
		else if(isAir(temp.xCoord, temp.yCoord-1, temp.zCoord) == true && isAir(temp.xCoord, temp.yCoord, temp.zCoord) == false){
			//Do Nothing
		}
		else if(isAir(temp.xCoord, temp.yCoord-1, temp.zCoord) == false && isAir(temp.xCoord, temp.yCoord, temp.zCoord) == true && (isAir(temp.xCoord, temp.yCoord+1, temp.zCoord) == false || isAir(current.xCoord, current.yCoord+1, current.zCoord) == false)){
			//Do Nothing
		}
		else if(isAir(temp.xCoord, temp.yCoord-1, temp.zCoord) == false && isAir(temp.xCoord, temp.yCoord, temp.zCoord) && isAir(temp.xCoord, temp.yCoord+1, temp.zCoord) && inClosed(temp2) == false){
			if(inOpen(temp2)){
				if(current.costSoFar + additionalCost <= tempnode.costSoFar){
					tempnode.parent = current;
					tempnode.jumpneeded = true;
					tempnode.costSoFar = current.costSoFar + additionalCost;
				}
			}
			else{
				temp2.parent = current;
				temp2.costSoFar = current.costSoFar + additionalCost;
				temp2.jumpneeded = true;
				openNodes.offerFirst(temp2);
			}
			if(openNodes.isEmpty()){
				temp2.jumpneeded = true;
				openNodes.offerFirst(temp2);
			}
		}
		else if(isAir(temp.xCoord, temp.yCoord-2, temp.zCoord) == true && isAir(temp.xCoord, temp.yCoord-1, temp.zCoord) == true && isAir(temp.xCoord, temp.yCoord, temp.zCoord) == true && inClosed(temp3) == false){
			if(inOpen(temp3)){
				if(current.costSoFar + additionalCost <= tempnode.costSoFar){
					tempnode.parent = current;
					tempnode.jumpneeded = false;
					tempnode.isFalling = true;
					tempnode.costSoFar = current.costSoFar + additionalCost;
				}
			}
			else{
				temp3.parent = current;
				temp3.costSoFar = current.costSoFar + additionalCost;
				temp3.jumpneeded = false;
				temp3.isFalling = true;
				openNodes.offerFirst(temp3);
			}
			if(openNodes.isEmpty()){
				openNodes.offerFirst(temp3);
			}
		}
		
		else if(inClosed(temp) == false){
			if(inOpen(temp))
			{
				if(current.costSoFar + additionalCost <= tempnode.costSoFar){
					tempnode.jumpneeded = false;
					tempnode.parent = current;
					tempnode.costSoFar = current.costSoFar + additionalCost;
				}
			}
			else
			{
				temp.parent = current;
				temp.costSoFar = current.costSoFar + additionalCost;
				openNodes.offerFirst(temp);
			}
			if(openNodes.isEmpty()){
				openNodes.offerFirst(temp);
			}
		}
	}

}