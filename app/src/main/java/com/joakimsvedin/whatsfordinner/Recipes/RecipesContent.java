package com.joakimsvedin.whatsfordinner.Recipes;

import android.graphics.Bitmap;
import android.util.Log;

import com.joakimsvedin.whatsfordinner.DataHandler;
import com.joakimsvedin.whatsfordinner.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class RecipesContent {

    String name;
    Bitmap image;
    ArrayList<String> ingredients = new ArrayList<>();
    String directions;
    List<String> counters = new ArrayList<>();
    Dictionary ingredientDictionary;


    public RecipesContent(String name){

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public ArrayList<Ingredient> getIngredientList() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        if (ingredientDictionary != null){
            int i = 0;
            for (String ing: this.ingredients){
                Ingredient newIng;
                if(counters.get(i).isEmpty()){
                    newIng = new Ingredient(ing, 0,
                            (String)ingredientDictionary.get(ing));
                }else{
                    newIng = new Ingredient(ing, Float.valueOf(counters.get(i)),
                            (String)ingredientDictionary.get(ing));
                }

                if(!ing.isEmpty()){
                    ingredients.add(newIng);
                }

                ++i;
            }
        }

        return ingredients;
    }

    public List<String> getCounters() {
        return counters;
    }

    public String getDirections() {
        return directions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setIngredients(List<String> ingredients, List<String> counter) {
        this.ingredients.addAll(ingredients);
        this.counters = counter;
    }

    public void setIngredients(String ingredients, String counters) {

        List<String> ingredientList = new ArrayList<>(Arrays.asList(ingredients.trim().split("\n")));;
        List<String> counterList;
        if (!counters.isEmpty()){
            counterList = new ArrayList<>(Arrays.asList(counters.trim().split("\n")));
        }else{
            counterList = new ArrayList<>();
        }
        this.ingredients.clear();
        this.ingredients.addAll(ingredientList);


        for (String _: ingredientList){
            if(ingredientList.size() > counterList.size()){
                counterList.add("");
            }else {
                break;
            }
        }
        this.counters = counterList;
        this.ingredients.add("");
        this.counters.add("1");

    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public void setIngredientDictionary(Dictionary ingredientDictionary) {
        this.ingredientDictionary = ingredientDictionary;
    }
}
