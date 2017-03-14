package edu.dartmouth.cs.raunakbhojwani.myruns;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.content.SharedPreferences;
import android.content.Context;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* This activity displays a profile page if the profile option is chosen from the settings fragment. It stores
 * information like the name, email, phone, gender, year, and major of the user as well
 * as a picture that can be taken in-app or chosen from the gallery.
 *
 * Developed by Raunak Bhojwani for CS65: Smartphone Programming
 * January 2017
 *
 * @author: RaunakB
 */

public class ProfileActivity extends Activity {

    // Initialize the global variables needed to manage this activity

    private static final String TAG = "DebugTag";   //TAG for Logging Capabilities

    public static final int CAMERA_CODE = 1;
    public static final int FILE_CODE = 7;

    private static final String PHOTO_URI_KEY = "fullPhoto";
    private static final String CROPPED_PHOTO_URI_KEY = "croppedPhoto";

    private Uri fullPhotoUri, croppedPhotoUri;
    private ImageView imageView;

    boolean cameraCapturing;

    String userName, userEmail, userPhone, userClass, userMajor;
    Integer userGender;
    EditText nameEditText, emailEditText, phoneEditText, classEditText, majorEditText;
    RadioGroup genderRadioGroup;

    public static final String PREFERENCES = "Preferences";
    public static final String NAME = "NameKey";
    public static final String EMAIL = "EmailKey";
    public static final String PHONE = "PhoneKey";
    public static final String CLASS = "ClassKey";
    public static final String MAJOR = "MajorKey";
    public static final String GENDER = "GenderKey";

    SharedPreferences preferences;


    // Method that runs everytime the app is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "App Created");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        permissionChecks();

        imageView = (ImageView) findViewById(R.id.imageView);


        //If previous data is saved, display it as necessary
        if (savedInstanceState != null) {
            croppedPhotoUri = savedInstanceState.getParcelable(CROPPED_PHOTO_URI_KEY);
            fullPhotoUri = savedInstanceState.getParcelable(PHOTO_URI_KEY);
            imageView.setImageURI(croppedPhotoUri);
        } else {
            loadSnap();
        }

        nameEditText = (EditText) findViewById(R.id.user_name);
        emailEditText = (EditText) findViewById(R.id.user_email);
        phoneEditText = (EditText) findViewById(R.id.user_phone);
        classEditText = (EditText) findViewById(R.id.user_class);
        majorEditText = (EditText) findViewById(R.id.user_major);
        genderRadioGroup = (RadioGroup) findViewById(R.id.user_gender);


        preferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if (preferences != null)
        {
            userName = preferences.getString(NAME, null);
            userEmail = preferences.getString(EMAIL, null);
            userPhone = preferences.getString(PHONE, null);
            userGender = preferences.getInt(GENDER, R.id.user_gender);
            userClass = preferences.getString(CLASS, null);
            userMajor = preferences.getString(MAJOR, null);

            nameEditText.setText(userName);
            emailEditText.setText(userEmail);
            phoneEditText.setText(userPhone);
            genderRadioGroup.check(userGender);
            classEditText.setText(userClass);
            majorEditText.setText(userMajor);

        }
    }
    //Method to check for camera and media permissions
    private void permissionChecks() {
        if(Build.VERSION.SDK_INT < 23)
            return;

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }
    }

    //Method to deal with denial of essential camera permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        }else if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)||shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setMessage("We need this permission for the app to function as designed.")
                            .setTitle("Important: We need your permission");
                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
                            }
                        }
                    });
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
                }else{
                }
            }
        }
    }

    // This method has changed form MyRuns1. Now, it sets up two choices and throws the
    // user an alert dialog from which to choose, and then calls the respective function
    public void onChangeClicked(View view) {

        Log.d(TAG, "Change button clicked");

        final CharSequence[] choices = {"Capture from Camera", "Select from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Choose Profile Picture");
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (choices[item].equals("Capture from Camera")) {
                    captureCamera();
                } else {
                    chooseGallery();
                }
            }
        });
        builder.show();
    }

    // If the capture from camera option is chosen, you use the camera as in MyRuns1
    public void captureCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        fullPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fullPhotoUri);
        cameraIntent.putExtra("return-data", true);

        try {
            startActivityForResult(cameraIntent, CAMERA_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

        cameraCapturing = true;
    }

    // If the choose from gallery option is chosen, you set up an intent to pick an IMAGE from the gallery. Note the use
    // of FILE_CODE. Simialr to the use of CAMERA_CODE.
    public void chooseGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select File"), FILE_CODE);

    }

    //Method to save all data logged in including picture
    public void onSaveClicked(View view) {

        Log.d(TAG, "Save button clicked");

        userName = nameEditText.getText().toString();
        userEmail = emailEditText.getText().toString();
        userPhone = phoneEditText.getText().toString();
        userClass = classEditText.getText().toString();
        userMajor = majorEditText.getText().toString();
        userGender = genderRadioGroup.getCheckedRadioButtonId();

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(NAME, userName);
        editor.putString(EMAIL, userEmail);
        editor.putString(PHONE, userPhone);
        editor.putString(CLASS, userClass);
        editor.putString(MAJOR, userMajor);
        editor.putInt(GENDER, userGender);

        editor.commit();

        saveSnap();

        Log.d(TAG, "Data saved! App closing.");

        finish();
    }

    //Method to close app
    public void onCancelClicked(View view) {
        Log.d(TAG, "Cancel button clicked. App closing.");
        finish();
    }

    //Deals with when the app is destroyed or paused
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(CROPPED_PHOTO_URI_KEY, croppedPhotoUri);
        savedInstanceState.putParcelable(PHOTO_URI_KEY, fullPhotoUri);
        Log.d(TAG, "Instance State Saved photo Uris");
    }

    //Deals with when app's activity (taking a picture) is completed
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case CAMERA_CODE:
                beginCrop(fullPhotoUri);
                break;

            case Crop.REQUEST_CROP:
                handleCrop(resultCode, data);
                if (cameraCapturing) {
                    File filePath = new File(fullPhotoUri.getPath());
                    if (filePath.exists())
                        filePath.delete();
                }
                break;
            //Deals with the new FILE_CODE. Gets the chosen picture and moves it on for cropping.
            case FILE_CODE:
                Uri fileChosen = data.getData();
                beginCrop(fileChosen);
                break;
        }
        Log.d(TAG, "Activity Result triggered");
    }

    //Deals with cropping the picture using 3rd party tools
    private void beginCrop (Uri source) {
        Log.d(TAG, "Crop begun");
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    //Makes sure the cropped picture is saved in order to display
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            croppedPhotoUri = Crop.getOutput(result);
            imageView.setImageDrawable(null);
            imageView.setImageURI(croppedPhotoUri);
        }
        else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "Crop handled");
    }

    //Saves the picture in a file locally
    private void saveSnap() {
        imageView.buildDrawingCache();
        Bitmap bmap = imageView.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput("profile_photo.png", MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Log.d(TAG, "Photo saved");
    }

    //Loads a previously saved picture to be displayed on the main screen.
    private void loadSnap() {
        try {
            if(croppedPhotoUri != null)
                imageView.setImageURI(croppedPhotoUri);
            else
            {
                FileInputStream fis = openFileInput("profile_photo.png");
                Bitmap bmap = BitmapFactory.decodeStream(fis);
                imageView.setImageBitmap(bmap);
                fis.close();
            }
            Log.d(TAG, "Photo loaded");
        }
        catch (IOException ioe)
        {
            imageView.setImageResource(R.drawable.default_profile);
        }

    }
}
