package com.joakimsvedin.whatsfordinner;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.joakimsvedin.whatsfordinner.Recipes.RecipesContent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class DataHandler {
    Context context;
    ContextWrapper contextWrapper;

    public DataHandler(Context context){
        this.context = context;
        contextWrapper = new ContextWrapper(context);
    }

    public void safeDish(String name, Bitmap image, List<String> ingredients, List<String> counter, String directions){
        try {
            Boolean alreadyExists = true;

            File recipesFolder = context.getDir("recipes", Context.MODE_PRIVATE); //Creating an internal dir;
            if (!recipesFolder.exists()){
                recipesFolder.mkdirs();
            }

            File newDishFolder = new File(recipesFolder.getPath(), name);
            if (!newDishFolder.exists()) {
                newDishFolder.mkdirs();
                alreadyExists = false;
            }


            File imageFile = new File(newDishFolder.getPath(),"image.jpg");
            File ingredientsFile = new File(newDishFolder.getPath(),"ingredients");
            File countersFile = new File(newDishFolder.getPath(),"counters");
            File directionsFile = new File(newDishFolder.getPath(),"directions");

            safeImage(imageFile, image);
            safeTextFile(ingredientsFile, ingredients);
            safeTextFile(countersFile, counter);
            safeTextFile(directionsFile, directions);

            if (!alreadyExists){
                addPathToConfig(name);
            }

            //display file saved message
            Toast.makeText(context, "File saved successfully!", Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void safeImage(File imageFile, Bitmap image){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void safeTextFile(File dataFile, String text){
        OutputStreamWriter out = null;
        try {
            FileOutputStream fos = new FileOutputStream(dataFile);
            out = new OutputStreamWriter(fos);

            out.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void safeTextFile(File dataFile, List<String> text){
        String fileContent = "";
        for (String ingredient : text) {
            fileContent += ingredient+"\n";
        }

        safeTextFile(dataFile, fileContent);

    }

    private void addPathToConfig(String name){
        File directory = context.getFilesDir();
        File configFile = new File(directory, "config");

        String config = readTextFile(configFile);

        config += name+"\n";

        safeTextFile(configFile, config);
    }

    private List<String> getContentFromFile(String fileName){
        File directory = context.getFilesDir();
        File itemFile = new File(directory, fileName);
        String content = readTextFile(itemFile);

        List<String> list = new ArrayList<>();
        String[] lines = content.split("\n");

        for (String line: lines) {
            if (!line.isEmpty()){
                list.add(line);
            }
        }

        return list;
    }

    private List<String> getNamesFromConfig(){
        return getContentFromFile("config");
    }

    public Dictionary getIngredientDictionary(){
        List<String> ingredients = getContentFromFile("ingredientItems");
        List<String> counter = getContentFromFile("ingredientCounters");
        Dictionary ingredientDictionary = new Hashtable();
        int i = 0;
        for (String ingredient: ingredients){
            ingredientDictionary.put(ingredient, counter.get(i));
            i++;
        }
        return ingredientDictionary;
    }


    //Read Methods:


    public String readTextFile(File data){

        try {

            FileInputStream fileIn = new FileInputStream(data);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer = new char[100];
            String content = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                content += readString;
            }
            InputRead.close();
            return content;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Bitmap readImageFile(String name, String path) {
        try {

            File file = new File(path, name);

            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public RecipesContent getDish(String name){
        RecipesContent recipesContent = new RecipesContent(name);

        File recipesFolder = context.getDir("recipes", Context.MODE_PRIVATE);
        File newDishFolder = new File(recipesFolder.getPath(), name);


        Bitmap image = readImageFile("image.jpg", newDishFolder.getPath());

        File ingredientFile = new File(newDishFolder, "ingredients");
        File countersFile = new File(newDishFolder, "counters");
        File directionsFile = new File(newDishFolder, "directions");

        String ingredients = readTextFile(ingredientFile);
        String directions = readTextFile(directionsFile);
        String counters = readTextFile(countersFile);

        recipesContent.setImage(image);
        recipesContent.setIngredients(ingredients, counters);
        recipesContent.setDirections(directions);

        recipesContent.setIngredientDictionary(getIngredientDictionary());

        return recipesContent;
    }

    public List<RecipesContent> getAllDishes(){
        List<String> names = getNamesFromConfig();
        List<RecipesContent> recipes = new ArrayList<>();

        for (String name: names) {
            RecipesContent recipe = getDish(name);
            recipes.add(recipe);
        }

        return recipes;
    }


    public void safeIngredientItems(List<String> items){
        File directory = context.getFilesDir();
        File itemFile = new File(directory, "ingredientItems");


        safeTextFile(itemFile, items);
    }

    public void safeIngredientCounters(List<String> counters){
        File directory = context.getFilesDir();
        File ingrFile = new File(directory, "ingredientCounters");

        safeTextFile(ingrFile, counters);
    }

    public void safeIngredients(Dictionary ingredients){


        List<String> list = new ArrayList<>();
        for (Enumeration i = ingredients.keys(); i.hasMoreElements();) {
            list.add((String) i.nextElement());
        }

        safeIngredientItems(list);
        list = new ArrayList<>();

        for (Enumeration i = ingredients.elements(); i.hasMoreElements();) {
            list.add((String) i.nextElement());
        }

        safeIngredientCounters(list);
    }




}
