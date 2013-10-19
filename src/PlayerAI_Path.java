package net.minecraft.client;

import java.util.LinkedList;

import net.minecraft.client.PlayerAI_PathNode;
import net.minecraft.client.PlayerAI_PathFinder;

public class PlayerAI_Path {
	/** Minecraft World */
	private Minecraft mc;
	/** List of points */
	public LinkedList<PlayerAI_PathNode> pathNodes = new LinkedList<PlayerAI_PathNode>();
	/** Whether or not we already began this path (to gauge interruptions) */
	public boolean alreadyStarted = false;
	/** Number of points in path */
	private int count = 0;
	/** If we are at the final destination point or not */
	private boolean atDestination = false;
	/** Total distance traveled on this path */
	private double distanceTraveled;
	/** Distance to the next (head) point */
	private double distanceToNextPoint;
	/** Estimated distance to destination */
	private double distanceToDest;
	public boolean generated = false;
	
	private PlayerAI_PathFinder finder = new PlayerAI_PathFinder();

	public PlayerAI_Path() {
		// TODO Auto-generated constructor stub
	}
	public void updateMC(Minecraft mcin){
		mc = mcin;
	}
	public void makePath(int x, int y, int z){
		finder.updateMC(mc, x, y, z);
		this.pathNodes = finder.pathNodes;
		generated = true;
	}
	public void push(PlayerAI_PathNode node){
		pathNodes.offer(node);
		count++;
	}
	public PlayerAI_PathNode pop(){
		count--;
		if(count < 0){
			count = 0;
		}
		System.out.println("Pop!");
		return pathNodes.pollFirst();
	}
	public PlayerAI_PathNode peek(){
		return pathNodes.peek();
	}
	public void forceToFront(PlayerAI_PathNode node){
		count++;
		pathNodes.offerFirst(node);
	}
	public boolean isEmpty() {
		if (count == 0)
			return true;
		else
			return false;
	}
	public boolean atDestination(){
		return atDestination;
	}
}
