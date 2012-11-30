package com.wordsaretoys.thread;

import android.opengl.GLES20;
import android.util.Log;

import com.wordsaretoys.soar.Camera;
import com.wordsaretoys.soar.GLObject;
import com.wordsaretoys.soar.Vector;

public class Path extends GLObject implements Runnable {

	private final double UPDATE_DISTANCE = 2.0;

	private Camera camera;
	private Textures textures;
	
	private Road road;
	private Cliff cliff;
	private Brush brush;
	private Rocks rocks;

	private Thread updateThread;
	private boolean updateGL = false;	
	
	public Path(Camera camera, Textures textures, World world) {
		this.textures = textures;
		this.camera = camera;

		cliff = new Cliff(camera, textures, world);
		road = new Road(camera, textures, world);
		brush = new Brush(Main.rng.nextLong(), camera, textures, world);
		rocks = new Rocks(Main.rng.nextLong(), camera, textures, world);
	}

	/**
	 * generate GL objects and kick off update thread
	 */
	public void onInit() {
		GLES20.glClearDepthf(1.0f);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		
		GLES20.glClearColor(0.75f, 0.75f, 0.75f, 1.0f);
		
		textures.makeGL();
		
		road.makeGL();
		cliff.makeGL();
		brush.makeGL();
		rocks.makeGL();
		
		updateThread = new Thread(this);
		updateThread.start();
		
		Log.i("Thread", "GL thread started.");
	}
	
	/**
	 * update/draw any GL objects that require it
	 */
	public void onUpdate() {

		if (updateGL) {
			road.updateGL();
			cliff.updateGL();
			brush.updateGL();
			rocks.updateGL();
			updateGL = false;
			Log.i("Thread", "GL thread updated");
		}
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		road.drawGL();
		cliff.drawGL();
		brush.drawGL();
		rocks.drawGL();

	}

	/**
	 * worker thread procedure, handles updates to meshes
	 * note that it's NOT running in the GL thread context,
	 * and must raise a signal to update the actual GL stuff
	 */
	public void run() {
		Vector ppos = camera.position;
		Vector lastUpdate = new Vector(1000, 1000, 1000);
		boolean running = true;
		while(running) {
			// time to regenerate the meshes?
			if (lastUpdate.distance(ppos) > UPDATE_DISTANCE) {
				double z = Math.floor(ppos.z);
				
				road.update(z);
				cliff.update(z);
				brush.update(z);
				rocks.update(z);
				
				lastUpdate.copy(ppos);
				updateGL = true;
				
				Log.i("Thread", "Worker thread updated.");

			}

			// sleep unless interrupted
			// (main thread will interrupt if paused)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				running = false;
			}
		}
	}
	
	public void stopThread() {
		updateThread.interrupt();
	}

}
