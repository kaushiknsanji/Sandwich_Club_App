package com.udacity.sandwichclub.model;

import java.util.List;

/**
 * Model class for storing the Sandwich data parsed,
 * which is displayed in the {@link com.udacity.sandwichclub.DetailActivity}
 *
 * @author Kaushik N Sanji
 */
public class Sandwich {

    //Stores the name of the Sandwich
    private String mainName;
    //Stores a List of Strings containing alternate names of the Sandwich
    private List<String> alsoKnownAs = null;
    //Stores the 'Place of origin' information of the Sandwich
    private String placeOfOrigin;
    //Stores the 'Description' of the Sandwich
    private String description;
    //Stores the URL to the image of Sandwich
    private String image;
    //Stores a List of Strings containing the 'Ingredients' information of the Sandwich
    private List<String> ingredients = null;

    /**
     * Private Constructor to prevent direct instantiation
     * and to enforce the use of Builder {@link SandwichBuilder}
     */
    private Sandwich() {
    }

    //Private Constructor used by the SandwichBuilder to create an instance of Sandwich object
    private Sandwich(String mainName, List<String> alsoKnownAs, String placeOfOrigin,
                     String description, String image, List<String> ingredients) {
        this.mainName = mainName;
        this.alsoKnownAs = alsoKnownAs;
        this.placeOfOrigin = placeOfOrigin;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
    }

    /**
     * Getter for {@link #mainName}
     *
     * @return String representing the name of the Sandwich
     */
    public String getMainName() {
        return mainName;
    }

    /**
     * Getter for {@link #alsoKnownAs}
     *
     * @return List of Strings containing alternate names of the Sandwich
     */
    public List<String> getAlsoKnownAs() {
        return alsoKnownAs;
    }

    /**
     * Getter for {@link #placeOfOrigin}
     *
     * @return String containing the 'Place of origin' information of the Sandwich
     */
    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    /**
     * Getter for {@link #description}
     *
     * @return String containing the 'Description' of the Sandwich
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for {@link #image}
     *
     * @return String containing the URL to the image of Sandwich
     */
    public String getImage() {
        return image;
    }

    /**
     * Getter for {@link #ingredients}
     *
     * @return List of Strings containing the 'Ingredients' information of the Sandwich
     */
    public List<String> getIngredients() {
        return ingredients;
    }

    /**
     * The Builder class that constructs {@link Sandwich}
     */
    public static class SandwichBuilder {
        //Declaring the same members of Sandwich
        private String mMainName;
        private List<String> mAlsoKnownAs = null;
        private String mPlaceOfOrigin;
        private String mDescription;
        private String mImage;
        private List<String> mIngredients = null;

        /**
         * Setter for {@link SandwichBuilder#mMainName}
         *
         * @param mainName is the Name of the Sandwich
         * @return Instance of {@link SandwichBuilder}
         */
        public SandwichBuilder setMainName(String mainName) {
            mMainName = mainName;
            return this;
        }

        /**
         * Setter for {@link SandwichBuilder#mAlsoKnownAs}
         *
         * @param alsoKnownAs is a List of Strings containing alternate names of the Sandwich
         * @return Instance of {@link SandwichBuilder}
         */
        public SandwichBuilder setAlsoKnownAs(List<String> alsoKnownAs) {
            mAlsoKnownAs = alsoKnownAs;
            return this;
        }

        /**
         * Setter for {@link SandwichBuilder#mPlaceOfOrigin}
         *
         * @param placeOfOrigin is the 'Place of origin' information of the Sandwich
         * @return Instance of {@link SandwichBuilder}
         */
        public SandwichBuilder setPlaceOfOrigin(String placeOfOrigin) {
            mPlaceOfOrigin = placeOfOrigin;
            return this;
        }

        /**
         * Setter for {@link SandwichBuilder#mDescription}
         *
         * @param description is the 'Description' of the Sandwich
         * @return Instance of {@link SandwichBuilder}
         */
        public SandwichBuilder setDescription(String description) {
            mDescription = description;
            return this;
        }

        /**
         * Setter for {@link SandwichBuilder#mImage}
         *
         * @param image is the String URL to the image of Sandwich
         * @return Instance of {@link SandwichBuilder}
         */
        public SandwichBuilder setImage(String image) {
            mImage = image;
            return this;
        }

        /**
         * Setter for {@link SandwichBuilder#mIngredients}
         *
         * @param ingredients is a List of Strings containing the 'Ingredients' information of the Sandwich
         * @return Instance of {@link SandwichBuilder}
         */
        public SandwichBuilder setIngredients(List<String> ingredients) {
            mIngredients = ingredients;
            return this;
        }

        /**
         * Method that generates the {@link Sandwich}
         * from the Builder parameters set
         *
         * @return Instance of {@link Sandwich}
         */
        public Sandwich createSandwich() {
            return new Sandwich(mMainName, mAlsoKnownAs, mPlaceOfOrigin, mDescription, mImage, mIngredients);
        }
    }
}
