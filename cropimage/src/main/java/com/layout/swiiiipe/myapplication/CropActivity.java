package com.layout.swiiiipe.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;

public class CropActivity extends AppCompatActivity {

    CropView cropView;


    RelativeLayout mainFRame;

    Rect srcRect, destRect;

    ImageView erase;
    ImageView scale;
    ImageView mWand;

    ImageView save;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);



        Intent intent = getIntent();
        String path = intent.getStringExtra("data");

        File file =  new File(path);



        //byte[] bytes = intent.getByteArrayExtra("data");




        mainFRame = (RelativeLayout) findViewById(R.id.imageFrame);
        erase = (android.widget.ImageView) findViewById(R.id.erase);
        scale = (android.widget.ImageView) findViewById(R.id.scale);
        mWand=  (android.widget.ImageView) findViewById(R.id.mwand);
        save = (ImageView) findViewById(R.id.save);



        ColorStateList colorStateList = ContextCompat.getColorStateList(this , R.color.icon_tint);
        erase.setImageTintList(colorStateList);
        scale.setImageTintList(colorStateList);
        mWand.setImageTintList(colorStateList);

        erase.setSelected(false);
        erase.invalidate();

        scale.setSelected(false);
        scale.invalidate();

        mWand.setSelected(false);
        mWand.invalidate();


        Bitmap bitmap = reduceSize(file , Utils.getScreenWidth(this) , 1000);


//        srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        destRect = new Rect(0, 0, Utils.getScreenWidth(this) , 600);
//
//        float srcAspect = bitmap.getWidth() / bitmap.getHeight();
//        float destAspect = Utils.getScreenWidth(this)/Utils.getScreenHeight(this);
//
//        int total = bitmap.getWidth() + bitmap.getHeight();
//
//        if (bitmap.getWidth() > destRect.width()) {
//
//            destRect = new Rect(0 , 0 , Utils.getScreenWidth(this) , (int)(total/srcAspect));
//
//        } else {
//
//            destRect = new Rect(0 , 0 , (int)(total*srcAspect) , 600);
//
//        }



//        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth() , bitmap.getHeight() , Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap1);
//        canvas.drawBitmap(bitmap , 0 , 0 , null);

        MyDrawingSurface md = new MyDrawingSurface(this ,file);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Utils.getScreenWidth(this)
                , 600);
        params.bottomMargin = Utils.PixeltoDp(this , 50);
        md.setLayoutParams(params);

        erase.setOnClickListener((v) -> {

            scale.setSelected(false);
            scale.invalidate();

            mWand.setSelected(false);
            mWand.invalidate();

            erase.setSelected(true);
            erase.invalidate();

            md.setMode(1);


        });

        scale.setOnClickListener((v) -> {

            erase.setSelected(false);
            erase.invalidate();

            mWand.setSelected(false);
            mWand.invalidate();

            scale.setSelected(true);
            scale.invalidate();

            md.setMode(2);

        });

        mWand.setOnClickListener((v)->{

            scale.setSelected(false);
            scale.invalidate();

            erase.setSelected(false);
            erase.invalidate();



            mWand.setSelected(true);
            mWand.invalidate();

            md.setMode(3);
        });

        scale.setSelected(true);
        scale.invalidate();

        md.setMode(2);

        mainFRame.addView(md);

        md.setMode(2);

        save.setOnClickListener((v)->{

            md.saveBitmapToGallery();


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

            while ((halfWidth/inSampleSize>=width) && (halfHeight/inSampleSize>=height)) {

                inSampleSize*=2;
            }
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds=  false;

        Bitmap bmp = BitmapFactory.decodeFile(bitmapFile.getPath() , options);
        return bmp;
    }


}
