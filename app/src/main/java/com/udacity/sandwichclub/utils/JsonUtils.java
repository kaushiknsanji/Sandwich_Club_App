package com.udacity.sandwichclub.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that parses the Sandwich details JSON
 * and prepares the {@link Sandwich} object
 *
 * @author Kaushik N Sanji
 */
public class JsonUtils {

    //Constant used for logs
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    /**
     * Method that parses the JSON string passed
     * and generates a {@link Sandwich} object
     *
     * @param json is the JSON String to be parsed
     * @return A {@link Sandwich} object of Sandwich details if parsed successfully; NULL if failed
     */
    @Nullable
    public static Sandwich parseSandwichJson(String json) {
        //Initializing the Sandwich Builder object to prepare the Sandwich
        Sandwich.SandwichBuilder builder = new Sandwich.SandwichBuilder();

        //Parsing the JSON String to build the Sandwich Object: START
        try {
            //Preparing the JSON object of the JSON String
            JSONObject rootJsonObject = new JSONObject(json);

            //Finding the 'name' JSON object
            JSONObject nameJsonObject = rootJsonObject.getJSONObject("name");

            //Retrieving the 'mainName' JSON Attribute
            builder.setMainName(nameJsonObject.getString("mainName"));

            //Retrieving and parsing the 'alsoKnownAs' JSON Array: START
            List<String> alsoKnownAsList = new ArrayList<>();
            JSONArray alsoKnownAsArray = nameJsonObject.getJSONArray("alsoKnownAs");
            //Number of Strings in the Array
            int alsoKnownAsArrayLength = alsoKnownAsArray.length();
            //Iterating over the JSON Array to build the list
            for (int index = 0; index < alsoKnownAsArrayLength; index++) {
                alsoKnownAsList.add(alsoKnownAsArray.getString(index).trim());
            }
            builder.setAlsoKnownAs(alsoKnownAsList); //Updating to Builder
            //Retrieving and parsing the 'alsoKnownAs' JSON Array: END

            //Retrieving the 'placeOfOrigin' JSON Attribute
            builder.setPlaceOfOrigin(rootJsonObject.getString("placeOfOrigin"));

            //Retrieving the 'description' JSON Attribute
            builder.setDescription(rootJsonObject.getString("description"));

            //Retrieving the 'image' JSON Attribute
            builder.setImage(rootJsonObject.getString("image"));

            //Retrieving and parsing the 'ingredients' JSON Array: START
            List<String> ingredientsList = new ArrayList<>();
            JSONArray ingredientsArray = rootJsonObject.getJSONArray("ingredients");
            //Number of Strings in the Array
            int ingredientsArrayLength = ingredientsArray.length();
            //Iterating over the JSON Array to build the list
            for (int index = 0; index < ingredientsArrayLength; index++) {
                ingredientsList.add(ingredientsArray.getString(index).trim());
            }
            builder.setIngredients(ingredientsList); //Updating to Builder
            //Retrieving and parsing the 'ingredients' JSON Array: END

        } catch (JSONException e) {
            Log.e(LOG_TAG, "parseSandwichJson: Error occurred while parsing the JSON String", e);
            builder = null;
        }
        //Parsing the JSON String to build the Sandwich Object: END

        //Returning Null when it failed to parse the content
        if (builder == null) {
            return null;
        }

        //Returning the prepared Sandwich object
        return builder.createSandwich();
    }
}
