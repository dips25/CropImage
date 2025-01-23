package com.layout.swiiiipe.myapplication;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


public class MyDrawingSurface extends SurfaceView {

    private static final String TAG = MyDrawingSurface.class.getName();

    private Matrix mMatrix;

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

    private float[] mMatrixValues = new float[9];

    Paint pathPaint;

    Paint transPAint;
    Canvas paintCnvs;

    float dx;
    float dy;

    SurfaceHolder holder;
    Bitmap bitmap;

    MyDrawingThread myDrawingThread;

    ProgressDialog progressDialog;
    int[] pixels;

    Random random = new Random();

    boolean isWidth,isHeight;

    Canvas mainCanvas;

    Bitmap mainBitmap;

    int ccx;

    int mActivesecondPointedId;

    int firstPointerIndex=0;
    int secondPointerIndex=0;

    float centerX=0f;
    float centerY=0f;

    float startX,startY;

    int ccy;

    Rect boundRect;

    boolean inBounds;




    public MyDrawingSurface(Context context) {
        super(context);
        //this(context , null ,file);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.create();






//        holder = getHolder();
//        holder.addCallback(this);
    }

    public MyDrawingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.create();



//        pathPaint = new Paint();
//        pathPaint.setAntiAlias(true);
//        pathPaint.setColor(getResources().getColor(R.color.white));
//        pathPaint.setStyle(Paint.Style.STROKE);
//        pathPaint.setStrokeJoin(Paint.Join.ROUND);
//        pathPaint.setStrokeCap(Paint.Cap.ROUND);
//        pathPaint.setStrokeWidth(30);
//
//        transPAint = new Paint();
//        transPAint.setAntiAlias(true);
//        transPAint.setColor(getResources().getColor(android.R.color.transparent));
//        transPAint.setStyle(Paint.Style.STROKE);
//        transPAint.setStrokeJoin(Paint.Join.ROUND);
//        transPAint.setStrokeCap(Paint.Cap.ROUND);
//        transPAint.setStrokeWidth(30);
//
//        //holder = getHolder();
//        //this.bitmap  = reduceSize(file , Utils.getScreenWidth(getContext()) , 600);
//        mScaleDetector = new ScaleGestureDetector(getContext() , new ScaleListener());
//
//        myDrawingThread = new MyDrawingThread("DrawingThread");
//        myDrawingThread.start();
//
//
//
//        myDrawingThread.holder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(@NonNull SurfaceHolder holder) {
//
//                myDrawingThread.handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        myDrawingThread.mCanvas.drawBitmap(myDrawingThread.bitmap , myDrawingThread.matrix , null);
//                        myDrawingThread.matrix.invert(inverseMatrix);
//                        pixels = new int[myDrawingThread.bitmap.getWidth() * myDrawingThread.bitmap.getHeight()];
//
//                        Canvas canvas = holder.lockCanvas();
//                        //Bitmap bitmap = BitmapFactory.decodeResource(getResources() , R.drawable.indiagate);
//
//                        canvas.drawBitmap(myDrawingThread.bitmap,myDrawingThread.matrix , null);
//
//                        MyDrawingSurface.this.holder.unlockCanvasAndPost(canvas);
//
//                    }
//                });
//            }
//
//            private Bitmap reduceSize(File bitmapFile , int width , int height) {
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//
//                Bitmap bitmap1 = BitmapFactory.decodeFile(bitmapFile.getPath() , options);
//
//                int halfWidth = 0;
//                int halfHeight = 0;
//                int inSampleSize=1;
//
//                if (options.outWidth>width || options.outHeight>height) {
//
//                    halfWidth = width/2;
//                    halfHeight = height/2;
//
//                    while ((halfWidth/inSampleSize>=width) && (halfHeight/inSampleSize>=height)) {
//
//                        inSampleSize*=2;
//                    }
//                }
//
//                options.inSampleSize = inSampleSize;
//                options.inJustDecodeBounds=  false;
//
//                Bitmap bmp = BitmapFactory.decodeFile(bitmapFile.getPath() , options);
//                return bmp;
//            }
//
//
//
//
//            @Override
//            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
//
//               // Log.d(TAG, "surfaceChanged: " + width + " " + height);
//
//
//
//            }
//
//            @Override
//            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
//
//                myDrawingThread.handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        myDrawingThread.mCanvas.drawBitmap(myDrawingThread.bitmap , myDrawingThread.matrix , null);
//                        myDrawingThread.matrix.invert(inverseMatrix);
//
////                        Canvas c =  holder.lockCanvas();
////                        c.drawBitmap(myDrawingThread.bitmap , myDrawingThread.matrix , null);
////                        myDrawingThread.holder.unlockCanvasAndPost(c);
//
//                    }
//                });
//
//            }
//        });
    }

    int width = 0;
    int height = 0;

    int bmpWidth;
    int bmpHeight;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.d(TAG, "onSizeChanged: " + w +" " + h);

        this.width = w;
        this.height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setImage(Bitmap bmp) {

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

        //this.bitmap = bmp;

        Log.d(TAG, "setImage: " + bmp.getWidth() +" " + bmp.getHeight());

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {





                MyDrawingSurface.this.bitmap = reduceSize(bmp);

                if (isWidth) {

                    int fHeight = (int) (getHeight()*0.50);
                    int dWidth = (int) (getWidth() - bitmap.getWidth())/2;
                    int dHeight = (int) (getHeight() - bitmap.getHeight())/2;
                    ccy = dHeight;
                    ccx = dWidth;

                    boundRect = new Rect(ccx,ccy,bitmap.getWidth(),ccy+bitmap.getHeight());

                } else {

                    int dWidth = (int) (getWidth() - bitmap.getWidth())/2;
                    int dHeight = (int) (getHeight() - bitmap.getHeight())/2;
                    ccy = dHeight;
                    ccx = dWidth;

                    boundRect = new Rect(ccx,ccy,ccx+bitmap.getWidth(),bitmap.getHeight());


                }

                mainBitmap = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
                mainCanvas = new Canvas(mainBitmap);
                mainCanvas.drawBitmap(bitmap,ccx,ccy,null);

                bmpWidth = mainBitmap.getWidth();
                bmpHeight = mainBitmap.getHeight();

                centerX = mainBitmap.getWidth()/2;
                centerY = mainBitmap.getHeight()/2;

                mScaleDetector = new ScaleGestureDetector(getContext() , new ScaleListener());

                myDrawingThread = new MyDrawingThread("DrawingThread");

//                mainCanvas.drawBitmap(bitmap,ccx,ccy,null);
                //mainCanvas.drawCircle(centerX,centerY,5,pathPaint);
                Log.d(TAG, "OPoint"  + centerX + " " + centerY);
//                myDrawingThread.matrix.getValues(mMatrixValues);
//                myDrawingThread.matrix.mapVectors(new float[]{(float) ccx,(float) ccy});


                myDrawingThread.holder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(@NonNull SurfaceHolder holder) {

                        myDrawingThread.handler.post(new Runnable() {
                            @Override
                            public void run() {


                                //myDrawingThread.mCanvas.drawBitmap(myDrawingThread.bitmap , myDrawingThread.matrix , null);

                                pixels = new int[myDrawingThread.bitmap.getWidth() * myDrawingThread.bitmap.getHeight()];

                                Canvas canvas = myDrawingThread.holder.lockCanvas();
                                canvas.drawColor(Color.BLACK);

                                //float[] translated = new float[]{(float)ccx,(float)ccy};


                                //myDrawingThread.matrix.setTranslate((float)ccx,(float)ccy);
                                //myDrawingThread.matrix.mapVectors(translated);



                                canvas.drawBitmap(mainBitmap,myDrawingThread.matrix,null);
                                myDrawingThread.matrix.invert(inverseMatrix);


                                myDrawingThread.holder.unlockCanvasAndPost(canvas);

                            }
                        });
                    }






                    @Override
                    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                        // Log.d(TAG, "surfaceChanged: " + width + " " + height);



                    }

                    @Override
                    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

                        myDrawingThread.handler.post(new Runnable() {
                            @Override
                            public void run() {

                                myDrawingThread.mCanvas.drawBitmap(myDrawingThread.bitmap , myDrawingThread.matrix , null);
                                myDrawingThread.matrix.invert(inverseMatrix);

//                        Canvas c =  holder.lockCanvas();
//                        c.drawBitmap(myDrawingThread.bitmap , myDrawingThread.matrix , null);
//                        myDrawingThread.holder.unlockCanvasAndPost(c);

                            }
                        });

                    }
                });

                getViewTreeObserver().removeOnGlobalLayoutListener(this);


            }
        });






    }

    private Bitmap reduceSize(File bitmapFile , int width , int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bitmap1 = BitmapFactory.decodeFile(bitmapFile.getPath() , options);

        int halfWidth = 0;
        int halfHeight = 0;
        int inSampleSize=1;

        if (options.outWidth>width || options.outHeight>height) {

            halfWidth = width/2;
            halfHeight = height/2;

            while ((halfWidth/inSampleSize>=width) || (halfHeight/inSampleSize>=height)) {

                inSampleSize*=2;
            }
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds=  false;

        Bitmap bmp = BitmapFactory.decodeFile(bitmapFile.getPath() , options);
        Rect srcRect = new Rect(0 , 0 , bmp.getWidth() , bmp.getHeight());
        Rect destREct = new Rect(0 , 0 , Utils.getScreenWidth(getContext()) , 600);
        int total = destREct.width() + destREct.height();
        float srcAspect = srcRect.width()/srcRect.height();

        try {

            if (srcRect.width()>destREct.width()) {

                destREct = new Rect(0 , 0 , destREct.width() , (int)(destREct.height()*srcAspect));

            } else if (srcRect.height()>destREct.width()) {

                destREct = new Rect(0 , 0 , (int)(destREct.width()*srcAspect) , destREct.height());

            } else {

                destREct = new Rect(0 , 0 , (int)(destREct.width()) , destREct.height());


            }

        } catch (Exception ex) {




        }


        Bitmap bmp1 = Bitmap.createScaledBitmap(bmp , Utils.getScreenWidth(getContext()) , 600 , false);

        Bitmap back = Bitmap.createBitmap(destREct.width() , destREct.height() , Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(back);
        cv.drawBitmap(bmp , srcRect , destREct , null);
        return back;

    }

    private Bitmap reduceSize(Bitmap bmp) {

        Log.d(TAG, "Bmp: " + bmp.getWidth() + " " + bmp.getHeight());







        Log.d(TAG, "reduceSize: " + width + " " + height);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//
//        Bitmap bitmap1 = BitmapFactory.decodeFile(bitmapFile.getPath() , options);
//
//        int halfWidth = 0;
//        int halfHeight = 0;
//        int inSampleSize=1;
//
//        if (options.outWidth>width || options.outHeight>height) {
//
//            halfWidth = width/2;
//            halfHeight = height/2;
//
//            while ((halfWidth/inSampleSize>=width) || (halfHeight/inSampleSize>=height)) {
//
//                inSampleSize*=2;
//            }
//        }
//
//        options.inSampleSize = inSampleSize;
//        options.inJustDecodeBounds=  false;

        //Bitmap bmp = BitmapFactory.decodeFile(bitmapFile.getPath() , options);
        Rect srcRect = new Rect(0 , 0 , bmp.getWidth() , bmp.getHeight());
        RectF destREct = new RectF(0 , 0 , width , height);

        float srcAspect = (float) srcRect.width()/(float) srcRect.height();
        RectF newRect=null;

        try {

            if (srcRect.width()>destREct.width()) {


                newRect = new RectF(0 , 0 , destREct.width() , (destREct.width()/srcAspect));

                isWidth = true;


            } else if (srcRect.height()>destREct.height()) {

                newRect = new RectF(0 , 0 , (destREct.height()*srcAspect) , destREct.height());

                Log.d(TAG, "reduceSize: " + newRect.width() + " " + newRect.height());

            }

        } catch (Exception ex) {




        }


        //Bitmap bmp1 = Bitmap.createScaledBitmap(bmp , Utils.getScreenWidth(getContext()) , 600 , false);

        Bitmap back = Bitmap.createBitmap((int) newRect.width() , (int) newRect.height() , Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(back);
        cv.drawBitmap(bmp,srcRect,newRect,null);
        return back;

    }






    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //mScaleDetector.onTouchEvent(event);
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                if (MODE == 1) {

                    mActivePointerId = event.getPointerId(firstPointerIndex);

                    float[] mappedPoints = mapPoints(event.getX(), event.getY());
                    float mappedX = mappedPoints[0];
                    float mappedY = mappedPoints[1];

                    mainPath.moveTo(mappedX , mappedY);


                } else if (MODE == 2) {

                    Log.d(CropView.class.getName(), "onTouchEvent: ");
                    final float x = event.getX(firstPointerIndex);
                    final float y = event.getY(firstPointerIndex);
                    mLastTouchX = x;
                    mLastTouchY = y;

                    Log.d(TAG, "Points: " + x + " " + y);

                    if (boundRect.contains((int)x,(int) y)) {

                        inBounds = true;

//                        centerX = bmpWidth/2;
//                        centerY = bmpHeight/2;


                    }


                }

                break;





            case MotionEvent.ACTION_POINTER_DOWN:


                   if (MODE == 4) {

                    mActivePointerId = event.getPointerId(firstPointerIndex);

                    final int pointerIndexx = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    int sId = event.getPointerId(pointerIndexx);

                    if (sId!=mActivePointerId) {


                        secondPointerIndex = 1;
                        mActivesecondPointedId = event.getPointerId(pointerIndexx);


                        //centerY = centerY - ccy;

                        Log.d(TAG, "onTouchEvent: " + String.valueOf(ccy-centerY));

                    }


                }


                break;


//            case MotionEvent.ACTION_DOWN: {
//
////                if (MODE == 1) {
////
////                    float[] mappedPoints = mapPoints(event.getX(), event.getY());
////                    float mappedX = mappedPoints[0];
////                    float mappedY = mappedPoints[1];
////
//////
////
////                    mainPath.moveTo(mappedX , mappedY);
////
////
////                } else {
////
////                    Log.d(CropView.class.getName(), "onTouchEvent: ");
////                    final float x = event.getX();
////                    final float y = event.getY();
////                    mLastTouchX = x;
////                    mLastTouchY = y;
////
////                    mx = (int) event.getX();
////                    my = (int) event.getY();
////
////                    mActivePointerId = event.getPointerId(0);
////
////
////
////
////                }
////
////
////                break;
//            }
            case MotionEvent.ACTION_MOVE:

                if (MODE == 1) {

                    float[] mappedPoints = mapPoints(event.getX(), event.getY());
                    float mappedX = mappedPoints[0];
                    float mappedY = mappedPoints[1];



                    mainPath.lineTo(mappedX , mappedY);

                    paths.add(mainPath);

                    myDrawingThread.handler.post(new Runnable() {
                        @Override
                        public void run() {

                            //myDrawingThread.matrix.reset();
                            //myDrawingThread.matrix.postTranslate(dx , dy);

                            mainCanvas.drawPath(mainPath , pathPaint);
                            Canvas c = myDrawingThread.holder.lockCanvas();
                            c.drawBitmap(mainBitmap , myDrawingThread.matrix ,  null);
                            myDrawingThread.holder.unlockCanvasAndPost(c);




//                            for (Path path : paths) {

//                                Canvas c = myDrawingThread.holder.lockCanvas();
//                                //c.drawColor(Color.BLACK);
//                                //c.drawPath(mainPath , pathPaint);
//                                c.drawBitmap(MyDrawingSurface.this.bitmap , myDrawingThread.matrix , null);
//                                c.drawPath(mainPath , pathPaint);
//                                myDrawingThread.holder.unlockCanvasAndPost(c);


                          //  }


                        }
                    });



                } else if (MODE == 2) {


                    Log.d(CropView.class.getName(), "onTouchMoe: ");

                    Log.d(TAG, "onTouchEvent: " + bitmap.getHeight() + " "+boundRect.height());

                    if (inBounds) {

                        final float x = event.getX(firstPointerIndex);
                        final float y = event.getY(firstPointerIndex);
                        if (!mScaleDetector.isInProgress()) {
                            Log.d(CropView.class.getName(), "onTouchMoew: ");
                            dx = x - mLastTouchX;
                            dy = y - mLastTouchY;

                            float adx = Math.abs(dx);
                            float ady = Math.abs(dy);
                            myDrawingThread.matrix.getValues(mMatrixValues);
                            float[] translated = {dx, dy};
                            myDrawingThread.matrix.mapVectors(translated);
                            inverseMatrix.mapVectors(translated);
                            float translatedX = translated[0];
                            float translatedY = translated[1];
                            myDrawingThread.matrix.postTranslate(translatedX, translatedY);
                            myDrawingThread.matrix.invert(inverseMatrix);



                            if (x>mLastTouchX) {

                                centerX+=adx;

                            } else if (x<mLastTouchX) {

                                centerX-=adx;
                            }

                            if (y>mLastTouchY) {

                                centerY+=ady;

                            } else if (y<mLastTouchY) {

                                centerY-=ady;
                            }

                            Log.d(TAG, "TPoint: "+centerX +" "+centerY);
                            myDrawingThread.handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    //mainCanvas.drawCircle(centerX,centerY,5,pathPaint);

                                    Canvas canvas = myDrawingThread.holder.lockCanvas();
                                    canvas.drawColor(Color.BLACK);
                                    canvas.drawBitmap(mainBitmap , myDrawingThread.matrix , null);
                                    myDrawingThread.holder.unlockCanvasAndPost(canvas);
                                    //myDrawingThread.mCanvas.drawBitmap(MyDrawingSurface.this.bitmap , myDrawingThread.matrix , null);



//                                Bitmap bitmap1 = Bitmap.createBitmap(myDrawingThread.bitmap.getWidth(),myDrawingThread.bitmap.getHeight() , Bitmap.Config.ARGB_8888);
//                                //myDrawingThread.mCanvas = new Canvas(bitmap1);
//                                myDrawingThread.mCanvas.drawBitmap(bitmap1 ,myDrawingThread.matrix  , null);




                                }
                            });
                        }

                        mLastTouchX = x;
                        mLastTouchY = y;


                    }






                } else if (MODE == 4) {

                    if (secondPointerIndex!=firstPointerIndex) {

                        Log.d(TAG, "onTouchEvent: " + centerX+" " + centerY);


                        Log.d(TAG, "onTouchEventSec: " + secondPointerIndex);

                        float dX = event.getX(secondPointerIndex);
                        float dy = event.getY(secondPointerIndex);

                        float angle = calculateDistanceAndAngle(centerX,centerY,dX,dy);

                        myDrawingThread.matrix.preRotate(angle,centerX,centerY);
                        inverseMatrix.preRotate(angle,centerX,centerY);
                        myDrawingThread.matrix.invert(inverseMatrix);

                        myDrawingThread.matrix.getValues(mMatrixValues);
                        //inverseMatrix.mapPoints(mMatrixValues);

                        //myDrawingThread.matrix.mapPoints(mMatrixValues);
                        Log.d(TAG, "onTouchEventSec: " + angle);


                        myDrawingThread.handler.post(new Runnable() {
                            @Override
                            public void run() {

                                Canvas canvas = myDrawingThread.holder.lockCanvas();
                                canvas.drawColor(Color.BLACK);

                                canvas.drawBitmap(mainBitmap,myDrawingThread.matrix,null);



                                myDrawingThread.holder.unlockCanvasAndPost(canvas);







                            }
                        });


                    }


                }


            break;
            case MotionEvent.ACTION_UP:

//                if (MODE == 3) {
//
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//
//
//                        myDrawingThread.handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                int px = (int) event.getX();
//                                int py = (int) event.getY();
//
//                                if (px >= 0 && px < myDrawingThread.bitmap.getWidth() && py >= 0 && py < myDrawingThread.bitmap.getHeight()) {
//
//                                    int tPixel = myDrawingThread.bitmap.getPixel(px , py);
////                                    int targetGray = (int) (Color.red(tPixel) * 0.3
////                                            + Color.green(tPixel) * 0.59
////                                            + Color.blue(tPixel) * 0.11);
//
//                                    myDrawingThread.bitmap.getPixels(pixels, 0, myDrawingThread.bitmap.getWidth(), 0, 0, myDrawingThread.bitmap.getWidth(), myDrawingThread.bitmap.getHeight());
//
//                                    for (int i = 0 ; i<myDrawingThread.bitmap.getWidth() ; i++) {
//
//                                        for (int j = 0; j < myDrawingThread.bitmap.getHeight();j++) {
//
//
//                                            int x = i;
//                                            int y = j;
//
//
//
//
////                                            int Cpixel = myDrawingThread.bitmap.getPixel(x, y);
////
////                                            int currentGray = (int) (Color.red(Cpixel) * 0.3
////                                                    + Color.green(Cpixel) * 0.59
////                                                    + Color.blue(Cpixel) * 0.11);
//
//
//
//                                                int Cpixel = myDrawingThread.bitmap.getPixel(x, y);
//
//                                                int currentGray = (int) (Color.red(Cpixel) * 0.3
//                                                        + Color.green(Cpixel) * 0.59
//                                                        + Color.blue(Cpixel) * 0.11);
//
//                                                //int thres = random.nextInt(200);
//
//                                            //Log.d(TAG, "run: " + thres);
//
//                                            if (Cpixel == tPixel) {
//                                                if (currentGray>100) {
//
//                                                    myDrawingThread.bitmap.setPixel(x , y , Color.WHITE);
//
//
//                                                }
//
//
//                                            }
//
//
//
//
//
//
//
//
//
//                                           // }
//
//
//
//
//
//                                        }
//                                        //break;
//
//                                    }
//
//                                    myDrawingThread.mCanvas.drawBitmap(myDrawingThread.bitmap , myDrawingThread.matrix , null);
//                                    myDrawingThread.matrix.invert(inverseMatrix);
//
//
//
//                                    // Get the target grayscale value
////                                    int targetColor = myDrawingThread.bitmap.getPixel(px, py);
////
////                                    int targetGray = (int) (Color.red(targetColor) * 0.3 + Color.green(targetColor) * 0.59 + Color.blue(targetColor) * 0.11);
////
////                                    // Get the bitmap dimensions
////                                    int width = myDrawingThread.bitmap.getWidth();
////                                    int height = myDrawingThread.bitmap.getHeight();
////
////                                    int currentColor = px;
////                                    int red = Color.red(px);
////                                    int green = Color.green(px);
////                                    int blue = Color.blue(px);
////
////                                    int Ccolor = Color.rgb(red , green , blue);
////
////                                    // Buffer to hold all pixels at once
//////                                    int[] pixels = new int[width * height];
////                                    myDrawingThread.bitmap.getPixels();
////
////                                    // Iterate through all pixels
////                                    for (int i = 0; i < pixels.length; i++) {
////                                        int x = i % width;
////                                        int y = i / width;
////
////                                        int pColor = pixels[i];
////                                        int pred = Color.red(pixels[i]);
////                                        int pgreen = Color.green(pixels[i]);
////                                        int pblue = Color.blue(pixels[i]);
////
////                                        int pcolor = Color.rgb(pred , pgreen , pblue);
////
////                                        if (pcolor == Ccolor) {
////
////
//////                                            if (currentGray>200) {
////
////                                                pixels[i] = Color.WHITE;
////                                                //break;
////
////                                            //}
////
////
////                                        }
////
////
////
////
////
////
////
////
////
////
////
////
////
////                                        // Compare grayscale values and update pixel if conditions met
////
////
////
////
////                                    }
//
//                                    //myDrawingThread.bitmap.setPixels(pixels , 0,  myDrawingThread.bitmap.getWidth() , 0 , 0,myDrawingThread.bitmap.getWidth() , myDrawingThread.bitmap.getHeight());
//                                }
//
//
//                                if (px>=0 && px<=myDrawingThread.bitmap.getWidth() && py>0 && py<=myDrawingThread.bitmap.getHeight()) {
//
//
//                                    int targetColor = myDrawingThread.bitmap.getPixel(px, py);
//
//                                    // Get the grayscale value of the target color
//                                    int targetGray = (int) (Color.red(targetColor) * 0.3 + Color.green(targetColor) * 0.59 + Color.blue(targetColor) * 0.11);
//
////                                    int red = Color.red(pxColor);
////                                    int green = Color.green(pxColor);
////                                    int blue = Color.blue(pxColor);
//
//                                    //int color = Color.rgb(red, green, blue);
//
//                                    for (int x = 0; x < myDrawingThread.bitmap.getWidth(); x++) {
//
//                                        for (int y = 0; y < myDrawingThread.bitmap.getHeight(); y++) {
//
//                                            int currentColor = myDrawingThread.bitmap.getPixel(x, y);
////
////                                            int cred = Color.red(convColor);
////                                            int cgreen = Color.green(convColor);
////                                            int cblue = Color.blue(convColor);
////
////                                            int cColor = Color.rgb(cred, cgreen, cblue);
//
//                                            int currentGray = (int) (Color.red(currentColor) * 0.3 + Color.green(currentColor) * 0.59 + Color.blue(currentColor) * 0.11);
//
////                                            if (targetColor == currentColor) {
//
//                                            if (targetGray == currentGray) {
//
//                                                if (currentGray>10) {
//                                                    // Erase or set to white (Color.WHITE) or any erase color
//                                                    myDrawingThread.bitmap.setPixel(x, y, Color.WHITE);
//                                                }
//                                                // }
//
//
//                                            }
//
//                                            // Compare grayscale values
//
//
////                                            if (pxColor == convColor) {
////
////                                                myDrawingThread.bitmap.setPixel(x, y, Color.WHITE);
////                                            }
//                                        }
//
//                                    }
//                                }
//
//
//
//
//                                Canvas c = myDrawingThread.holder.lockCanvas();
//                                c.drawBitmap(myDrawingThread.bitmap , myDrawingThread.matrix , null);
//                                myDrawingThread.holder.unlockCanvasAndPost(c);
//
//                                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        progressDialog.dismiss();
//
//                                    }
//                                });
//
//                            }
//                        });
//
//
//
//                } else {
//
//                    mx = (int) event.getX();
//                    my = (int) event.getY();
//                }

                break;



            case MotionEvent.ACTION_CANCEL: {
                //mActivePointerId = INVALID_POINTER_ID;
                //mActivesecondPointedId = INVALID_POINTER_ID;

                break;
            }
            case MotionEvent.ACTION_POINTER_UP:

                firstPointerIndex = 0;
                secondPointerIndex = 0;
                antiClockwise = false;
                clockwise = false;
                tempAngle = 0;
                inBounds = false;
                //tempAngle = 0f;
//                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
//                final int pointerId = event.getPointerId(pointerIndex);
//                if (pointerId == mActivePointerId) {
//                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
//                    mLastTouchX = event.getX(newPointerIndex);
//                    mLastTouchY = event.getY(newPointerIndex);
//                    mActivePointerId = event.getPointerId(newPointerIndex);
//                }
                break;

        }
        return true;
    }

    float tempAngle = 0f;
    float savedAngle = 0;

    boolean antiClockwise = false;
    boolean clockwise = false;

    boolean isFirst = false;

    boolean positive,negative;

    float X,Y;

    Bitmap saveBitmap;

    private float calculateDistanceAndAngle(float centerX, float centerY, float dX, float dY) {

        float angle;
        float realAngle=0;

        boolean isNegative = false;

        float X = dX - centerX;
        float Y = dY - centerY;

        float DX =(float) Math.pow(X,2);
        float DY = (float) Math.pow(Y,2);

        float radius =(float) Math.sqrt(DX+DY);

        float m = (X)/radius;

        //angle = (float) Math.asin(m);


        float angleInRadians = (float) Math.atan2(Y, X);

        if (tempAngle == 0) {

             if (angleInRadians<0){

                realAngle = 0;

            } else {

                realAngle = 0;


            }

        } else {

            realAngle = angleInRadians - tempAngle;

            if (angleInRadians > 180) {

                // Ensure the shortest path (rotate counter-clockwise)

                realAngle = angleInRadians - 180;

            } else if (angleInRadians < -180) {
                // Ensure the shortest path (rotate clockwise)
                realAngle = angleInRadians+180;

            } else if (angleInRadians > 360) {

                    realAngle = angleInRadians-360;

                //realAngle -= 360;

            } else if (angleInRadians < -360) {

                realAngle = angleInRadians+360;


            }
        }

        tempAngle = angleInRadians;



        float angleInDegrees = (float) Math.toDegrees(realAngle);


        isFirst = true;

        return angleInDegrees;

    }

    public void setMode(int i) {

        this.MODE = i;

    }

    private float[] mapPoints(float x, float y) {
        float[] pts = new float[] { x, y };
        inverseMatrix.mapPoints(pts);
        return pts;
    }

    public void saveImageToGallery(Context context) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE , "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED , System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN , System.currentTimeMillis());


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q) {

            try {


                values.put(MediaStore.Images.Media.RELATIVE_PATH , "Pictures/CropImage");
                values.put(MediaStore.Images.Media.IS_PENDING,true);

                saveBitmap = Bitmap.createBitmap(mainBitmap
                        ,0
                        ,0
                        ,bitmap.getWidth(),bitmap.getHeight(),myDrawingThread.matrix
                        ,false);

                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
                OutputStream fos = context.getContentResolver().openOutputStream(uri);
                saveBitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

                values.put(MediaStore.Images.Media.IS_PENDING,false);
                context.getContentResolver().update(uri,values,null,null);
                Toast.makeText(context, "File saved to gallery.", Toast.LENGTH_SHORT).show();


            } catch (IOException e) {

                Toast.makeText(context, "Can't save file.", Toast.LENGTH_SHORT).show();


            } catch (Exception e) {

                Log.d(TAG, "saveImageToGallery: " + e.getMessage());

                Toast.makeText(context, "Can't save file.", Toast.LENGTH_SHORT).show();


            }

        } else {

            File file = new File(Environment.getExternalStorageDirectory().toString()+"/CropImage");

            try {

                if (!file.exists()) {

                    file.mkdirs();
                }
                String fileName = System.currentTimeMillis()+".png";

                File file1 = new File(file,fileName);
                if (!file1.exists()) {

                    file1.createNewFile();
                }

                saveBitmap = Bitmap.createBitmap(mainBitmap
                        ,0
                        ,0
                        ,bitmap.getWidth(),bitmap.getHeight(),myDrawingThread.matrix
                        ,false);

                OutputStream fos = new FileOutputStream(file1);
                saveBitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

                values.put(MediaStore.Images.Media.DATA,file1.getAbsolutePath());
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

                Toast.makeText(context, "File saved to gallery.", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

                Toast.makeText(context, "Can't save file.", Toast.LENGTH_SHORT).show();

            }




        }

    }

    private class MyDrawingThread extends HandlerThread {

        ImageView view = new ImageView(getContext());






        SurfaceHolder holder;



        Handler handler;
        ProgressDialog progressDialog;

        Bitmap bitmap;
        Canvas mCanvas;


        Matrix matrix;

        public MyDrawingThread(String name) {
            super(name);

            matrix = new Matrix();


            bitmap = Bitmap.createBitmap(MyDrawingSurface.this.bitmap.getWidth()
                    , MyDrawingSurface.this.bitmap.getHeight()
                    , Bitmap.Config.ARGB_8888);

            mCanvas = new Canvas(bitmap);

            mCanvas.drawBitmap(MyDrawingSurface.this.bitmap , matrix , null);

            holder = getHolder();

            start();

            handler = new Handler(getLooper());





        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        float mScaleFactor = 1.0f;

        int focusX , focusY;



        @Override
        public boolean onScale(ScaleGestureDetector detector) {
//            mScaleFactor *= detector.getScaleFactor();
//            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
//            myDrawingThread.matrix.setScale(mScaleFactor, mScaleFactor , mx , my);
//            myDrawingThread.handler.post(new Runnable() {
//                @Override
//                public void run() {
//
//
//
//                    Canvas canvas = myDrawingThread.holder.lockCanvas();
//                    canvas.drawColor(Color.BLACK);
//                    canvas.drawBitmap(myDrawingThread.bitmap , myDrawingThread.matrix , null);
//                    myDrawingThread.matrix.invert(inverseMatrix);
//                    myDrawingThread.holder.unlockCanvasAndPost(canvas);
//
//                }
//            });
            return true;
        }
    }


}
