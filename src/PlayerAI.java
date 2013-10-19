package net.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.Block;
import net.minecraft.client.PlayerAI_Path;
import net.minecraft.src.GuiInventory;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class PlayerAI {
	public boolean pressed = false;
	public int presstime = 0;
	Robot robot;
	private Minecraft mc;
	private boolean moveX = true;
	private boolean moveZ = true;
	private boolean destchanged = true;
	private double moveRatio = 0;
	private boolean wasInWater = false;
	private int xpos = 1;
	private int zpos = 1;
	private int destx;
	private int desty;
	private int destz;
	private boolean xposchanged = false;
	private boolean checkpoint = true;
	private float rotationYaw;
	private float rotationPitch;
	private Entity closestAnimal;
	private Entity closestSlime;
	private Entity closestZombie;
	private Entity closestCreeper;
	private Entity closestSkeleton;
	private Entity closestSpider;
	private Entity closestMob;
	private Class watchedClass;
	public PlayerAI_Path path;
	private int jumpcounter = 0;
	public boolean pathmade = false;
	private int playerX;
	private int playerY;
	private int playerZ;
	private AxisAlignedBB playerBB;
	private int bs;
	private int pathcounter = 0;

	public PlayerAI() {
		path = new PlayerAI_Path();
		try {
		robot = new Robot();
		} catch(Exception e) {
			//NULL
		}
	}
	
	public void runAI(Minecraft mc) {
		/* CONSTRUCTOR FOR MC. DO NOT REMOVE! */
		if(this.mc == null)
		{
			this.mc = mc;
		}
		playerX = (int)mc.thePlayer.posX;
		playerY = (int)mc.thePlayer.posY;
		playerZ = (int)mc.thePlayer.posZ;
		bs = 5;
		/* CONSTRUCTOR FOR MC ENDS. */
		updateClosestEntity();
		updatePathMC();
		if(pathmade == false){
			destx = getXDest();
			destz = getZDest();
			desty = getYDest();
			
			//System.out.println(destx + ", " + desty + ", " + destz);
			pathmade = true;
			path.makePath(destx, desty, destz);
			//System.out.println(mc.theWorld.isAirBlock(path.peek().xCoord, path.peek().yCoord-1, path.peek().zCoord));
			//System.out.println(path.pathNodes.size());
		}
		if(path.pathNodes.size() != 0 && playerY < path.peek().yCoord && pathcounter++ >= 180){
			path.makePath(destx, desty, destz);
			pathcounter = 0;
		}
		else if(path.pathNodes.size() != 0 && playerY >= path.peek().yCoord){
			pathcounter = 0;
		}
		if(path.pathNodes.isEmpty() == false){
			move(path.peek().xCoord,path.peek().zCoord,path.peek().yCoord,true);
			if((int)mc.thePlayer.posX == path.peek().xCoord && (int)mc.thePlayer.posZ == path.peek().zCoord ){
				path.pop();
			}
		}
		if(destx != getXDest() || destz != getZDest() || desty != getYDest()){
			pathmade = false;
		}
	}
	private int getXDest(){
		int returnval;
		return 685;
		/*
		if(closestMob != null){
			if(closestMob.posX > closestMob.prevPosX){
				returnval = playerX - 10;
			}
			else{
				returnval = playerX + 10;
			}
		}
		else if(closestAnimal != null){
			returnval = (int)closestAnimal.posX;
		}
		else{
			returnval = playerX;
		}
		return returnval;
		*/
	}
	private int getYDest(){
		int returnval;
		return mc.theWorld.getTopSolidOrLiquidBlock(destx, destz);
		/*
		if(closestMob != null){
			returnval = mc.theWorld.getTopSolidOrLiquidBlock(destx, destz);
		}
		else if(closestAnimal != null){
			returnval = (int)closestAnimal.posY + 1;
		}
		else{
			returnval = playerY;
		}
		return returnval;
		*/
	}
	private int getZDest(){
		int returnval;
		return 186;
		/*
		if(closestMob != null){
			if(closestMob.posZ > closestMob.prevPosZ){
				returnval = playerZ - 10;
			}
			else{
				returnval = playerZ + 10;
			}
		}
		else if(closestAnimal != null){
			returnval = (int)closestAnimal.posZ;
		}
		else{
			returnval = playerZ;
		}
		return returnval;
		*/
	}
	public void updatePathMC() {
		path.updateMC(mc);
	}
	public void updateClosestEntity() { // Find the nearest enemy entity (entities).
		closestMob = mc.thePlayer.worldObj.findNearestEntityWithinAABB(EntityMob.class, mc.thePlayer.boundingBox.expand(8.0D, 3.0D, 8.0D), mc.thePlayer);
		if (closestMob != null){
			//System.out.println(closestMob.posX);
		}
		closestAnimal = mc.thePlayer.worldObj.findNearestEntityWithinAABB(EntityAnimal.class, mc.thePlayer.boundingBox.expand(8.0D, 3.0D, 8.0D), mc.thePlayer);
		if (closestAnimal != null){
			//System.out.println(closestAnimal.posX);
		}
		closestSlime = mc.thePlayer.worldObj.findNearestEntityWithinAABB(EntitySlime.class, mc.thePlayer.boundingBox.expand(8.0D, 3.0D, 8.0D), mc.thePlayer);
		if (closestSlime != null){
			//System.out.println(closestSlime.posX);
		}
		if (closestMob != null)
		{
			closestCreeper = mc.thePlayer.worldObj.findNearestEntityWithinAABB(EntityCreeper.class, mc.thePlayer.boundingBox.expand(8.0D, 3.0D, 8.0D), mc.thePlayer);
			if (closestCreeper != null){
				//System.out.println(closestCreeper.posX);
			}
			closestZombie = mc.thePlayer.worldObj.findNearestEntityWithinAABB(EntityZombie.class, mc.thePlayer.boundingBox.expand(8.0D, 3.0D, 8.0D), mc.thePlayer);
			if (closestZombie != null){
				//System.out.println(closestZombie.posX);
			}
			closestSkeleton = mc.thePlayer.worldObj.findNearestEntityWithinAABB(EntitySkeleton.class, mc.thePlayer.boundingBox.expand(8.0D, 3.0D, 8.0D), mc.thePlayer);
			if (closestSkeleton != null){
				//System.out.println(closestSkeleton.posX);
			}
			closestSpider = mc.thePlayer.worldObj.findNearestEntityWithinAABB(EntitySpider.class, mc.thePlayer.boundingBox.expand(8.0D, 3.0D, 8.0D), mc.thePlayer);
			if (closestSpider != null){
				//System.out.println(closestSpider.posX);
			}
		}
	}
	public void lookAt(double XCoord, double ZCoord, double YCoord, boolean dest) {
		double x = getPlayerX();
		double z = getPlayerZ();
		double y = getPlayerY();
		
		ZCoord -= 0.5;
		XCoord -= 0.5;
		
		double diffy = y - YCoord + 0.0;
		double diffx = x - XCoord + 0.5;
		double diffz = z - ZCoord + 0.5;

		double hofxz = Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffz, 2));
		double hofxyz = Math.sqrt(Math.pow(diffy, 2) + Math.pow(hofxz, 2));
	
		if (dest) {
			rotationPitch = (float) (Math.asin(diffy/hofxyz)*(180/Math.PI));
			if(z < ZCoord){
				rotationYaw = (float) -(Math.atan(diffx/diffz)*(180/Math.PI));
			}
			else {
				rotationYaw = (float) (180-(Math.asin(diffx/hofxz)*(180/Math.PI)));
			}
		}
		else {
			rotationPitch = (float) 0;
			if(xpos == 1 && xposchanged){
				rotationYaw = (float)270;
			}
			else if(xpos == -1 && xposchanged){
				rotationYaw = (float)90;
			}
			else if(zpos == 1 && !xposchanged){
				rotationYaw = (float)0;
			}
			else if(zpos == -1 && !xposchanged){
				rotationYaw = (float)180;
			}
		}
		this.mc.thePlayer.rotationPitch = rotationPitch;
		this.mc.thePlayer.rotationYaw = rotationYaw;
	}
	public boolean move(int d, int e, int YCoord, boolean dest) {
		/* USE THIS MOVERATIO CALCULATION SOMEWHERE */
		if (moveX == true && moveZ == false) {
			moveRatio = 1;
		}
		else if (moveZ == true && moveX == false) {
			moveRatio = 0;
		}
		else if (moveZ == true && moveX == true) {
			moveRatio = .5;
		}
		else {
			moveRatio = 0;
		}
		/* END OF MOVERATIO CALCULATION */
		moveX = moveX(d, moveRatio);
		moveZ = moveZ(e, 1 - moveRatio);
		if(path.peek().jumpneeded == true){
			path.peek().toString();
			robot.keyPress(KeyEvent.VK_SPACE);
			jumpcounter = 0;
		}
		else if(jumpcounter == 5){
			robot.keyRelease(KeyEvent.VK_SPACE);
		}
		else {
			jumpcounter++;
		}
		lookAt(path.peek().xCoord, path.peek().zCoord, path.peek().desty-2, path.peek().isDestination());
		
		return (moveX || moveZ);
	}
	public boolean moveX(int d, double ratio) {
		if(Math.abs(getPlayerX() - d) > .3) {
			if(getPlayerX() < d) {
				xposchanged = true;
				xpos = 1;
			}
			else {
				xposchanged = true;
				xpos = -1;
			}
			if (mc.thePlayer.isInWater() && d != getPlayerX()) {
				robot.keyPress(KeyEvent.VK_SPACE);
				mc.thePlayer.motionX = 0.075F * xpos;
				wasInWater = true;
			}
			else
			{
				if(wasInWater) {
					robot.keyRelease(KeyEvent.VK_SPACE);
					wasInWater = false;
				}
				if (d != getPlayerX() && mc.thePlayer.onGround == true){
					mc.thePlayer.motionX = 0.22F * (ratio+0.2) * xpos;
				}
				else if (d != getPlayerX()) {
					mc.thePlayer.motionX = 0.1F * xpos;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	public boolean moveZ(int e, double ratio) {
		if(Math.abs(getPlayerZ() - e) > .3) {
			if(getPlayerZ() < e) {
				xposchanged = false;
				zpos = 1;
			}
			else {
				xposchanged = false;
				zpos = -1;
			}
			if (mc.thePlayer.isInWater()) {
				robot.keyPress(KeyEvent.VK_SPACE);
				mc.thePlayer.motionZ = 0.075F * zpos;
				wasInWater = true;
			}
			else
			{
				if(wasInWater) {
					robot.keyRelease(KeyEvent.VK_SPACE);
					wasInWater = false;
				}
				if (e != getPlayerZ() && mc.thePlayer.onGround == true){
					mc.thePlayer.motionZ = 0.22F * (ratio+0.2) * zpos;
				}
				else if (e != getPlayerZ()) {
					mc.thePlayer.motionZ = 0.1F * zpos;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	public void moveY(double YCoord){
		if(Math.abs(getPlayerY() - YCoord) > .2) {
			if (getPlayerY() > YCoord){
				//go down
			}
			else if(getPlayerY() < YCoord) {
				//jump();
			}
		}
	}
	public int getPlayerX() {
		return playerX;
	}
	public int getPlayerY() {
		return playerY;
	}
	public int getPlayerZ() {
		return playerZ;
	}
	public void jump() {
		if(mc.thePlayer.onGround == true)
		   {
			   mc.thePlayer.jump();
		   }	
	}
	
	public void clickAttack() {
		if(mc.inGameHasFocus){
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
	}
	
	public void clickPick() {
		if(mc.inGameHasFocus){
			robot.mousePress(InputEvent.BUTTON1_MASK);
		}
	}
	public void unClickPick() {
		if(mc.inGameHasFocus){
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
	}
	public void clickBlock() {
		if(mc.inGameHasFocus){
			robot.mousePress(InputEvent.BUTTON2_MASK);
		}
	}
	public void unBlock() {
		if(mc.inGameHasFocus){
			robot.mouseRelease(InputEvent.BUTTON2_MASK);
		}
	}
}