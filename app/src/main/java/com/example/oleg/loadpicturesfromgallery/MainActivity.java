package com.example.oleg.loadpicturesfromgallery;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.first_image)
    Button firstButton;
    @BindView(R.id.second_image)
    Button secondButton;
    @BindView(R.id.image_one)
    ImageView firstImage;
    @BindView(R.id.image_two)
    ImageView secondImage;
    private Unbinder unbinder;
    private static final int SELECT_PICTURE_ONE = 100;
    private static final int SELECT_PICTURE_TWO = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        Log.d(TAG, "onCreate: "+1);
    }
    @OnClick({R.id.first_image, R.id.second_image})
    public void loadImage(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        switch (v.getId()){
            case R.id.first_image:
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_ONE);
                break;
            case R.id.second_image:
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_TWO);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_TWO) {
                selectImageContainer(data, secondImage);
            }else if(requestCode == SELECT_PICTURE_ONE){
                selectImageContainer(data,firstImage);
            }
        }
    }
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
    private void selectImageContainer(Intent data, ImageView image){
        Uri selectedImageUri = data.getData();
        if (null != selectedImageUri) {
            // Get the path from the Uri
            String path = getPathFromURI(selectedImageUri);
            Log.d(TAG, "Image Path : " + path);
            // Set the image in ImageView
            image.setImageURI(selectedImageUri);
        }
    }
}
