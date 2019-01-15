package com.joakimsvedin.whatsfordinner.Groceries;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.joakimsvedin.whatsfordinner.Ingredient;
import com.joakimsvedin.whatsfordinner.R;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Ingredient> data;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mValue;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.txtTitle);
            mValue = itemView.findViewById(R.id.txtTitle2);

        }
    }

    public RecyclerViewAdapter(ArrayList<Ingredient> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTitle.setText(data.get(position).getName());
        int q = (int)data.get(position).getQuantity();
        String u = data.get(position).getUnit();
        if(q >= 10){
            holder.mValue.setText(Integer.toString(q)+" "+u);
        }else{
            holder.mValue.setText(" "+Integer.toString(q)+" "+u);
        }

        if(q == 0){
            holder.mTitle.setPaintFlags(holder.mTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.mTitle.setPaintFlags(holder.mValue.getPaintFlags());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Ingredient item, int position) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    public void changeItem(Ingredient item, int position) {
        data.set(position, item);
        notifyItemChanged(position);
    }

    public ArrayList<Ingredient> getData() {
        return data;
    }
}



