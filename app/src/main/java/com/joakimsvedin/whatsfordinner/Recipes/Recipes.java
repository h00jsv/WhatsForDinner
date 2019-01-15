package com.joakimsvedin.whatsfordinner.Recipes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joakimsvedin.whatsfordinner.DataHandler;
import com.joakimsvedin.whatsfordinner.R;

import java.util.List;

public class Recipes extends AppCompatActivity {

    List<RecipesContent> listOfRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataHandler dataHandler = new DataHandler(this);
        if(listOfRecipes == null){
            listOfRecipes = dataHandler.getAllDishes();
        }

        setContentView(R.layout.activity_recipes);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment contentFragment;
        Configuration config = getResources().getConfiguration();

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            contentFragment = new RecipesLandscape();
            ((RecipesLandscape) contentFragment).setRecepiesContents(listOfRecipes);
        } else {
            contentFragment = new RecipesPortrait();
            ((RecipesPortrait) contentFragment).setRecipesContents(listOfRecipes);
        }

        fragmentTransaction.replace(R.id.your_placeholder, contentFragment);
        fragmentTransaction.commit();

    }

}
