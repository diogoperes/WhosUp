package com.whosup.android.whosup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

public class UpdateMyProfileActivity extends Activity {

    public static final int GET_FROM_GALLERY = 3;
    public static final String TAG = "UpdateMyProfileActivity";
    private ImageView avatarPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_profile);

        Button uploadImageButton = (Button) findViewById(R.id.button_upload_image);
        avatarPreview = (ImageView) findViewById(R.id.imageView_avatar);
        EditText customPhraseEditText = (EditText) findViewById(R.id.editText_custom_phrase);
        EditText cityEditText = (EditText) findViewById(R.id.editText_city);
        EditText coutryEditText = (EditText) findViewById(R.id.editText_country);
        EditText aboutMeEditText = (EditText) findViewById(R.id.about_me_update);
        EditText oldPasswordEditText = (EditText) findViewById(R.id.editText_password_old);
        EditText newPasswordEditText = (EditText) findViewById(R.id.editText_password_new);

        uploadImageButton.setOnClickListener(new UploadButtonImageOnClickListener());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            int[] maxSize = new int[1];
            GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                //IF IMAGE DIMENSIONS ARE BIGGER THAN SUPPORTED BY OPENGL VERSION ON HARDWARE
                if(bitmap.getHeight()>=maxSize[0]||bitmap.getWidth()>=maxSize[0]){
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                    System.out.println("BITMAP SIZE: "+bitmap.getWidth()+"x"+bitmap.getHeight());
                }
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

            avatarPreview.setImageBitmap(bitmap);
            avatarPreview.setAdjustViewBounds(true);
            System.out.println("IMAGE VIEW SIZE: "+avatarPreview.getWidth()+"x"+avatarPreview.getHeight());
        }
    }

    private class UploadButtonImageOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
        }
    }
}
