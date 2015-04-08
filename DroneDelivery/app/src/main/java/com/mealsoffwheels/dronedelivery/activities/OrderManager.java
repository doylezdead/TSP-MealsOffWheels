package com.mealsoffwheels.dronedelivery.activities;

import android.content.SharedPreferences;

public class OrderManager {

    public static final String ORDER_PREFS = "com.mealsoffwheels.dronedelivery.values";

    private static final String QUANTITY_SUBSTRING = "quantity";
    private static final String LAST_ORDER = "Last";

    private static final int NO_ORDERS = 0;
    private static final int NO_VARIABLE_DEFINED = -1;

    private OrderManager() {
    }

    public static boolean isLastDefined(SharedPreferences prefs) {
        return prefs.getInt(LAST_ORDER, -1) != -1;
    }

    public static void defineLast(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LAST_ORDER, 0);
        editor.commit();
    }

}
