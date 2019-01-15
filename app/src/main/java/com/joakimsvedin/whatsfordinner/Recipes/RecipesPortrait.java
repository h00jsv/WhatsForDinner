package com.joakimsvedin.whatsfordinner.Recipes;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.joakimsvedin.whatsfordinner.ColorUtilities;
import com.joakimsvedin.whatsfordinner.DataHandler;
import com.joakimsvedin.whatsfordinner.Meals.RecipesList;
import com.joakimsvedin.whatsfordinner.R;
import com.joakimsvedin.whatsfordinner.Repository;
import com.joakimsvedin.whatsfordinner.newDish.*;

import java.util.Arrays;
import java.util.List;

public class RecipesPortrait extends Fragment {

    private ListView lv ;
    List<RecipesContent> listOfRecipes;
    int lastClickedPos = -1;
    public RecipesPortrait() {
        // Required empty public constructor
    }

    public void setRecipesContents(List<RecipesContent> listOfRecipes){
        this.listOfRecipes = listOfRecipes;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes_portrait, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        lv = v.findViewById(R.id.recipesListView);
        Repository repository = new Repository();
        repository.populateRecipes(listOfRecipes);

        final String products[] = repository.getAllRecipes();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.list_item,
                products){
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
//                Toast.makeText(getApplicationContext(),"hiihih",Toast.LENGTH_SHORT).show();
                if(lastClickedPos != -1){
                    parent.getChildAt(lastClickedPos).setBackgroundColor(ColorUtilities.getColorForRowPos(pos));
                }
                lastClickedPos = pos;
                parent.getChildAt(pos).setBackgroundColor(Color.parseColor("#33E0FF"));
                Log.v("onclick list it ", products[pos]);

                Repository.getInstance().addToMealPlan(products[pos]);
                Toast.makeText(getActivity().getApplicationContext(), products[pos] +" added to the meal plan",
                        Toast.LENGTH_SHORT).show();


            }
        });
        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), NewDishActivity.class);
                i.putExtra("recipeName", products[position]);

                startActivityForResult(i, 1);

                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){
            DataHandler dataHandler = new DataHandler(getContext());
            RecipesContent recipesContent = dataHandler.getDish(data.getStringExtra("meal"));

            List<String> res = Arrays.asList(Repository.getInstance().getAllRecipes());

            int index = res.indexOf(recipesContent.getName());
            listOfRecipes.set(index, recipesContent);
        }
    }


}