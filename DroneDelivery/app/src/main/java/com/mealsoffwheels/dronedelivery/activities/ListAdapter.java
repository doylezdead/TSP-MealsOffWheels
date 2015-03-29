package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mealsoffwheels.dronedelivery.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private ArrayList<Food> foods;
    private Context context;
    private LayoutInflater layoutInflater;

    public ListAdapter(Context context, ArrayList<Food> foods) {
        this.foods = foods;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.fooditem, parent, false);

        Food food = (Food) getItem(position);

        TextView foodText = (TextView) convertView.findViewById(R.id.Item);
        foodText.setText(food.getName());



        return null;
    }

}
