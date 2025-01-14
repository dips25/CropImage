package com.layout.swiiiipe.myapplication;

import static android.Manifest.permission;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PickActivity extends AppCompatActivity {
    private static final int GALLERY_CODE =200 ;
    ActivityResultLauncher launcher;
    File photoFile;

    Button camera , gallery;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    private static final int REQUEST_IMAGE_CAPTURE = 100;

    public static void startImagePicker(Context context) {

        Intent intent = new Intent(context , PickActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = (Button) findViewById(R.id.camera);
        gallery = (Button) findViewById(R.id.gallery);

        camera.setOnClickListener((v)->{

            onCameraClick();
        });

        gallery.setOnClickListener((v)->{

            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);

                        try {

                            InputStream is = getContentResolver().openInputStream(uri);

                            byte[]bytes = new byte[is.available()];

                            is.read(bytes,0,bytes.length);


//                            byte[] bytes = new byte[2048];
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                            File file1 = new File(file , "Pics");
//                            if (!file1.exists()) {
//
//                                file1.mkdir();
//                            }
//
//                            File file2 = new File(file1 , "sample_img.png");
//                            if (!file2.exists()) {
//
//                                file2.createNewFile();
//                            }
//
//                            FileOutputStream fos = new FileOutputStream(file2);
//                            int i = 0;
//
//                            while ((i=(is.read(bytes))) !=-1) {
//
//                                baos.write(bytes , 0 , i);
//
//
//                            }
//
//                            baos.writeTo(fos);

                            Intent intent = new Intent(this , CropActivity.class);
                            intent.putExtra("data",bytes);
                            startActivity(intent);

                        } catch (Exception ex) {

                            Log.d(PickActivity.class.getName(), "onActivityResult: " + ex.getMessage());


                        }
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        launcher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions() , (isGranted)->{

            if (isGranted.containsValue(false)) {

                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            }



        });

        checkPermissions();


    }

    private void onCameraClick() {

        dispatchTakePictureIntent();

    }

    private void onGalleryClick() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , GALLERY_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode,resultCode,data);

        Bitmap bitmap = null;
        Uri uri=null;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //uri = data.getData();

            Intent intent = new Intent(this , CropActivity.class);
            intent.putExtra("data" , photoFile.getPath());
            startActivity(intent);
            //imageView.setImageBitmap(imageBitmap);
        } else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {

            try {

                String galleryUri = getGalleryImage(uri);

                InputStream is = getContentResolver().openInputStream(Uri.parse(galleryUri));
                //bitmap = BitmapFactory.decodeStream(is);

            } catch (Exception ex) {




            }

        }

        try {

            InputStream is = getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[2048];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = 0;

            while ((i=(is.read(bytes))) !=-1) {

                baos.write(bytes , 0 , i);


            }

            Intent intent = new Intent(this , CropActivity.class);
            intent.putExtra("data" , baos.toByteArray());
            startActivity(intent);

        } catch (Exception ex) {

            Log.d(PickActivity.class.getName(), "onActivityResult: " + ex.getMessage());


        }


    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.layout.swiiiipe.myapplication.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

    }

    private String getGalleryImage(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        String imagePath = null;

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnindex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                imagePath = cursor.getString(columnindex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imagePath;
    }

    private void checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

            String[] perms = {permission.READ_MEDIA_IMAGES
                    , permission.READ_MEDIA_VIDEO
                    , permission.CAMERA};

            launcher.launch(perms);


        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {

            String[] perms = {permission.READ_MEDIA_IMAGES
                    , permission.READ_MEDIA_VIDEO
                    , permission.CAMERA


            };

            launcher.launch(perms);



        } else {

            String[] perms = {permission.READ_EXTERNAL_STORAGE
                    , permission.WRITE_EXTERNAL_STORAGE
                    , permission.CAMERA
            };

            launcher.launch(perms);


        }


    }
}


