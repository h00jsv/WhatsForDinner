package com.joakimsvedin.whatsfordinner.Groceries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.joakimsvedin.whatsfordinner.DataHandler;
import com.joakimsvedin.whatsfordinner.Ingredient;
import com.joakimsvedin.whatsfordinner.R;
import com.joakimsvedin.whatsfordinner.Recipes.RecipeData;
import com.joakimsvedin.whatsfordinner.Recipes.Recipes;
import com.joakimsvedin.whatsfordinner.Recipes.RecipesContent;
import com.joakimsvedin.whatsfordinner.Repository;
import com.joakimsvedin.whatsfordinner.newDish.NewDishActivity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class Groceries extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    CoordinatorLayout coordinatorLayout;

    SwipeToDeleteCallback swipeToDeleteCallback;
    SwipeToChangeCallback swipeToChangeCallback;
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    Dictionary<String, Integer> lookUp = new Hashtable<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);

        getIngredients();

        recyclerView = findViewById(R.id.recyclerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        populateRecyclerView();
        enableSwipeToDeleteAndUndo();
        //enableSwipeToChangeCount();
        useSwipeHelper();
    }

    private void populateRecyclerView() {
        mAdapter = new RecyclerViewAdapter(ingredients);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP){
                    Log.e("click recyclerView", "onTouch: ");
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }


    private void enableSwipeToDeleteAndUndo() {
        swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                if(swipeToChangeCallback == null || !swipeToChangeCallback.isInAction()){
                    final int position = viewHolder.getAdapterPosition();
                    final Ingredient item = mAdapter.getData().get(position);
                    final float q = item.getQuantity();
                    mAdapter.removeItem(position);

                    Repository.getInstance().deleteIngredient(item.getName());

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Repository.getInstance().restoreIngredient(item.getName(), q);
                            mAdapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);

                        }
                    });


                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void useSwipeHelper(){
        SwipeHelper swipeHelper = new SwipeHelper(this, recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getDrawable(R.drawable.ic_plus_button),
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Ingredient item = mAdapter.getData().get(pos);
                                item.increaseQuantity(1);
                                mAdapter.changeItem(item, pos);
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getDrawable(R.drawable.ic_minus_button),
                        Color.parseColor("#FF9502"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {

                                Ingredient item = mAdapter.getData().get(pos);
                                int quantity = (int)item.getQuantity();
                                if(quantity > 0){
                                    item.increaseQuantity(-1);
                                    mAdapter.changeItem(item, pos);
                                }else{
                                    Toast.makeText(getApplicationContext(), "negative quantities are not allowed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                ));

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeHelper);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    private void getIngredients(){

        Ingredient[] ingredients = Repository.getInstance().getItemsToBuy();

        for (Ingredient ingredient: ingredients){
            if(ingredient.getQuantity() > 0){
                int pos = this.ingredients.size();
                if(lookUp.get(ingredient.getName()) == null){
                    this.ingredients.add(ingredient);
                    lookUp.put(ingredient.getName(), pos);
                }else{
                    pos = this.lookUp.get(ingredient.getName());
                    Ingredient ing = this.ingredients.get(pos);
                    ing.increaseQuantity((int)ingredient.getQuantity());
                }
            }
        }



    }

}
