package com.wordsaretoys.thread;

import android.opengl.GLES20;

import com.wordsaretoys.soar.Camera;
import com.wordsaretoys.soar.Mesh;
import com.wordsaretoys.soar.Random;
import com.wordsaretoys.soar.Shader;

public class Brush {

	private long seed;
	
	private Random rng;
	
	private Camera camera;
	private Textures textures;
	private World world;
	
	private Shader shader;
	private Mesh mesh;
	
	private int modelviewId;
	private int projectorId;
	private int walk0Id;
	private int stem0Id;
	
	public Brush(long seed, Camera camera, Textures textures, World world) {
		rng = new Random();

		this.seed = seed;
		this.camera = camera;
		this.textures = textures;
		this.world = world;
	}
	
	public void makeGL() {
		shader = new Shader();
        shader.build(
        	Shader.getText(Main.display.context, R.raw.vs_general),
        	Shader.getText(Main.display.context, R.raw.fs_fog) +
        	Shader.getText(Main.display.context, R.raw.fs_brush)
        );

        modelviewId = shader.getUniformId("modelview");
        projectorId = shader.getUniformId("projector");
        walk0Id = shader.getUniformId("walk0");
        stem0Id = shader.getUniformId("stem0");
        
        mesh = new Mesh();
        mesh.add(shader.getAttributeId("position"), 3);
        mesh.add(shader.getAttributeId("texture"), 2);
        mesh.retain = true;
	}
	
	public void update(double pz) {
		mesh.reset();
		int i, j, k;
		double iz, x, y, z, a, r, s;
		// 16 cells because path/cliff is 16 units long in z-direction
		for (i = -8; i < 8; i++) {
			iz = pz + i;
			// same random seed for each cell
			rng.reseed((long)Math.abs(iz * seed + 1));
			// place 25 bits of brush at random positions/sizes
			for (j = 0; j < 25; j++) {
				s = rng.get() < 0.5 ? -1 : 1;
				z = iz + rng.get(0, 1);
				y = world.roadHeight(z) - 0.0025;
				x = world.cliffSurface(y, z) + s * (0.5 - rng.get(0, 0.15));
				r = rng.get(0.01, 0.1);
				a = rng.get(0, Math.PI);
				// each brush consists of 4 triangles
				// rotated around the center point
				for (k = 0; k < 4; k++) {
					mesh.set(x, y, z, 0, 0);
					mesh.set(x + r * Math.cos(a), y + r, z + r * Math.sin(a), -1, 1);
					a = a + Math.PI * 0.5;
					mesh.set(x + r * Math.cos(a), y + r, z + r * Math.sin(a), 1, 1);
				}
			}
		}
	}
	
	public void updateGL() {
		mesh.build();
	}
	
	public void drawGL() {
		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glDisable(GLES20.GL_CULL_FACE);
        shader.activate();
        GLES20.glUniformMatrix4fv(modelviewId, 1, false, camera.modelview, 0);
        GLES20.glUniformMatrix4fv(projectorId, 1, false, camera.projector, 0);
        textures.walk0.bind(0, walk0Id);
        textures.stem0.bind(0, stem0Id);
        mesh.draw();
	}
}
