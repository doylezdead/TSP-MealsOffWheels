package com.mealsoffwheels.dronedelivery.activities;

import android.content.Context;
import android.widget.ImageView;

public class MenuImageView extends ImageView {

    public MenuImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

}
