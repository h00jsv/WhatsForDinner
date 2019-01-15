package com.joakimsvedin.whatsfordinner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joakimsvedin.whatsfordinner.Groceries.Groceries;
import com.joakimsvedin.whatsfordinner.Meals.MealsActivity;
import com.joakimsvedin.whatsfordinner.Recipes.Recipes;
import com.joakimsvedin.whatsfordinner.newDish.NewDishActivity;


public class MainActivity extends AppCompatActivity {

    ImageView bannerImageView;
    TextView alertTextView;

    public void showMeals(View view)
    {
        // Intent object which calls getApplicationContext(), jumping to activity 'MealsActivity.class'
        Intent intent = new Intent(getApplicationContext(), MealsActivity.class);
        // how to pass variable to another activity
        intent.putExtra("username", "joakim");

        startActivity(intent);
    }
    public void newDish(View view)
    {

        Intent intent = new Intent(getApplicationContext(), NewDishActivity.class);
        //intent.putExtra("recipeName", "gggg");
        startActivity(intent);
    }

    public void showGroceries(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Groceries.class);

        startActivity(intent);
    }

    public void showRecipes(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Recipes.class);

        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hides the Action Bar from the Emulator
        setContentView(R.layout.activity_main);

        /** CREATE POP-UP DIALOG */

        bannerImageView = findViewById(R.id.bannerImageView);
        alertTextView = findViewById(R.id.AlertTextView);

        bannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setCancelable(true);
                builder.setTitle("App Info:");
                builder.setMessage("Author: Joakim Svedin\nSoftware Version: 1.0\nURL: www.sjsu.edu\n" +
                        "Copyright information:\n");


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //alertTextView.setVisibility(View.VISIBLE);
                    }
                });
                builder.show();
            }
        });


        /** END CREATE POP-UP DIALOG */
    }
}
