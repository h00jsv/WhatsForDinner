package com.joakimsvedin.whatsfordinner;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import com.joakimsvedin.whatsfordinner.Recipes.RecipeData;
import com.joakimsvedin.whatsfordinner.Recipes.RecipesContent;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Repository {

    // DATA FOR RECIPES ACTIVITY
    private static String toEat = "eaten out";
    private static Map<String, Integer> recepiesMealPlan = new HashMap<>();
    private static Map<String, Ingredient> itemsToBuy = new HashMap<>();
    private static String[][] mealsChosen = new String[7][3];

    // DATA FROM NEW DISH ACTIVITY
    private static Map<String, Boolean> ingredientNames = new HashMap<>();
    private static Map<String, RecipeData> recipeNames = new HashMap<>();
    private static Map<String, Ingredient> nutrients = new HashMap<>();
    private static ArrayList<Ingredient> mealPlanning = new ArrayList<>();


    private static final Repository DATA_CONTAINER = new Repository();


    public static Repository getInstance() {
        return DATA_CONTAINER;
    }

    static {
        // populateRecipes();
        initiateMealsChosen();
    }

    private static void initiateMealsChosen() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                mealsChosen[i][j] = toEat;
            }
        }
    }


    // GETTERS/SETTERS MIGHT REQUIRE THE [][] - DECLARATION TO WORK
    public static String getMealsChosen(int row, int col) {
        return mealsChosen[row][col];
    }

    public static void setMealsChosen(int row, int col, String recipeName) {
        mealsChosen[row][col] = recipeName;
    }

    public static void resetMealsChosen(int row, int col) {
        mealsChosen[row][col] = toEat;
    }

    public void addNewIngredient(Ingredient ingredient) {
        if (nutrients.containsKey(ingredient.getName())) return;
        nutrients.put(ingredient.getName(),
                new Ingredient(ingredient.getName(), 0,ingredient.getUnit()));
    }

    public String[] getMealsAvailable() {
        Set keys = recepiesMealPlan.keySet();

        String[] mealsAvailable = (String[]) keys.toArray(new String[keys.size()]);
        ArrayList<String> mealsToShow = new ArrayList<String>();

        for (int i = 0; i < mealsAvailable.length; i++) {
            String ma = mealsAvailable[i];
            if (recepiesMealPlan.get(ma) == 0)
                continue;

            mealsToShow.add(ma);
        }
        mealsToShow.add(toEat);
        return mealsToShow.toArray(new String[mealsToShow.size()]);
    }

    public Ingredient[] getItemsToBuy() {
        Set itemsKeys = itemsToBuy.keySet();
        String[] itemsToAdd = (String[]) itemsKeys.toArray(new String[itemsKeys.size()]);
        Ingredient[] itemsToDisplay = new Ingredient[itemsKeys.size()];

        for (int i = 0; i < itemsToAdd.length; i++) {
            itemsToDisplay[i] = (itemsToBuy.get(itemsToAdd[i]));
        }

        return itemsToDisplay;
    }

    public Ingredient getIngredients(String iName) {
        return itemsToBuy.get(iName);
    }

    public void deleteIngredient(String name){
        itemsToBuy.get(name).setQuantity(0);
    }

    public void restoreIngredient(String name, float quantity){
        itemsToBuy.get(name).setQuantity(quantity);
    }

    private void addRecipeIngredientsToGroceries(String rName, float rCount) {
        Log.e("recipe", rName);
        RecipeData recipeData = recipeNames.get(rName);
        for (String key: recipeNames.keySet()) {
            Log.e("listelements", key);
            if(recipeNames.get(key) == null){
                Log.e("error on finding", key);
            }
        }

        if(recipeData != null){
            for (int j = 0; j < recipeData.getIngredients().size(); j++) {
                Ingredient ingredient = recipeData.getIngredients().get(j);
                String ingredientName = ingredient.getName();
                if (itemsToBuy.containsKey(ingredientName)) {
                    Ingredient groceryIn = itemsToBuy.get(ingredientName);
                    groceryIn.setQuantity(groceryIn.getQuantity() + (rCount * ingredient.getQuantity()));
                    itemsToBuy.put(ingredientName, groceryIn);
                } else {
                    itemsToBuy.put(ingredientName, ingredient.getClone());
                }
            }
        }
    }


    //todo: remove this test code
    private void injectMockDataForTesting(){
        addToMealPlan("eggs toast");
        addToMealPlan("banana bread");
        addToMealPlan("banana bread");
//        addToMealPlan("banana bread");
//        for(int i=0; i<20; i++){
//            groceriesToShop.put("test " + i, new Ingredient("test " + i, i, "pieces"));
//        }
//        recipiesForMealPlan.put("eggs toast", 1);
//        recipiesForMealPlan.put("banana bread", 2);
    }

    public void updateTargetNutrient(String nName, Ingredient in){
//        if(nutrients.containsKey(nName)){
        nutrients.put(nName, in);
//        }
    }

    private Map<String, Ingredient> updateNutrientsFromMealPlan(){
        ArrayList<String> tempR = new ArrayList<>();
        for(int i=0; i < mealsChosen.length; i++){
            for(int j=0; j<mealsChosen[i].length; j++){
                String r = mealsChosen[i][j];
                if(r.equals(toEat)) continue;                   // NOTE: was "eatingOut"
                tempR.add(r);
            }
        }
        Map<String, Ingredient> nutrientsFrommealsChosen = new HashMap<>();
        for(int i=0; i<tempR.size(); i++){
            String r = tempR.get(i);
            List<Ingredient> inList = recipeNames.get(r).getNutrients();
            for(int j=0; j<inList.size(); j++){
                Ingredient in  = inList.get(j);
                Ingredient toUpdate = nutrientsFrommealsChosen.get(in.getName());
                if(nutrientsFrommealsChosen.containsKey(in.getName())){
                    toUpdate.setQuantity(toUpdate.getQuantity()+in.getQuantity());
                    nutrientsFrommealsChosen.put(in.getName(), toUpdate);
                } else  nutrientsFrommealsChosen.put(in.getName(), in.getClone());
            }
        }
        return nutrientsFrommealsChosen;
    }

    public ArrayList<Ingredient> getmealPlanning(){
        return mealPlanning;
    }

    public void updatemealPlanning(){
        Map<String, Ingredient> nutrientsFrommealsChosen = updateNutrientsFromMealPlan();
        ArrayList<Ingredient> list = new ArrayList<>();
        Set keys = nutrientsFrommealsChosen.keySet();
        String[] possibleMeals =  (String[]) keys.toArray(new String[keys.size()]);
        for(int i=0; i<possibleMeals.length; i++){
            String r = possibleMeals[i];
            list.add(nutrientsFrommealsChosen.get(r));
        }
        mealPlanning = list;
    }

    private HashMap<String, Ingredient> clonenutrients(){
        HashMap<String, Ingredient> clone = new HashMap<>();
        Set keys = nutrients.keySet();
        String[] possibleMeals =  (String[]) keys.toArray(new String[keys.size()]);
        for(int i=0; i<possibleMeals.length; i++){
            String r = possibleMeals[i];
            clone.put(r, nutrients.get(r).getClone());
        }
        return clone;
    }
    public String hasTargetNutrientReached(){
        HashMap<String, Ingredient> targetClone = clonenutrients();


        String result = "Yes";
        for(int i=0; i<mealPlanning.size(); i++){
            Ingredient in = mealPlanning.get(i);
            if(targetClone.containsKey(in.getName())){
                Ingredient targetIn = targetClone.get(in.getName());
                targetIn.setQuantity(targetIn.getQuantity()-in.getQuantity());
                if(targetIn.getQuantity() < 0) return "No";
            }
        }

        Set keys = targetClone.keySet();
        String[] possibleMeals =  (String[]) keys.toArray(new String[keys.size()]);
        for(int i=0; i<possibleMeals.length; i++){
            String r = possibleMeals[i];
            float val = targetClone.get(r).getQuantity();

            if(Math.abs(val)>0.09) //continue;
                return "No";
        }
        return "Yes";
    }

    public ArrayList<Ingredient> getnutrients(){
        ArrayList<Ingredient> list = new ArrayList<>();
        Set keys = nutrients.keySet();
        String[] possibleMeals =  (String[]) keys.toArray(new String[keys.size()]);
        for(int i=0; i<possibleMeals.length; i++){
            String r = possibleMeals[i];
            list.add(nutrients.get(r));
        }
        return list;
    }

    public static void populateRecipes(List<RecipesContent> listOfRecipes) {
        recipeNames.clear();
        if (listOfRecipes == null){
            return;
        }
        for (RecipesContent recipesContent: listOfRecipes){
            RecipeData meal = new RecipeData();
            meal.setIngredients(recipesContent.getIngredientList());
            meal.setNameOfRecipe(recipesContent.getName());
            meal.setInstructions(recipesContent.getDirections());
            meal.setImage(recipesContent.getImage());

            recipeNames.put(meal.getNameOfRecipe(), meal);
        }
    }

    public static void populateDummyRecipes(Context context) {
        RecipeData bananaBread = new RecipeData();
        List<Ingredient> bananaBreadIn = new ArrayList<>();
        bananaBreadIn.add(new Ingredient("bread", 1, "loaves"));
        bananaBreadIn.add(new Ingredient("banana", 20, "hands"));
        bananaBreadIn.add(new Ingredient("baking powder", 4, "lbs"));
        bananaBreadIn.add(new Ingredient("cashews", 2, "lbs"));
        bananaBread.setIngredients(bananaBreadIn);
        bananaBread.setNameOfRecipe("banana bread");
        bananaBread.setInstructions("Cream together butter and sugar."
                +"\nAdd eggs and crushed bananas.\nCombine well.\nSift together flour, soda and salt."+
                " Add to creamed mixture. Add vanilla.\nPour into greased and floured loaf pan."+
                "\nBake at 350 degrees for 60 minutes.\nKeeps well, refrigerated.");
        recipeNames.put("banana bread", bananaBread);
        ingredientNames.put("bread", true);
        ingredientNames.put("banana", true);
        ingredientNames.put("baking powder", true);
        ingredientNames.put("cashews", true);




        DataHandler data = new DataHandler(context);
        /*List<RecipesContent> list = data.getAllDishes();

        for (RecipesContent r: list){
            r.getName();
        }*/
        RecipesContent recipesContent = data.getDish("gggg");

        RecipeData testEssen = new RecipeData();
        testEssen.setIngredients(recipesContent.getIngredientList());
        testEssen.setNameOfRecipe(recipesContent.getName());
        testEssen.setInstructions(recipesContent.getDirections());
        testEssen.setImage(recipesContent.getImage());

        recipeNames.put(testEssen.getNameOfRecipe(), testEssen);




        RecipeData eggsToast = new RecipeData();
        List<Ingredient> eggsToastIn = new ArrayList<>();
        eggsToastIn.add(new Ingredient("eggs", 5, "dozens"));
        eggsToastIn.add(new Ingredient("bread", 1, "loaves"));
        eggsToastIn.add(new Ingredient("vanilla", 1, "oz"));
        eggsToastIn.add(new Ingredient("sugar", 5, "lbs"));
        eggsToastIn.add(new Ingredient("cinnamon", (float)0.5, "lbs"));
        eggsToastIn.add(new Ingredient("maple syrup", 30, "oz"));
        eggsToastIn.add(new Ingredient("strawberries", 10, "pieces"));
        eggsToastIn.add(new Ingredient("grapes", 10, "pieces"));
        eggsToastIn.add(new Ingredient("almonds", 10, "pieces"));
        eggsToastIn.add(new Ingredient("milk", 10, "gallons"));
//        eggsToastIn.add(new Ingredient("salt", 1, "lb"));
        eggsToast.setIngredients( eggsToastIn);
        eggsToast.setNameOfRecipe("eggs toast");
        List<Ingredient> eggsToastNu = new ArrayList<>();
        eggsToastNu.add(new Ingredient("carbohydrates", 1, "cal"));
        DATA_CONTAINER.addNewIngredient(new Ingredient("carbohydrates", 1, "cal"));
        eggsToast.setNutrients(eggsToastNu);
        eggsToast.setInstructions("Mix eggs and put them over bread. toast the bread now. ");
        recipeNames.put("eggs toast", eggsToast);
        ingredientNames.put("eggs", true);
        ingredientNames.put("vanilla", true);
        ingredientNames.put("sugar", true);
        ingredientNames.put("cinnamon", true);
        ingredientNames.put("maple syrup", true);
        ingredientNames.put("strawberries", true);
        ingredientNames.put("grapes", true);
        ingredientNames.put("almonds", true);
        ingredientNames.put("milk", true);
//        ingredientNames.put("salt", true);
    }
    
    
    

    public boolean existingRecipe(String newRecipieName) {
        newRecipieName = newRecipieName;
        if (recipeNames.containsKey(newRecipieName)) return true;
        return false;
    }


    public RecipeData getRecipe(String recipeName) {

        if (recipeNames.containsKey(recipeName))
            return recipeNames.get(recipeName);
        return null;
    }


    // printRecipes() method



    public void saveRecipe(RecipeData recipeData) {
        recipeNames.put(recipeData.getNameOfRecipe(), recipeData);
    }

    public void addNewIngredient(String newIngredient) {
        newIngredient = newIngredient;

        if (ingredientNames.containsKey(newIngredient))
            return;
        ingredientNames.put(newIngredient, true);
    }

    public String[] getAllRecipes() {
        Set keys = recipeNames.keySet();
        String[] strings = (String[]) keys.toArray(new String[keys.size()]);
        return strings;
    }

    public String[] getAllIngredients() {
        Set keys = ingredientNames.keySet();
        return (String[]) keys.toArray(new String[keys.size()]);
    }

    public void addToMealPlan(String recipeName) {
        incrementMealCount(recipeName);
        addRecipeIngredientsToGroceries(recipeName, 1);
    }

    public void incrementMealCount(String recipeName) {
        Integer integer = recepiesMealPlan.get(recipeName);
        if (integer == null) {
            recepiesMealPlan.put(recipeName, 1);
        } else {
            recepiesMealPlan.put(recipeName, integer + 1);
        }
    }

    public void decrementMealCount(String recipeName) {
        Integer integer = recepiesMealPlan.get(recipeName);
        if (integer != null && integer > 0) {
            recepiesMealPlan.put(recipeName, integer - 1);
        }

    }

    public Integer getMealCount(String recipeName) {
        return recepiesMealPlan.get(recipeName);
    }
}