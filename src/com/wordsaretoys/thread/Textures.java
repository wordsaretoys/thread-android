package com.wordsaretoys.thread;

import com.wordsaretoys.soar.Pattern;
import com.wordsaretoys.soar.Surface;
import com.wordsaretoys.soar.Texture;

/**
 * maintains all generated textures
 * @author chris
 *
 */
public class Textures {

	public Texture walk0;
	public Texture walk1;
	public Texture lines;
	public Texture cross;
	public Texture stem0;

	private Surface swalk0;
	private Surface swalk1;
	private Surface slines;
	private Surface scross;
	private Surface sstem0;
	
	/**
	 * constructor, generates bitmaps
	 * instantiate outside GL thread at app start
	 */
	public Textures() {
		swalk0 = new Surface(64, 64);
		Pattern.walk(swalk0, Main.rng.nextLong(), 12, 0.05, 1, 0.5, 0.5, 0.5, 0.5);
		Pattern.normalize(swalk0, 0, 1);
		
		swalk1 = new Surface(64, 64);
		Pattern.walk(swalk1, Main.rng.nextLong(), 12, 0.05, 1, 0.5, 0.5, 0.5, 0.5);
		Pattern.normalize(swalk1, 0, 1);
		
		slines = new Surface(64, 64);
		Pattern.walk(slines, Main.rng.nextLong(), 12, 0.05, 1, 0.03, 0.95, 0.04, 0.85);
		Pattern.normalize(slines, 0, 1);
		
		scross = new Surface(64, 64);
		Pattern.walk(scross, Main.rng.nextLong(), 12, 0.025, 1, 0.03, 0.95, 0.04, 0.85);
		Pattern.walk(scross, Main.rng.nextLong(), 12, 0.025, 1, 0.95, 0.03, 0.85, 0.04);
		Pattern.normalize(scross, 0, 1);

		sstem0 = new Surface(64, 64);
		for (int i = 0; i < 100; i++) {
			int x = (int)Main.rng.get(0, 64);
			Pattern.scratch(sstem0, 0.5, 1, x, 0, 0, 1, 64);
		}
		Pattern.normalize(sstem0, 0, 1);
	}
	
	/**
	 * generates textures
	 * call when GL thread is (re)initialized
	 */
	public void makeGL() {
		walk0 = new Texture();
		walk0.build(swalk0);
		
		walk1 = new Texture();
		walk1.build(swalk1);
		
		lines = new Texture();
		lines.build(slines);
		
		cross = new Texture();
		cross.build(scross);
		
		stem0 = new Texture();
		stem0.build(sstem0);
	}
}
