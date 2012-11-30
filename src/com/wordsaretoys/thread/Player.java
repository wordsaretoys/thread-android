package com.wordsaretoys.thread;

import com.wordsaretoys.soar.Camera;
import com.wordsaretoys.soar.GLObject;
import com.wordsaretoys.soar.Vector;

public class Player extends GLObject {

	final private double HEIGHT = 0.25;
	final private double TOP_SPEED = 0.025;
	
	private Camera camera;
	private World world;

	private double speed;
	private double lookX;
	private double lookY;
	
	private Vector direction = new Vector();

	public Player(Camera camera, World world) {
		this.camera = camera;
		this.world = world;
	}
	
	public void onInit() {
		camera.nearLimit = 0.01;
		camera.farLimit = 200;
		camera.free = false;
		camera.bound.set(Math.sqrt(2) / 2, -1, 0);
		
		double y = world.roadHeight(0);
		double x = world.cliffSurface(y, 0);
		camera.position.set(x, y + HEIGHT, 0);
	}
	
	public void onUpdate() {
		double dt = Main.display.interval;
		Vector pos = camera.position;
        
		camera.turn(lookY * dt, 2 * lookX * dt, 0);
        
		if (speed > 0) {
	        direction.copy(camera.front).mul(speed * TOP_SPEED);
	        pos.add(direction);
	        
			pos.y = HEIGHT + world.roadHeight(pos.z);
			double s = world.cliffSurface(pos.y, pos.z);
			if (pos.x > s + 0.4) {
				pos.x = s + 0.4;
			}
			if (pos.x < s - 0.5) {
				pos.x = s - 0.5;
			}
		}
	}
	
	public void setMotion(double s) {
		speed = s;
	}
	
	public void setLooking(double dx, double dy) {
		lookX = dx;
		lookY = dy;
	}

	public void test() {
		camera.position.z += 100;
	}
	
	public void onResize() {
        camera.setViewport(Main.display.width, Main.display.height);
	}
	
}
