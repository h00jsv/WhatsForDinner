package com.joakimsvedin.whatsfordinner.Recipes;

import android.graphics.Bitmap;
import android.net.Uri;

import com.joakimsvedin.whatsfordinner.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeData {

    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Ingredient> nutrients = new ArrayList<>();
    private String nameOfRecipe = "";
    private Uri imageUri = null;
    Bitmap image;
    private String instructions = "";

    public RecipeData() {

    }

    public RecipeData getClone() {

        RecipeData recipeClone = new RecipeData();
        recipeClone.setNameOfRecipe(this.nameOfRecipe);
        recipeClone.setImageUri(this.imageUri);
        recipeClone.setInstructions(this.instructions);

        ArrayList<Ingredient> ingredList = new ArrayList<>();
        for(int i = 0; i < this.getIngredients().size(); i++) {
            ingredList.add(this.getIngredients().get(i).getClone());
        }

        recipeClone.setIngredients(ingredList);

        ArrayList<Ingredient> nutrList = new ArrayList<>();
        for(int j = 0; j < this.getNutrients().size(); j++) {
            nutrList.add(this.getNutrients().get(j).getClone());
        }

        recipeClone.setNutrients(nutrList);
        return recipeClone;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<Ingredient> nutrients) {
        this.nutrients = nutrients;
    }

    public String getNameOfRecipe() {
        return nameOfRecipe;
    }

    public void setNameOfRecipe(String nameOfRecipe) {
        this.nameOfRecipe = nameOfRecipe;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
