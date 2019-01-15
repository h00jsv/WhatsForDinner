package com.joakimsvedin.whatsfordinner.Meals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.joakimsvedin.whatsfordinner.R;
import com.joakimsvedin.whatsfordinner.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class RecipesList extends AppCompatActivity {

    Repository repository = Repository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_list);

        final List<String> list = Arrays.asList(repository.getMealsAvailable());

        Collections.sort(list);

        int i = 0;
        for(String name: list){
            Integer count = repository.getMealCount(name);
            if(count != null && count != 0){
                list.set(i, name+" ("+count.toString()+"x)");
            }

            ++i;
        }

        ListView listView = findViewById(R.id.recipe_list);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                List<String> list = Arrays.asList(repository.getMealsAvailable());
                Collections.sort(list);
                intent.putExtra("meal", list.get(i));
                setResult(i, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(-1, intent);
        finish();
    }
}
