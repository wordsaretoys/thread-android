package com.wordsaretoys.thread;

import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Hud implements Runnable {

	private TextView framesPerSecond;
	
	private ImageView moveButton;
	private ImageView lookButton;
	
	private Handler updater;
	
	public Hud(final Player player, RelativeLayout layout) {
		
        framesPerSecond = new TextView(Main.display.context);
        layout.addView(framesPerSecond);

        RelativeLayout.LayoutParams lp = 
        		new RelativeLayout.LayoutParams(
        				RelativeLayout.LayoutParams.WRAP_CONTENT, 
        				RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
        moveButton = new ImageView(Main.display.context) {
        	public boolean onTouchEvent(MotionEvent e) {
        		switch(e.getAction()) {
        		case MotionEvent.ACTION_DOWN:
        		case MotionEvent.AXIS_PRESSURE:
        			player.setMotion(e.getPressure());
        			break;
        			
        		case MotionEvent.ACTION_UP:
        			player.setMotion(0);
        			break;
        			
        		}
        		return true;
        	}
        };
        moveButton.setImageResource(R.drawable.foot_icon);
        layout.addView(moveButton, lp);
        
        lp = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
        lookButton = new ImageView(Main.display.context) {
    		private double initX; 
    		private double initY;
    		public boolean onTouchEvent(MotionEvent e) {
        		switch(e.getAction()) {
        		case MotionEvent.ACTION_DOWN:
        			initX = this.getWidth() * 0.5;
        			initY = this.getHeight() * 0.5;
        			break;
        		case MotionEvent.ACTION_MOVE:
        			double dx = (e.getX() - initX) / initX;
        			double dy = (e.getY() - initY) / initY;
        			player.setLooking(dx, dy);
        			break;
        		case MotionEvent.ACTION_UP:
        			player.setLooking(0,  0);
        			break;
        		}
        		return true;
        	}
        };
        lookButton.setImageResource(R.drawable.eye_watch);
        layout.addView(lookButton, lp);

        updater = new Handler();
        run();
	}
	
	public void run() {
		String tf = String.valueOf(Main.display.fps);
		framesPerSecond.setText(tf.subSequence(0, tf.length()));
		
		updater.postDelayed(this, 100);
	}
}
