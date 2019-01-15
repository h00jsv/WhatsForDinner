package com.joakimsvedin.whatsfordinner.newDish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.joakimsvedin.whatsfordinner.DataHandler;
import com.joakimsvedin.whatsfordinner.Ingredient;
import com.joakimsvedin.whatsfordinner.R;
import com.joakimsvedin.whatsfordinner.Recipes.RecipesContent;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> itemName;
    private ArrayList<String> dropItems = new ArrayList<>();
    private ArrayList<String> itemCounters = new ArrayList<>();
    private ArrayList<String> counters = new ArrayList<>();
    private DataHandler dataHandler;
    private Dictionary ingredientDictionary;

    EditText inputText;
    Spinner inputSpinner;

    public CustomListAdapter(Activity context, ArrayList<String> itemName) {
        super(context, R.layout.ingredients_list, itemName);

        // TODO Auto-generated constructor stub

        this.counters.add("pieces");
        this.counters.add("g");
        this.counters.add("kg");
        this.counters.add("oz");
        this.counters.add("lb");
        this.counters.add("l");

        dataHandler = new DataHandler(context);

        this.context = context;
        this.itemName = itemName;

        ingredientDictionary = dataHandler.getIngredientDictionary();

        for (String _: itemName){
            itemCounters.add("1");
        }

        for (Enumeration i = ingredientDictionary.keys(); i.hasMoreElements();) {
            this.dropItems.add((String) i.nextElement());
        }

        this.dropItems.add("New Ingredient");
    }

    public CustomListAdapter(Activity context, RecipesContent recipesContent) {
        super(context, R.layout.ingredients_list, recipesContent.getIngredients());

        this.counters.add("pieces");
        this.counters.add("g");
        this.counters.add("kg");
        this.counters.add("oz");
        this.counters.add("lb");
        this.counters.add("l");

        this.context = context;

        this.itemName = recipesContent.getIngredients();


        dataHandler = new DataHandler(context);
        ingredientDictionary = dataHandler.getIngredientDictionary();

        EditText editText = context.findViewById(R.id.editText);
        TextView editText2 = context.findViewById(R.id.editText2);
        ImageView imageView = context.findViewById(R.id.imageView);

        editText.setText(recipesContent.getName());
        editText2.setText(recipesContent.getDirections());
        imageView.setImageBitmap(recipesContent.getImage());

        itemCounters = new ArrayList<>();
        itemCounters.addAll(recipesContent.getCounters());


        for (Enumeration i = ingredientDictionary.keys(); i.hasMoreElements();) {
            this.dropItems.add((String) i.nextElement());
        }

        this.dropItems.add("New Ingredient");
    }

    public ArrayList<String> getItemName(){
        ArrayList<String> items = new ArrayList<>();
        items.addAll(itemName);
        items.remove(items.size()-1);

        return items;
    }

    public void safe(){
        EditText editText = context.findViewById(R.id.editText);
        ImageView imageView = context.findViewById(R.id.imageView);
        EditText editText2 = context.findViewById(R.id.editText2);
        String name = editText.getText().toString();

        Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        String directions = editText2.getText().toString();

        dataHandler.safeDish(name, image, getItemName(), itemCounters, directions);

        dataHandler.safeIngredients(ingredientDictionary);
    }

    public void setItemName(List<String> items){
        itemName.clear();
        itemName.addAll(items);
        itemName.add("");

        notifyDataSetChanged();
        ListView listView = context.findViewById(R.id.listView);
        setListViewHeightBasedOnChildren(listView);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private View getInput(){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        inputText = new EditText(context);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        inputText.setWidth(300);
        inputText.setId(R.id.inputText);


        inputSpinner = new Spinner(context);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, counters);
        inputSpinner.setAdapter(spinnerAdapter);
        inputSpinner.setLayoutParams(layoutParams);
        inputSpinner.setId(R.id.inputSpinner);


        linearLayout.addView(inputText);
        linearLayout.addView(inputSpinner);

        return linearLayout;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.ingredients_list, null,true);

        final CustomSpinner dropDown = rowView.findViewById(R.id.dropMenu);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, dropItems);
        dropDown.setDefaultValue(itemName.get(position));
        dropDown.setAdapter(spinnerAdapter);

        String unitRow = (String)ingredientDictionary.get(itemName.get(position));
        final TextView textViewCounters = rowView.findViewById(R.id.textViewCounters);
        textViewCounters.setText(unitRow);




        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int positionSpinner, long arg3) {

                String item = dropDown.getSelectedItem().toString();
                String unit = (String)ingredientDictionary.get(item);

                final ListView listView = context.findViewById(R.id.listView);


                if (positionSpinner == dropItems.size()-1){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("New Ingredient");

                    builder.setView(getInput());

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String mText = inputText.getText().toString();
                            int pos = inputSpinner.getSelectedItemPosition();

                            itemName.set(position, mText);
                            //itemCounters.set(position, counters.get(pos));
                            ingredientDictionary.put(mText, counters.get(pos));

                            if (dropDown.getIsDefault()) {
                                if (itemName.size() < 10) {
                                    itemName.add("");
                                    itemCounters.add("1");
                                }
                            }

                            dropItems.add(dropItems.size()-1, mText);

                            notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(listView);

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(listView);
                            dialog.cancel();
                        }
                    });

                    builder.show();

                }else {

                    itemName.set(position, item);
                    //itemCounters.set(position, unit);
                    if (dropDown.getIsDefault()){
                        if (itemName.size() < 10) {
                            itemName.add("");
                            itemCounters.add("1");
                        }
                        notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(listView);
                    }

                    dropDown.setIsDefault(false);
                    textViewCounters.setText(unit);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        final EditText editText = rowView.findViewById(R.id.editCount);

        editText.setText(itemCounters.get(position));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                itemCounters.set(position, s.toString());
            }
        });


        return rowView;

    }


}