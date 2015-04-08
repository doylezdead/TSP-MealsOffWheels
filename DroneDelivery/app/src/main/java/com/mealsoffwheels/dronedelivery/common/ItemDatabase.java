package com.mealsoffwheels.dronedelivery.common;

import com.mealsoffwheels.dronedelivery.R;

import java.util.*;

/**
 * Class defining prices, weights, names, and
 * images of items.
 *
 * @author Eric Kosovec
 */
public final class ItemDatabase {

    /**
     * Class that hold the price,
     * weight, and image resource id of
     * the item.
     */
    public static class ItemData {
        public int image;
        public int weight;
        public double price;

        /**
         * Constructs the representation of the
         * item.
         *
         * @param image  - ID of the image for the item.
         * @param weight - Weight in grams of the item.
         * @param price  - Price in USD of the item.
         */
        public ItemData(int image, int weight, double price) {
            this.image = image;
            this.weight = weight;
            this.price = price;
        }
    }

    /*
     * HashMap that stores an item's data.
     */
    private static Map<String, ItemData> foodCollection = Collections.unmodifiableMap(
            new HashMap<String, ItemData>() {
                {
                    put("Burrito Supreme", new ItemData(R.drawable.burrito_supreme, 240, 2.89));
                    put("XXL Grilled Stuft Burrito", new ItemData(R.drawable.xxl_stuft_burrito, 434, 3.99));
                    put("XXL Stuft Burrito", new ItemData(R.drawable.xxl_stuft_burrito, 434, 3.99));
                    put("Soft Taco", new ItemData(R.drawable.soft_taco, 92, 1.19));
                    put("Crunchy Taco", new ItemData(R.drawable.crunchy_taco, 92, 1.19));
                    put("Supreme Taco", new ItemData(R.drawable.crunchy_taco_supreme, 128, 1.59));
                    put("Mexican Pizza", new ItemData(R.drawable.mexican_pizza, 213, 3.19));
                    put("Nacho Bellgrande", new ItemData(R.drawable.nacho_bellgrande, 308, 3.49));
                    put("Chalupa Supreme", new ItemData(R.drawable.chalupa_supreme, 153, 2.19));
                    put("Chicken Quesadilla", new ItemData(R.drawable.quesadilla, 181, 3.39));
                    put("Crunchwrap Supreme", new ItemData(R.drawable.crunchwrap_supreme, 265, 2.89));
                    put("Cheesy Gordita Crunch", new ItemData(R.drawable.cheesy_gordita_crunch, 174, 2.69));
                    put("Gordita Crunch", new ItemData(R.drawable.cheesy_gordita_crunch, 174, 2.69));
                    put("Doritos Locos Tacos", new ItemData(R.drawable.doritos_locos_tacos, 78, 1.79));
                    put("Smothered Burrito", new ItemData(R.drawable.smothered_burrito, 387, 3.99));
                    put("7-Layer Burrito", new ItemData(R.drawable.layer_7_burrito, 232, 2.19));
                    put("Quesarito", new ItemData(R.drawable.quesarito, 259, 2.49));
                    put("Beefy 5-Layer Burrito", new ItemData(R.drawable.layer_5_burrito, 230, 1.69));
                    put("Double Decker Taco", new ItemData(R.drawable.double_decker_taco, 149, 1.99));

                    put("Burrito Supreme Combo", new ItemData(R.drawable.burrito_supreme, 254, 5.56));
                    put("XXL Stuft Burrito Combo", new ItemData(R.drawable.xxl_stuft_burrito, 448, 5.95));
                    put("3 Tacos Supreme Combo", new ItemData(R.drawable.crunchy_taco_supreme, 290, 5.76));
                    put("Mexican Pizza Combo", new ItemData(R.drawable.mexican_pizza, 227, 7.06));
                    put("Nacho Bellgrande Combo", new ItemData(R.drawable.nacho_bellgrande, 322, 6.16));
                    put("2 Chalupas Supreme Combo", new ItemData(R.drawable.chalupa_supreme, 320, 6.46));
                    put("Chicken Quesadilla Combo", new ItemData(R.drawable.quesadilla, 195, 5.76));
                    put("3 Tacos Combo", new ItemData(R.drawable.crunchy_taco, 290, 5.06));
                    put("Crunchwrap Supreme Combo", new ItemData(R.drawable.crunchwrap_supreme, 279, 5.46));
                    put("Cheesy Gordita Crunch Combo", new ItemData(R.drawable.cheesy_gordita_crunch, 188, 5.75));
                    put("3 Doritos Locos Tacos Combo", new ItemData(R.drawable.doritos_locos_tacos, 248, 5.55));
                    put("Smothered Burrito Combo", new ItemData(R.drawable.smothered_burrito, 401, 5.25));

                    put("Sauce", new ItemData(0, 14, 0));

                    put("Small", new ItemData(R.drawable.soda, 454, 0));
                    put("Medium", new ItemData(R.drawable.soda, 567, 0));
                    put("Large", new ItemData(R.drawable.soda, 851, 0));
                    put("XL", new ItemData(R.drawable.soda, 1134, .10));
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

    /**
     * Private constructor to prevent an
     * instantiation of the class.
     */
    private ItemDatabase() { }

    /**
     * Get the given item's data.
     *
     * @param name - Name of the item.
     *
     * @return - Class defining the image ID,
     *           weight, and price of the item.
     */
    public static ItemData getData(String name) {
        return foodCollection.get(name);
    }

}
