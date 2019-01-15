package com.joakimsvedin.whatsfordinner.Recipes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joakimsvedin.whatsfordinner.ColorUtilities;
import com.joakimsvedin.whatsfordinner.DataHandler;
import com.joakimsvedin.whatsfordinner.Ingredient;
import com.joakimsvedin.whatsfordinner.R;
import com.joakimsvedin.whatsfordinner.Repository;
import com.joakimsvedin.whatsfordinner.newDish.*;

import java.util.Arrays;
import java.util.List;


public class RecipesLandscape extends Fragment {

    private ListView lv ;
    View fragmentView;
    int lastClickedPos = 0;
    View lastConvertView;
    List<RecipesContent> listOfRecipes;

    public RecipesLandscape() {
        // Required empty public constructor
    }

    public void setRecepiesContents(List<RecipesContent> listOfRecipes){
        this.listOfRecipes = listOfRecipes;
        Log.e("update", "data: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes_landscape, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadListView();
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        fragmentView = v;
        lv = v.findViewById(R.id.recipesListView);
        loadListView();
    }

    private void loadListView() {
        Repository.getInstance().populateRecipes(listOfRecipes);


        final String products[] = Repository.getInstance().getAllRecipes();
        if(products.length == 0){
            Log.e("viewLoad", "close");
            return;
        }

        RecipeData data = Repository.getInstance().getRecipe(products[0]);
        Log.e("viewLoad", "instr: "+data.getInstructions());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.list_item, products){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                view.setBackgroundColor(ColorUtilities.getColorForRowPos(position));
                return view;
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView, int pos,
                                    long arg3) {
                if(lastConvertView != null) {
                    Log.v("last click ", Integer.toString(lastClickedPos));
                    lastConvertView.setBackgroundColor(ColorUtilities.getColorForRowPos(lastClickedPos));
                }
                lastConvertView = convertView;
                convertView.setBackgroundColor(Color.parseColor("#33E0FF"));
                lastClickedPos = pos;
                Log.v("onclick list it ", products[pos]);

                loadRecipe(Repository.getInstance().getRecipe(products[pos]));
            }
        });

        if(products.length <= lastClickedPos){
            return;
        }

        loadRecipe(Repository.getInstance().getRecipe(products[lastClickedPos]));


        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), NewDishActivity.class);
                i.putExtra("recipeName", products[position]);

                startActivityForResult(i, 1);

                /*if(lastConvertView != null) {
                    Log.v("last click ", Integer.toString(lastClickedPos));
                    lastConvertView.setBackgroundColor(ColorUtilities.getColorForRowPos(lastClickedPos));
                }*/

                lastClickedPos = position;

                lastConvertView = v;

                return true;
            }
        });
    }

    private void loadRecipe(RecipeData selectedRecipe){
        if(selectedRecipe == null){
            return;
        }
        TextView recipieNameTxtView = fragmentView.findViewById(R.id.recipe_name);
        recipieNameTxtView.setText(selectedRecipe.getNameOfRecipe());

        TextView ingredientListTxtView = fragmentView.findViewById(R.id.ingredient_list);
        StringBuilder ingredientListStr = new StringBuilder("");
        for(int i = 0; i < selectedRecipe.getIngredients().size(); i++){
            Ingredient in = selectedRecipe.getIngredients().get(i);
            ingredientListStr.append("* ");
            ingredientListStr.append(in.getName());
            ingredientListStr.append(" ");
            ingredientListStr.append("(");
            ingredientListStr.append(in.getQuantity());
            ingredientListStr.append(" ");
            ingredientListStr.append(in.getUnit());
            ingredientListStr.append(") \n");
        }
        ingredientListTxtView.setText(ingredientListStr.toString());

        ImageView recipeImageView = fragmentView.findViewById(R.id.recipe_photo);
        if(selectedRecipe.getImage() == null){
            recipeImageView.setImageResource(R.drawable.sharp_chrome_reader_mode_black_48);
        } else {
            recipeImageView.setImageBitmap(selectedRecipe.getImage());
        }
        recipeImageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.recipeImageView_height);
        recipeImageView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.recipeImageView_width);

        TextView recipeDescription  = fragmentView.findViewById(R.id.recipe_directions);
        recipeDescription.setText(selectedRecipe.getInstructions());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){
            DataHandler dataHandler = new DataHandler(getContext());

            listOfRecipes = dataHandler.getAllDishes();

            Repository.getInstance().populateRecipes(listOfRecipes);
        }
    }
}
