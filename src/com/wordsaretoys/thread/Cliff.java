package com.wordsaretoys.thread;

import android.opengl.GLES20;

import com.wordsaretoys.soar.Camera;
import com.wordsaretoys.soar.Mesh;
import com.wordsaretoys.soar.Shader;

/**
 * maintains cliff surface model,
 * and GL objects representing it
 * 
 * @author chris
 *
 */
public class Cliff {

	private Camera camera;
	private Textures textures;
	private World world;
	
	private Shader shader;
	private Mesh mesh;
	private Mesh.Iterator2D uIter;
	private Mesh.Iterator2D lIter;
	
	private int modelviewId;
	private int projectorId;
	private int walk0Id;
	private int walk1Id;
	private int line0Id;
	private int cros0Id;

	public Cliff(Camera camera, Textures textures, World world) {
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
        	Shader.getText(Main.display.context, R.raw.fs_cliff)
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

        uIter = new Mesh.Iterator2D(mesh, 32, 64, false);
        lIter = new Mesh.Iterator2D(mesh, 32, 64, true);
	}
	
	public void update(double pz) {
		mesh.reset();
		uIter.reset();
		
		while(!uIter.done()) {
			double yr = uIter.ir;
			double zr = uIter.jr;
			double z = pz + (zr - 0.5) * 16;
			double y = world.roadHeight(z) + yr * 8;
			double x = world.cliffSurface(y, z) + 0.5;
			mesh.set(x, y, z, y, z % 1);
			uIter.next();
		}

		lIter.reset();
		while(!lIter.done()) {
			double yr = lIter.ir;
			double zr = lIter.jr;
			double z = pz + (zr - 0.5) * 16;
			double y = world.roadHeight(z) - yr * 8;
			double x = world.cliffSurface(y, z) - 0.5;
			mesh.set(x, y, z, y, z % 1);
			lIter.next();
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
