package com.wordsaretoys.thread;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.wordsaretoys.soar.Camera;
import com.wordsaretoys.soar.Display;
import com.wordsaretoys.soar.Random;

public class Main extends Activity {

	static public Display display;
	static public Random rng = new Random();
	
	private Path path;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i("Thread", "Main.onCreate");
	    
		// our app window should be full screen, without a menu bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		RelativeLayout layout = new RelativeLayout(this);
		setContentView(layout);

		// SOAR display object extends GL surface
		display = new Display(this);
		layout.addView(display);

		// create application objects
		Camera camera = new Camera();
		Textures textures = new Textures();
		World world = new World();
		
		path = new Path(camera, textures, world);

		Player player = new Player(camera, world);
		
		// register objects for GL thread callbacks
		display.addGLObject(player);
		display.addGLObject(path);

		new Hud(player, layout);
		
		Log.i("Thread", "Main thread started.");
    }

    @Override
    protected void onPause() {
    	super.onPause();
        Log.i("Thread", "Main.onPause");
        path.stopThread();
        display.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
        Log.i("Thread", "Main.onResume");
    	display.onResume();
    }
}
