package com.whosup.android.whosup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.whosup.android.whosup.utils.ConnectionDetector;
import com.whosup.android.whosup.utils.DateDisplayPicker;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.android.whosup.utils.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class UpdateMyProfileActivity extends Activity {

    ConnectionDetector cd;
    DateDisplayPicker birthdate;
    private Uri fileUri;
    private static final String UPDATE_PROFILE_URL = "http://whosup.host22.com/update_profile.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog=null;
    JSONParser jsonParser = new JSONParser();


    public static final int GET_FROM_GALLERY = 3;
    public static final String TAG = "UpdateMyProfileActivity";
    private ImageView avatarPreview;
    EditText customPhraseEditText, cityEditText, countryEditText, aboutMeEditText, oldPasswordEditText, newPasswordEditText, confirmNewPasswordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_profile);

        Button uploadImageButton = (Button) findViewById(R.id.button_upload_image);
        Button updateProfileButton = (Button) findViewById(R.id.update_profile_button);
        avatarPreview = (ImageView) findViewById(R.id.imageView_avatar);
        customPhraseEditText = (EditText) findViewById(R.id.editText_custom_phrase);
        cityEditText = (EditText) findViewById(R.id.editText_city);
        countryEditText = (EditText) findViewById(R.id.editText_country);
        aboutMeEditText = (EditText) findViewById(R.id.about_me_update);
        oldPasswordEditText = (EditText) findViewById(R.id.editText_password_old);
        newPasswordEditText = (EditText) findViewById(R.id.editText_password_new);
        confirmNewPasswordEditText = (EditText) findViewById(R.id.editText_password_confirm);
        uploadImageButton.setOnClickListener(new UploadButtonImageOnClickListener());
        updateProfileButton.setOnClickListener(new UpdateProfileOnClickListener());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            fileUri = selectedImage;
            System.out.println("FILE URI: " + fileUri);
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



    public class UpdateProfile extends AsyncTask<String, String, String> {



        /**
         * Before starting background thread Show Progress Dialog
         * */

        int success =0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog=null;
            pDialog = new ProgressDialog(UpdateMyProfileActivity.this);
            pDialog.setMessage("Updating Profile...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag




            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("username", SPreferences.getInstance().getLoginUsername(UpdateMyProfileActivity.this)));
                System.out.println("Password Inserted: " + newPasswordEditText.getText().toString());
                if(newPasswordEditText.getText().length()>0){
                    System.out.println("SENDING NEW PASSWORD");
                    String passwordMD5 = Utility.MD5(newPasswordEditText.getText().toString());
                    String oldPasswordMD5 = Utility.MD5(oldPasswordEditText.getText().toString());
                    params.add(new BasicNameValuePair("newPassword", passwordMD5));
                    params.add(new BasicNameValuePair("oldPassword", oldPasswordMD5));
                }
                if(customPhraseEditText.getText().length()>0){
                    params.add(new BasicNameValuePair("customPhrase", customPhraseEditText.getText().toString()));
                }
                if(cityEditText.getText().length()>0){
                    params.add(new BasicNameValuePair("city", cityEditText.getText().toString()));
                }
                if(countryEditText.getText().length()>0){
                    params.add(new BasicNameValuePair("country", countryEditText.getText().toString()));
                }
                if(aboutMeEditText.getText().length()>0){
                    params.add(new BasicNameValuePair("aboutMe", aboutMeEditText.getText().toString()));
                }
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        UPDATE_PROFILE_URL, "POST", params);

                // check your log for json response
                Log.d("Register Attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Profile Updated!", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Profile Update Failed!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (Exception e) {
                return null;
            }
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(UpdateMyProfileActivity.this, file_url, Toast.LENGTH_LONG).show();
                if(newPasswordEditText.getText().length()>0){
                   String passwordMD5 = Utility.MD5(newPasswordEditText.getText().toString());
                   SPreferences.getInstance().setLoginPassword(UpdateMyProfileActivity.this, passwordMD5);
                }
                if(customPhraseEditText.getText().length()>0){
                    SPreferences.getInstance().setLoginCustomPhrase(UpdateMyProfileActivity.this, customPhraseEditText.getText().toString());
                }
                if(cityEditText.getText().length()>0){
                    System.out.println("SENING COMMAND TO CHANGE SPREF");
                    SPreferences.getInstance().setLoginCity(UpdateMyProfileActivity.this, cityEditText.getText().toString());

                }
                if(countryEditText.getText().length()>0){
                    SPreferences.getInstance().setLoginCountry(UpdateMyProfileActivity.this, countryEditText.getText().toString());
                }
                if(aboutMeEditText.getText().length()>0){
                    SPreferences.getInstance().setLoginAboutMe(UpdateMyProfileActivity.this, aboutMeEditText.getText().toString());
                }

                if(success==1){
                    finish();
                }
            }else{
                Toast.makeText(UpdateMyProfileActivity.this, R.string.noConnection, Toast.LENGTH_LONG).show();

            }


        }

    }




    private class UpdateProfileOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            cd= new ConnectionDetector(UpdateMyProfileActivity.this);
            if(!cd.isConnectingToInternet()) {
                Toast.makeText(UpdateMyProfileActivity.this, R.string.noConnection, Toast.LENGTH_LONG).show();;
                return;
            }
            if(!allFieldsOk()) {
                return;

            }else{
                // Save the Data in Database
                //loginDataBaseAdapter.insertEntry(userName, password);
                new UpdateProfile().execute();
            }

        }
    }

    private boolean allFieldsOk() {
        if(newPasswordEditText.getText().length()>0 || oldPasswordEditText.getText().length()>0){
            //check password lenght
            if(newPasswordEditText.getText().toString().length()<6){
                Toast.makeText(UpdateMyProfileActivity.this, R.string.password_must_be_at_least_6_characters_long, Toast.LENGTH_LONG).show();
                return false;
            }else if(newPasswordEditText.getText().toString().length()>30){
                Toast.makeText(UpdateMyProfileActivity.this, R.string.password_too_long_maximum_length_is_30, Toast.LENGTH_LONG).show();
                return false;
            }


            String oldPasswordMD5 = Utility.MD5(oldPasswordEditText.getText().toString());
            if(!oldPasswordMD5.equals(SPreferences.getInstance().getLoginPassword(UpdateMyProfileActivity.this))){
                Toast.makeText(UpdateMyProfileActivity.this, R.string.old_password_is_wrong, Toast.LENGTH_LONG).show();
                return false;
            }
            if(!newPasswordEditText.getText().toString().equals(confirmNewPasswordEditText.getText().toString())){
                Toast.makeText(UpdateMyProfileActivity.this, R.string.password_does_not_match, Toast.LENGTH_LONG).show();
                return false;
            }
        }


        return true;
    }
}
