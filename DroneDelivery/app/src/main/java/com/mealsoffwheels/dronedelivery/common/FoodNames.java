package com.mealsoffwheels.dronedelivery.common;

public final class FoodNames {

    public final static String[] names = {
        "Burrito Supreme", "XXL Grilled Stuft Burrito", "3 Tacos Supreme",
        "Mexican Pizza", "Nachos Bellgrande", "2 Chalupas Supreme",
        "Quesadilla", "3 Tacos", "Crunchwrap Supreme", "Cheesy Gordita Crunch",
        "3 Doritos Locos Tacos", "Smothered Burrito"
    };

    private FoodNames() {}

    public static String getFoodName(int number) {
        if (number == 0 || number - 1 > names.length) {
            return "";
        }

        return names[number - 1];
    }

    public static int numberOfNames() {
        return names.length;
    }

}
