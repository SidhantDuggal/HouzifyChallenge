
package com.houzifychallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ZoomImageView extends ImageView {

    //These two constants specify the minimum and maximum zoom
    private static float MIN_ZOOM = 1f;
    private static float MAX_ZOOM = 2.5f;

    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleDetector;

    //These constants specify the mode that we’re in
    private static int NONE = 0;
    private static int DRAG = 1;
    private static int ZOOM = 2;

    private int mode;

    private float mLastGestureX;
    private float mLastGestureY;

    //These two variables keep track of the X and Y coordinate of the finger when it first
    //touches the screen
    private float mStartX = 0f;
    private float mStartY = 0f;

    //These two variables keep track of the amount we need to translate the canvas along the X
    //and the Y coordinate
    private float mTranslateX = 0f;
    private float mTranslateY = 0f;

    //These two variables keep track of the amount we translated the X and Y coordinates, the last time we
    //panned.
    private float previousTranslateX = 0f;
    private float previousTranslateY = 0f;

    private boolean mDragged = false;
    private float mDisplayWidth;
    private float mDisplayHeight;

    public ZoomImageView(Context context, AttributeSet attrs){

        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        mDisplayWidth = this.getWidth();
        mDisplayHeight = this.getHeight();

    }

    public ZoomImageView(Context context){

        super(context);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        mDisplayWidth = this.getWidth();
        mDisplayHeight = this.getHeight();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;

                mStartX = event.getX() - previousTranslateX;
                mStartY = event.getY() - previousTranslateY;
                break;

            case MotionEvent.ACTION_MOVE:

                mode = DRAG;

                if (mScaleFactor <= 1.0f){

                    getParent().requestDisallowInterceptTouchEvent(false);

                } else {

                    getParent().requestDisallowInterceptTouchEvent(true);

                }

                mTranslateX = event.getX() - mStartX;
                mTranslateY = event.getY() - mStartY;

                if (mScaleDetector.isInProgress()){

                    mLastGestureX = mScaleDetector.getFocusX();
                    mLastGestureY = mScaleDetector.getFocusY();

                }

                double distance = Math.sqrt(Math.pow(event.getX() - (mStartX + previousTranslateX), 2) +
                    Math.pow(event.getY() - (mStartY + previousTranslateY), 2)
                );

                if(distance > 0)
                {
                    mDragged = true;
                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                mode = ZOOM;
                break;

            case MotionEvent.ACTION_UP:

                mode = NONE;
                mDragged = false;

                previousTranslateX = mTranslateX;
                previousTranslateY = mTranslateY;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;

                previousTranslateX = mTranslateX;
                previousTranslateY = mTranslateY;
                break;
        }

        mScaleDetector.onTouchEvent(event);

        if ((mode == DRAG && mScaleFactor != 1f && mDragged) || mode == ZOOM)
        {
            invalidate();
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.save();

        if((mTranslateX * -1)> (mScaleFactor - 1) * mDisplayWidth){

            mTranslateX = (1 - mScaleFactor) * mDisplayWidth;

        }

        if((mTranslateY * -1)> (mScaleFactor - 1) * mDisplayHeight){

            mTranslateY = (1 - mScaleFactor) * mDisplayHeight;

        }

        canvas.translate(mTranslateX, mTranslateY);

        if (mScaleDetector.isInProgress()){

            canvas.scale(mScaleFactor, mScaleFactor, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());

        }
        else{

            canvas.scale(mScaleFactor, mScaleFactor, mLastGestureX, mLastGestureY);

        }

        super.onDraw(canvas);

        canvas.restore();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector detector){

            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(MIN_ZOOM, Math.min(mScaleFactor, MAX_ZOOM));
            return true;

        }

    }

}