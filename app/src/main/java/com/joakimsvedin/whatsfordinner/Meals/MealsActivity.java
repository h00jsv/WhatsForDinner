package com.joakimsvedin.whatsfordinner.Meals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.joakimsvedin.whatsfordinner.MainActivity;
import com.joakimsvedin.whatsfordinner.R;
import com.joakimsvedin.whatsfordinner.Recipes.Recipes;
import com.joakimsvedin.whatsfordinner.Repository;
import com.joakimsvedin.whatsfordinner.newDish.NewDishActivity;

import java.util.List;

public class MealsActivity extends AppCompatActivity {

    String[] weekDays = {"Monday", "Tuesday","Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"};
    String[] mealTypes = {"Breakfast", "Lunch","Dinner"};

    Button[] buttons = new Button[7*3];

    Repository repository = Repository.getInstance();

    String tempValue = "";

    public void backToMain(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hides the Action Bar from the Emulator
        setContentView(R.layout.activity_meals);

        createView();

        // retrieve the intent variable passed from MainActivity
        //Intent intent = getIntent();
        // show it with a Toast
        //Toast.makeText(this, intent.getStringExtra("username"), Toast.LENGTH_SHORT).show();
    }

    private int getDP(int px){
        float d = this.getResources().getDisplayMetrics().density;
        return (int)(px*d*3/4);
    }


    private void createView(){
        LinearLayout linearLayout = findViewById(R.id.linearLayoutMeals);

        int buttonCount = 0;
        for (final String day: weekDays){
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout1.setOrientation(LinearLayout.VERTICAL);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMarginStart(getDP(16));
            params.topMargin = getDP(20);

            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setText(day);
            textView.setTextSize(getDP(18));



            linearLayout1.addView(textView);
            for (final String type: mealTypes){
                LinearLayout linearLayout2 = new LinearLayout(this);
                linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout2.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(getDP(140), getDP(50));

                params2.setMarginStart(getDP(32));
                params2.topMargin = getDP(4);

                TextView textView2 = new TextView(this);
                textView2.setLayoutParams(params2);
                textView2.setText(type);
                textView2.setTextSize(getDP(13));

                params2 = new LinearLayout.LayoutParams(getDP(240), getDP(50));

                params2.topMargin = getDP(7);


                buttons[buttonCount] = new Button(this);
                buttons[buttonCount].setLayoutParams(params2);

                final int d = buttonCount/3;
                final int t = buttonCount - d*3;

                String meal = Repository.getInstance().getMealsChosen(d, t);

                buttons[buttonCount].setText(meal);
                buttons[buttonCount].setTransformationMethod(null);

                final int buttonId = buttonCount;
                buttons[buttonCount].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), RecipesList.class);
                        tempValue = (String)buttons[buttonId].getText();
                        if(!tempValue.equals("eaten out")){
                            repository.incrementMealCount(tempValue);
                        }
                        Repository.resetMealsChosen(d, t);
                        startActivityForResult(i, buttonId);
                    }
                });

                linearLayout2.addView(textView2);
                linearLayout2.addView(buttons[buttonCount]);
                linearLayout1.addView(linearLayout2);

                buttonCount++;
            }

            linearLayout.addView(linearLayout1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int day = requestCode/3;
        int type = requestCode - day*3;

        if (requestCode < 7*3 && resultCode != -1) {

            String meal = data.getStringExtra("meal");

            buttons[requestCode].setText(meal);

            repository.setMealsChosen(day, type, meal);
            repository.decrementMealCount(meal);

        }

        if (resultCode == -1){
            buttons[requestCode].setTag(tempValue);
            repository.decrementMealCount(tempValue);
            Repository.getInstance().setMealsChosen(day, type, tempValue);
        }
    }

}
