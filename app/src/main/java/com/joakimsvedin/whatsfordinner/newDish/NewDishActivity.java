package com.joakimsvedin.whatsfordinner.newDish;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.joakimsvedin.whatsfordinner.DataHandler;
import com.joakimsvedin.whatsfordinner.MainActivity;
import com.joakimsvedin.whatsfordinner.R;
import com.joakimsvedin.whatsfordinner.Recipes.RecipesContent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



public class NewDishActivity extends AppCompatActivity {

    /** INTENT to go back to MainActivity */

    ArrayList<String> listViewItems = new ArrayList<>();
    CustomListAdapter customAdapter;
    DataHandler dataHandler;

    public NewDishActivity(){
        super();
        listViewItems.add("");

        dataHandler = new DataHandler(this);
    }

    public void backToMain(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
    }

    /** END INTENT to go back to MainActivity */

    /** IMPORTING PHOTO FROM PHONE'S GALLERY, USING INTENT */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getPhoto();

            }
        }

    }

    // creating a method, since the call is used in >1 place
    public void getPhoto(){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);

    }

    /** IMPORTING PHOTO FROM PHONE'S GALLERY, USING INTENT */


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);
        getSupportActionBar().hide(); // hides the Action Bar from the Emulator
        dataHandler = new DataHandler(this);

        final Button addPicBtn = findViewById(R.id.addPictureBtn);

        addPicBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /** IMPORTING PHOTO FROM PHONE'S GALLERY, USING INTENT */

                        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else{

                            // else, if permission is already granted, get the photo/
                            getPhoto();
                        }
                    }
                }
        );


        Intent intent = getIntent();
        final String name = intent.getStringExtra("recipeName");
        if (name != null){
            customAdapter = new CustomListAdapter(this, updateDish(name));
        }else{
            customAdapter = new CustomListAdapter(this, listViewItems);
        }



        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(customAdapter);
        customAdapter.setListViewHeightBasedOnChildren(listView);

        final Button ioBtn = findViewById(R.id.io);
        ioBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                customAdapter.safe();

                if (name != null){
                    Intent intent = new Intent();
                    intent.putExtra("meal", name);
                    setResult(1, intent);
                }

                finish();
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });


    }

    private RecipesContent updateDish(String name){
        return dataHandler.getDish(name);
    }




    // Setting the image into the imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri selectedImage = data.getData();

            try {
                final InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                final Bitmap selectedBitmap = BitmapFactory.decodeStream(imageStream);

                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(selectedBitmap);

            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

}
