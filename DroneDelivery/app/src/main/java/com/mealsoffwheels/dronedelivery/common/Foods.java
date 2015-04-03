package com.mealsoffwheels.dronedelivery.common;

import com.mealsoffwheels.dronedelivery.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Foods {

    public static class FoodData {
        public int image;
        public int weight;

        public FoodData(int image, int weight) {
            this.image = image;
            this.weight = weight;
        }
    }

    private static Map<String, FoodData> foodCollection = Collections.unmodifiableMap(
            new HashMap<String, FoodData>() {
                {
                    put("Burrito Supreme", new FoodData(R.drawable.burrito_supreme, 240));
                    put("XXL Grilled Stuft Burrito", new FoodData(0, 434));
                    put("XXL Stuft Burrito", new FoodData(0, 434));
                    put("Soft Taco", new FoodData(0, 92));
                    put("Crunchy Taco", new FoodData(0, 92));
                    put("Supreme Taco", new FoodData(0, 128));
                    put("Mexican Pizza", new FoodData(0, 213));
                    put("Nacho Bellgrande", new FoodData(0, 308));
                    put("Chalupa Supreme", new FoodData(0, 153));
                    put("Chicken Quesadilla", new FoodData(0, 181));
                    put("Crunchwrap Supreme", new FoodData(0, 265));
                    put("Cheesy Gordita Crunch", new FoodData(0, 174));
                    put("Gordita Crunch", new FoodData(0, 174));
                    put("Doritos Locos Tacos", new FoodData(0, 78));
                    put("Smothered Burrito", new FoodData(0, 387));
                    put("7-Layer Burrito", new FoodData(0, 232));
                    put("Quesarito", new FoodData(0, 259));
                    put("Beefy 5-Layer Burrito", new FoodData(0, 230));
                    put("Double Decker Taco", new FoodData(0, 149));

                    put("Burrito Supreme Combo", new FoodData(0, 254));
                    put("XXL Stuft Burrito Combo", new FoodData(0, 448));
                    put("3 Tacos Supreme Combo", new FoodData(0, 290));
                    put("Mexican Pizza Combo", new FoodData(0, 227));
                    put("Nacho Bellgrande Combo", new FoodData(0, 322));
                    put("2 Chalupas Supreme Combo", new FoodData(0, 320));
                    put("Chicken Quesadilla Combo", new FoodData(0, 195));
                    put("3 Tacos Combo", new FoodData(0, 290));
                    put("Crunchwrap Supreme Combo", new FoodData(0, 279));
                    put("Cheesy Gordita Crunch Combo", new FoodData(0, 188));
                    put("3 Doritos Locos Tacos Combo", new FoodData(0, 248));
                    put("Smothered Burrito Combo", new FoodData(0, 401));

                    put("Sauce", new FoodData(0, 14));

                    put("Small", new FoodData(0, 454));
                    put("Medium", new FoodData(0, 567));
                    put("Large", new FoodData(0, 851));
                    put("XL", new FoodData(0, 1134));
                }
            }
    );

    public final static String[] burritos = {
        "Burrito Supreme", "Smothered Burrito", "XXL Grilled Stuft Burrito",
            "7-Layer Burrito", "Quesarito", "Beefy 5-Layer Burrito"
    };

    public final static String[] tacos = {
        "Soft Taco", "Crunchy Taco", "Doritos Locos Tacos", "Cheesy Gordita Crunch",
            "Double Decker Taco"
    };

    public final static String[] specialties = {
        "Cheesy Gordita Crunch", "Chicken Quesadilla", "Mexican Pizza", "Nacho Bellgrande",
            "Chalupa Supreme", "Crunchwrap Supreme", "Gordita Crunch", "XXL Stuft Burrito"
    };

    public final static String[] drinks = {
        "Pepsi", "Diet Pepsi", "Mountain Dew", "Mtn. Dew Baja Blast", "Sierra Mist",
            "Dr. Pepper", "Brisk Iced Tea"
    };

    public final static String[] combos = {
        "Burrito Supreme Combo", "XXL Stuft Burrito Combo", "3 Tacos Supreme Combo",
            "Mexican Pizza Combo", "Nacho Bellgrande Combo", "2 Chalupas Supreme Combo",
            "Chicken Quesadilla Combo", "3 Tacos Combo", "Crunchwrap Supreme Combo",
            "Cheesy Gordita Crunch Combo", "3 Doritos Locos Tacos Combo", "Smothered Burrito Combo"
    };

    public static FoodData getData(String name) {
        return foodCollection.get(name);
    }

    private Foods() { }

}
