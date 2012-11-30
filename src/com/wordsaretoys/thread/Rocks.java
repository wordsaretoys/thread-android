package com.wordsaretoys.thread;

import android.opengl.GLES20;

import com.wordsaretoys.soar.Camera;
import com.wordsaretoys.soar.Mesh;
import com.wordsaretoys.soar.Random;
import com.wordsaretoys.soar.Shader;
import com.wordsaretoys.soar.Vector;

public class Rocks {

	private long seed;
	
	private Random rng;
	
	private Camera camera;
	private Textures textures;
	private World world;
	
	private Shader shader;
	private Mesh mesh;
	private Mesh.Iterator2D iter;
	
	private int modelviewId;
	private int projectorId;
	private int walk0Id;
	private int walk1Id;
	
	public Rocks(long seed, Camera camera, Textures textures, World world) {
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
        	Shader.getText(Main.display.context, R.raw.fs_materials) +
        	Shader.getText(Main.display.context, R.raw.fs_fog) +
        	Shader.getText(Main.display.context, R.raw.fs_rocks)
        );

        modelviewId = shader.getUniformId("modelview");
        projectorId = shader.getUniformId("projector");
        walk0Id = shader.getUniformId("walk0");
        walk1Id = shader.getUniformId("walk1");
        
        mesh = new Mesh();
        mesh.add(shader.getAttributeId("position"), 3);
        mesh.add(shader.getAttributeId("texture"), 2);
        mesh.retain = true;
        
        iter = new Mesh.Iterator2D(mesh, 6, 6, false);

	}
	
	public void update(double pz) {
		mesh.reset();
		Vector o = new Vector();
		int i, j;
		double iz, x, y, z, r, s;
		double tx, ty;
		for (i = -8; i < 8; i++) {
			iz = pz + i;
			// same random seed for each cell--though not
			// the same as the brush, or rocks would overlap!
			rng.reseed((long)Math.abs(iz * seed + 2));
			// twenty rocks per cell
			for (j = 0; j < 20; j++) {
				s = rng.get() < 0.5 ? -1 : 1;
				z = iz + rng.get(0, 1);
				y = world.roadHeight(z) - 0.005;
				x = world.cliffSurface(y, z) + s * (0.5 - rng.get(0.02, 0.25));
				r = rng.get(0.01, 0.03);
				tx = rng.get(0, 5);
				ty = rng.get(0, 5);
				// each rock is an upturned half-sphere
				iter.reset();
				while(!iter.done()) {
					double xr = iter.ir;
					double zr = iter.jr;
					o.x = 2 * (xr - 0.5);
					o.z = 2 * (zr - 0.5);
					o.y = (1 - o.x * o.x) * (1 - o.z * o.z);
					o.norm().mul(r);
					mesh.set(x + o.x, y + o.y, z + o.z, xr + tx, zr + ty);
					iter.next();
				}
				
			}
		}
	}
	
	public void updateGL() {
		mesh.build();
	}
	
	public void drawGL() {
		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);
        shader.activate();
        GLES20.glUniformMatrix4fv(modelviewId, 1, false, camera.modelview, 0);
        GLES20.glUniformMatrix4fv(projectorId, 1, false, camera.projector, 0);
        textures.walk0.bind(0, walk0Id);
        textures.walk1.bind(0, walk1Id);
        mesh.draw();
	}
}
