package com.wordsaretoys.thread;

import android.opengl.GLES20;

import com.wordsaretoys.soar.Camera;
import com.wordsaretoys.soar.Mesh;
import com.wordsaretoys.soar.Shader;

/**
 * maintains road surface model and
 * GL objects representing it
 * 
 * @author chris
 *
 */
public class Road {
	
	private Camera camera;
	private Shader shader;
	private World world;
	
	private Mesh mesh;
	private Textures textures;
	private Mesh.Iterator2D iter;
	
	private int modelviewId;
	private int projectorId;
	private int walk0Id;
	private int walk1Id;
	private int line0Id;
	private int cros0Id;
	
	/**
	 * constructor, generates road model
	 * @param camera player camera
	 * @param textures textures object
	 */
	public Road(Camera camera, Textures textures, World world) {
		this.camera = camera;
		this.textures = textures;
		this.world = world;
	}
	
	/**
	 * generates GL objects
	 */
	public void makeGL() {
		shader = new Shader();
        shader.build(
        	Shader.getText(Main.display.context, R.raw.vs_general),
        	Shader.getText(Main.display.context, R.raw.fs_materials) +
        	Shader.getText(Main.display.context, R.raw.fs_fog) +
        	Shader.getText(Main.display.context, R.raw.fs_road)
        );

        modelviewId = shader.getUniformId("modelview");
        projectorId = shader.getUniformId("projector");
        walk0Id = shader.getUniformId("walk0");
        walk1Id = shader.getUniformId("walk1");
        line0Id = shader.getUniformId("line0");
        cros0Id = shader.getUniformId("cros0");
        
        mesh = new Mesh();
        mesh.add(shader.getAttributeId("position"), 3);
        mesh.add(shader.getAttributeId("texture"), 2);
        mesh.retain = true;

        iter = new Mesh.Iterator2D(mesh, 2, 64, false);

	}
	
	public void update(double pz) {
		mesh.reset();
		iter.reset();
		
		while(!iter.done()) {
			double xr = iter.ir;
			double zr = iter.jr;
			double z = pz + (zr - 0.5) * 16;
			double y = world.roadHeight(z);
			double x = world.cliffSurface(y, z) + xr - 0.5;
			mesh.set(x, y, z, xr, z % 1);
			iter.next();
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
        textures.lines.bind(0, line0Id);
        textures.cross.bind(0, cros0Id);
        mesh.draw();
	}
}
