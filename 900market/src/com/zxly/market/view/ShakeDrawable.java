package com.zxly.market.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

/**
 * 界面加载动画
 */
public class ShakeDrawable extends AnimationDrawable {

    // default animation duration
    private static final int DURATION = 150;
    
    // the animation duration
    private int mDuration;
    
    public ShakeDrawable(Context context) {
        this(context, DURATION);
    }
    
	public ShakeDrawable(Context context, int duration) {
        mDuration = duration;
        // repeat the animation
        setOneShot(false);
        /** 来回晃动*/
        for(int i =1;i<9;i++){
        	int id = (i/2==0) ? 1 : 2;
        }
        int resourceId = context.getResources().getIdentifier( "shake_progress1", "drawable", context.getPackageName());
        addFrame(context.getResources().getDrawable(resourceId), mDuration);
        int resourceId2 = context.getResources().getIdentifier( "shake_progress2", "drawable", context.getPackageName());
        addFrame(context.getResources().getDrawable(resourceId2), mDuration);


    }
}