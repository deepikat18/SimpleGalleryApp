package com.example.galleryapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.DownloadManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FullScreenActivity extends AppCompatActivity {
    private FileOutputStream outputStream;
    private BitmapDrawable drawable;
    private Bitmap bitmap;
    private File file,dir;


    private static int REQUEST_CODE = 100;
    ImageView imageView,nextIV,prevIV;
    Button button,share;
    int position;
    private Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        imageView = (ImageView) findViewById(R.id.ImageFullView);
        button = (Button) findViewById(R.id.btnDownload);
        share = (Button) findViewById(R.id.btnShare);
        Animation scale = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale);
        imageView.startAnimation(scale);
        Intent i = getIntent();
        position = i.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);
        imageView.setImageResource(imageAdapter.imageArray[position]);

        nextIV = findViewById(R.id.nextIV);
        prevIV = findViewById(R.id.prevIV);

        if (position == 0)
            prevIV.setVisibility(View.GONE);
        if (imageAdapter.imageArray.length-1 ==position)
            nextIV.setVisibility(View.GONE);

        nextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageAdapter.imageArray.length -1 == position) {
                    nextIV.setVisibility(View.GONE);
                    Toast.makeText(FullScreenActivity.this, "This is the Last picture", Toast.LENGTH_SHORT).show();
                }
                else{
                    ++position;
                    nextIV.setVisibility(View.VISIBLE);
                    nextIV.setVisibility(View.VISIBLE);
                }
                imageView.setImageResource(imageAdapter.imageArray[position]);

            }
        });
        prevIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position ==0) {
                    prevIV.setVisibility(View.GONE);
                    Toast.makeText(FullScreenActivity.this, "This is the First picture", Toast.LENGTH_SHORT).show();
                }
                else {
                    --position;
                    nextIV.setVisibility(View.VISIBLE);
                    nextIV.setVisibility(View.VISIBLE);
                }
                imageView.setImageResource(imageAdapter.imageArray[position]);


            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(com.example.galleryapp.FullScreenActivity.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE

                )== PackageManager.PERMISSION_GRANTED){
                    saveImage();

                }
                else {
                    askPermission();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image();

            }
        });



    }

    private void askPermission() {
        ActivityCompat.requestPermissions(FullScreenActivity.this,new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        },REQUEST_CODE);
    }

    public void onRequestPermissionResult(int requestCode, @NotNull String[] permissions,@NotNull int[] grantResults){
        if (requestCode == REQUEST_CODE){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImage();
            }
            else {
                Toast.makeText(FullScreenActivity.this, "Please provide the request Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    private void saveImage() {

        dir = new File(Environment.getExternalStorageDirectory(),"SaveImage");
        if (!dir.exists()){
            dir.mkdir();
        }
        drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();

        file = new File(dir,System.currentTimeMillis()+".jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        Toast.makeText(com.example.galleryapp.FullScreenActivity.this,"Download Complete",Toast.LENGTH_SHORT).show();

        try {
            outputStream.flush();
            outputStream.close();

            intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            sendBroadcast(intent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void image(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();
        file  = new File(getExternalCacheDir()+"/"+"ImageDrawableApplication"+".jpg");

        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);

            outputStream.flush();
            outputStream.close();

            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startActivity(Intent.createChooser(intent,"share image"));
    }
}
