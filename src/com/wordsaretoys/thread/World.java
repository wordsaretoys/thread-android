package com.wordsaretoys.thread;

import com.wordsaretoys.soar.Line;
import com.wordsaretoys.soar.Pattern;
import com.wordsaretoys.soar.Surface;

public class World {

	private Line line;
	
	private Surface surf0;
	private Surface surf1;
	private Surface surf2;
	
	public World() {
		// create road height object
		line = new Line(128, 6, 0.05);
		Pattern.randomize(line, 0, 0, 1);

		// create cliff surface objects
		surf0 = new Surface(32, 32, 10, 0.2, 0.4);
		Pattern.walk(surf0, 0, 8, 0.05, 1, 0.5, 0.5, 0.5, 0.5);
		Pattern.normalize(surf0, 0, 1);
		
		surf1 = new Surface(64, 64, 5, 0.5, 1.0);
		Pattern.walk(surf1, 0, 8, 0.05, 1, 0.5, 0.5, 0.5, 0.5);
		Pattern.normalize(surf0, 0, 1);

		surf2 = new Surface(128, 127, 1, 0.75, 1.5);
		Pattern.walk(surf2, 0, 8, 0.05, 1, 0.5, 0.5, 0.5, 0.5);
		Pattern.normalize(surf0, 0, 1);
	}
	
	/**
	 * returns road height
	 * @param z player z-position
	 * @return height of road at z
	 */
	public double roadHeight(double z) {
		return line.get(z);
	}

	/**
	 * returns cliff surface
	 * @param y, z player y, z position
	 * @return cliff x position
	 */
	public double cliffSurface(double y, double z) {
		return surf0.get(y, z) + surf1.get(y, z) + surf2.get(y, z);
	}

}
