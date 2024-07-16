package com.layout.swiiiipe.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.nio.file.Paths;
import java.util.ArrayList;

public class CropView extends ImageView {

    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Path> calPath = new ArrayList<>();

    private Matrix inverseMatrix = new Matrix();



    int mx , my;

    Bitmap paintBmp;


    Path mainPath = new Path();


    private int MODE;
    private static final int INVALID_POINTER_ID = -1;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1f;
    private Matrix mMatrix = new Matrix();
    private float[] mMatrixValues = new float[9];

    Paint pathPaint;

    Paint transPAint;
    Canvas paintCnvs;

    float dx;
    float dy;

    public CropView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public CropView(Context context) {
        super(context);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(getResources().getColor(R.color.white));
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(30);

        transPAint = new Paint();
        transPAint.setAntiAlias(true);
        transPAint.setColor(getResources().getColor(android.R.color.transparent));
        transPAint.setStyle(Paint.Style.STROKE);
        transPAint.setStrokeJoin(Paint.Join.ROUND);
        transPAint.setStrokeCap(Paint.Cap.ROUND);
        transPAint.setStrokeWidth(30);




    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paintBmp != null) {

            canvas.drawBitmap(paintBmp, mMatrix, null);
        }

//        if (MODE == 2) {
//
//            for (Path path : paths) {
//                canvas.drawPath(path , transPAint);
////                paintCnvs.drawPath(path, pathPaint);
//            }
//
//            return;
//
//        }
        for (Path path : paths) {

            paintCnvs.drawPath(path, pathPaint);
        }
//
//            }

//        if (MODE == 2) {
//
//            for (Path path : paths) {
//
//                canvas.drawPath(path , transPAint);
//                paintCnvs.drawPath(path , pathPaint);
//
//            }
//
//            paths.clear();
//            mainPath.reset();
//
//        } else if (MODE == 1) {
//
//            for (Path path : paths) {
//
//                canvas.drawPath(path , pathPaint);
//                paintCnvs.drawPath(path , pathPaint);
//
//
//            }


        }

    private float[] mapPoints(float x, float y) {
        float[] pts = new float[] { x, y };
        inverseMatrix.mapPoints(pts);
        return pts;
    }




       // canvas.drawBitmap(paintBmp , 0 , 0 , null);









    public void setMode(int i) {

        this.MODE = i;


    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            mMatrix.setScale(mScaleFactor, mScaleFactor);
            setImageMatrix(mMatrix);
            return true;
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        paintBmp = Bitmap.createBitmap(bm.getWidth() , bm.getHeight() , Bitmap.Config.ARGB_8888);
        paintCnvs = new Canvas(paintBmp);
        paintCnvs.drawBitmap(bm, mMatrix, null);
        mMatrix.invert(inverseMatrix);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {

                if (MODE == 1) {

                    float[] mappedPoints = mapPoints(event.getX(), event.getY());
                    float mappedX = mappedPoints[0];
                    float mappedY = mappedPoints[1];

//                    mx = (int) event.getX();
//                    my = (int) event.getY();

                    mainPath.moveTo(mappedX , mappedY);


                } else {

                    Log.d(CropView.class.getName(), "onTouchEvent: ");
                    final float x = event.getX();
                    final float y = event.getY();
                    mLastTouchX = x;
                    mLastTouchY = y;
                    mActivePointerId = event.getPointerId(0);


                }


                break;
            }
            case MotionEvent.ACTION_MOVE: {

                if (MODE == 1) {

                    float[] mappedPoints = mapPoints(event.getX(), event.getY());
                    float mappedX = mappedPoints[0];
                    float mappedY = mappedPoints[1];

//                    mx = (int) event.getX();
//                    my = (int) event.getY();

                    mainPath.lineTo(mappedX , mappedY);


                    //mainPath = new Path();
                    //paintCnvs.drawLine(mx , my , (int)(event.getX()) , (int)(event.getY()) , pathPaint);
//                    mainPath.lineTo(mx , my);
                    paths.add(mainPath);
                    invalidate();


                } else {

                    Log.d(CropView.class.getName(), "onTouchMoe: ");

                    final int pointerIndex = event.findPointerIndex(mActivePointerId);
                    final float x = event.getX(pointerIndex);
                    final float y = event.getY(pointerIndex);
                    if (!mScaleDetector.isInProgress()) {
                        Log.d(CropView.class.getName(), "onTouchMoew: ");
                        dx = x - mLastTouchX;
                        dy = y - mLastTouchY;
                        mMatrix.getValues(mMatrixValues);
                        float[] translated = {dx, dy};
                        mMatrix.mapVectors(translated);
                        float translatedX = translated[0];
                        float translatedY = translated[1];
                        mMatrix.postTranslate(translatedX, translatedY);
                        setImageMatrix(mMatrix);
                    }
                    mLastTouchX = x;
                    mLastTouchY = y;
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }
}