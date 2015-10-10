
package com.houzifychallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;
import android.widget.ImageView;



public class TouchImageView extends ImageView {

    //These two constants specify the minimum and maximum zoom
    private static float MIN_ZOOM = 1f;
    private static float MAX_ZOOM = 2.5f;

    private float scaleFactor = 1.0f;
    private ScaleGestureDetector detector;

    //These constants specify the mode that we’re in
    private static int NONE = 0;
    private static int DRAG = 1;
    private static int ZOOM = 2;

    private int mode;

    private float mLastGestureX;
    private float mLastGestureY;

    //These two variables keep track of the X and Y coordinate of the finger when it first
//touches the screen
    private float startX = 0f;
    private float startY = 0f;

    //These two variables keep track of the amount we need to translate the canvas along the X
//and the Y coordinate
    private float translateX = 0f;
    private float translateY = 0f;

    //These two variables keep track of the amount we translated the X and Y coordinates, the last time we
//panned.
    private float previousTranslateX = 0f;
    private float previousTranslateY = 0f;

    private boolean dragged = false;
    private float displayWidth;
    private float displayHeight;

    public TouchImageView(Context context, AttributeSet attrs){

        super(context, attrs);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        displayWidth = this.getWidth();
        displayHeight = this.getHeight();

    }

    public TouchImageView(Context context){

        super(context);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        displayWidth = this.getWidth();
        displayHeight = this.getHeight();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;

//We assign the current X and Y coordinate of the finger to startX and startY minus the previously translated
//amount for each coordinates This works even when we are translating the first time because the initial
//values for these two variables is zero.
                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;
                break;

            case MotionEvent.ACTION_MOVE:

                mode = DRAG;

                if (scaleFactor == 1.0f){

                    getParent().requestDisallowInterceptTouchEvent(false);

                } else {

                    getParent().requestDisallowInterceptTouchEvent(true);

                }

                translateX = event.getX() - startX;
                translateY = event.getY() - startY;

                if (detector.isInProgress()){

                    mLastGestureX = detector.getFocusX();
                    mLastGestureY = detector.getFocusY();

                }

//We cannot use startX and startY directly because we have adjusted their values using the previous translation values.
//This is why we need to add those values to startX and startY so that we can get the actual coordinates of the finger.
                double distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX), 2) +
                    Math.pow(event.getY() - (startY + previousTranslateY), 2)
                );

                if(distance > 0)
                {
                    dragged = true;
                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                mode = ZOOM;
                break;

            case MotionEvent.ACTION_UP:

                mode = NONE;
                dragged = false;

//All fingers went up, so let’s save the value of translateX and translateY into previousTranslateX and
//previousTranslate
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;

//This is not strictly necessary; we save the value of translateX and translateY into previousTranslateX
//and previousTranslateY when the second finger goes up
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;
        }

        detector.onTouchEvent(event);

//We redraw the canvas only in the following cases:
//
// o The mode is ZOOM
// OR
// o The mode is DRAG and the scale factor is not equal to 1 (meaning we have zoomed) and dragged is
// set to true (meaning the finger has actually moved)
        if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM)
        {
            invalidate();
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.save();

//If translateX times -1 is lesser than zero, let’s set it to zero. This takes care of the left bound

        if((translateX * -1)> (scaleFactor - 1) * displayWidth){

            translateX = (1 - scaleFactor) * displayWidth;

        }

        if((translateY * -1)> (scaleFactor - 1) * displayHeight){

            translateY = (1 - scaleFactor) * displayHeight;

        }

//We need to divide by the scale factor here, otherwise we end up with excessive panning based on our zoom level
//because the translation amount also gets scaled according to how much we’ve zoomed into the canvas.
        canvas.translate(translateX, translateY);

//We’re going to scale the X and Y coordinates by the same amount
        if (detector.isInProgress()){

            canvas.scale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());

        }
        else{

            canvas.scale(scaleFactor, scaleFactor, mLastGestureX, mLastGestureY);

        }

        super.onDraw(canvas);

/* The rest of your canvas-drawing code */
        canvas.restore();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector detector){

            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            return true;

        }

    }

    public float getScaleFactor(){

        return scaleFactor;

    }

}