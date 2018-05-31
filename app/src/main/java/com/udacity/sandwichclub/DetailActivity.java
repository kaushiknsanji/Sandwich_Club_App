package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

/**
 * The Details Activity of the App that inflates the layout 'R.layout.activity_detail'
 * and displays the Sandwich details for the Sandwich item selected
 * in the {@link MainActivity}
 *
 * @author Kaushik N Sanji
 */
public class DetailActivity extends AppCompatActivity {

    //Constants used as the Intent EXTRA for passing the item clicked position from the MainActivity
    public static final String EXTRA_POSITION = "extra_position";
    //Constant for the item clicked position, defaulted to -1 (invalid)
    private static final int DEFAULT_POSITION = -1;

    //String Constants used for building up the Text content for List data
    private static final String QUOTE_MARK = "\"";
    private static final String BULLET_MARK = "\u2022";
    private static final String NEW_LINE = "\n";
    private static final String WHITESPACE = " ";
    private static final String SEMICOLON_DELIMITER = ";";

    //For Caching the Views
    private ImageView mSandwichImageView;
    private TextView mAkaLabelTextView;
    private TextView mAkaTextView;
    private TextView mOriginLabelTextView;
    private TextView mOriginTextView;
    private TextView mIngredientsLabelTextView;
    private TextView mIngredientsTextView;
    private TextView mDescriptionLabelTextView;
    private TextView mDescriptionTextView;
    private ConstraintLayout mSandwichContentLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    //For Generating the Color Palette Asynchronously
    private Palette.PaletteAsyncListener mPaletteAsyncListener = new Palette.PaletteAsyncListener() {
        @Override
        public void onGenerated(@NonNull Palette palette) {
            //Get the Light Vibrant/Muted Swatch
            Palette.Swatch lightSwatch = palette.getLightVibrantSwatch();
            if (lightSwatch == null) {
                //Selecting Light Muted Swatch when Light Vibrant Swatch is not available
                lightSwatch = palette.getLightMutedSwatch();
            }

            //Setting all Label Text's Color to Title Text Color from any of the Light Swatches
            int titleTextColor = lightSwatch.getTitleTextColor();
            mAkaLabelTextView.setTextColor(titleTextColor);
            mOriginLabelTextView.setTextColor(titleTextColor);
            mIngredientsLabelTextView.setTextColor(titleTextColor);
            mDescriptionLabelTextView.setTextColor(titleTextColor);

            //Setting all Content Text's Color to Body Text Color from any of the Light Swatches
            int bodyTextColor = lightSwatch.getBodyTextColor();
            mAkaTextView.setTextColor(bodyTextColor);
            mOriginTextView.setTextColor(bodyTextColor);
            mIngredientsTextView.setTextColor(bodyTextColor);
            mDescriptionTextView.setTextColor(bodyTextColor);

            //Setting the Background color for the content from any of the Light Swatches
            mSandwichContentLayout.setBackgroundColor(lightSwatch.getRgb());

            //Setting the Colors from Vibrant Swatch to Toolbar and Status Bar
            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
            if (vibrantSwatch != null) {
                //Applying only when Vibrant Swatch is present
                mCollapsingToolbarLayout.setContentScrimColor(vibrantSwatch.getRgb());
                mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(
                        ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
            }
        }
    };
    //The Target for the Bitmap loaded through Picasso
    private Target mBitmapTarget = new Target() {

        //Called when the Bitmap is downloaded successfully
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //Loading the Bitmap downloaded
            mSandwichImageView.setImageBitmap(bitmap);

            //Generating the Color Palette for use
            Palette.from(bitmap).generate(mPaletteAsyncListener);
        }

        //Called when the Bitmap failed to download
        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            //Loading the Error Drawable
            mSandwichImageView.setImageDrawable(errorDrawable);

            //Converting the Drawable into Bitmap: START
            Bitmap bitmap = Bitmap.createBitmap(errorDrawable.getIntrinsicWidth(),
                    errorDrawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            errorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            errorDrawable.draw(canvas);
            //Converting the Drawable into Bitmap: END

            //Generating the Color Palette for use
            Palette.from(bitmap).generate(mPaletteAsyncListener);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            //no-op
        }
    };

    //Called when the Activity is to be created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Finding the Views for later use
        findViews();

        //Reading the Intent passed from the MainActivity
        Intent intent = getIntent();
        if (intent == null) {
            //Finishing the Activity with an Error message when no Intent data is present
            closeOnError();
        }

        //Reading the Intent EXTRA_POSITION for the List Item clicked position
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent, finishing the Activity with an Error message
            closeOnError();
            return;
        }

        //Loading the details of the Sandwich selected
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        //Parsing the Sandwich details
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable, finishing the Activity with an Error message
            closeOnError();
            return;
        }

        //Initializing the Toolbar as ActionBar and setting the title
        setupToolbar(sandwich.getMainName());

        //Populating the UI with the parsed Sandwich data
        populateUI(sandwich);
    }

    /**
     * Method that initializes the Toolbar as ActionBar
     * and sets the Title
     *
     * @param titleName Name of the Sandwich to be set as the toolbar title
     */
    private void setupToolbar(String titleName) {
        //Setting the Toolbar as the ActionBar
        setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));

        //Retrieving the Action Bar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            //Removing the default title text
            supportActionBar.setDisplayShowTitleEnabled(false);
            //Enabling home button to be used for Up navigation
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            //Enabling home button
            supportActionBar.setHomeButtonEnabled(true);
        }

        //Setting the Title on CollapsingToolbarLayout
        mCollapsingToolbarLayout.setTitle(titleName);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handling the Menu Item selected based on their Id
        switch (item.getItemId()) {
            case android.R.id.home:
                //Handling the action bar's home/up button
                //Finishing the Activity
                closeActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method that finishes the Activity
     * with transitions if supported
     */
    private void closeActivity() {
        //Checking if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Finish after playing transition
            finishAfterTransition();
        } else {
            //Finish without transition
            finish();
        }
    }

    /**
     * Finds the required Views from the layout for later use
     */
    private void findViews() {
        mSandwichImageView = findViewById(R.id.image_iv);
        mAkaLabelTextView = findViewById(R.id.also_known_as_label);
        mAkaTextView = findViewById(R.id.also_known_tv);
        mOriginLabelTextView = findViewById(R.id.place_of_origin_label);
        mOriginTextView = findViewById(R.id.origin_tv);
        mIngredientsLabelTextView = findViewById(R.id.ingredients_label);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);
        mDescriptionLabelTextView = findViewById(R.id.description_label);
        mDescriptionTextView = findViewById(R.id.description_tv);
        mSandwichContentLayout = findViewById(R.id.sandwich_content_layout);
        mCollapsingToolbarLayout = findViewById(R.id.detail_collapsing_toolbar);
    }

    /**
     * Method that finishes the Activity with an Error message toast.
     * Invoked when the Intent data is not present.
     */
    private void closeOnError() {
        closeActivity();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method that populates the UI elements with the data from
     * the {@link Sandwich} object
     *
     * @param sandwich The Parsed {@link Sandwich} object data
     */
    private void populateUI(Sandwich sandwich) {
        //Loading the Image of Sandwich
        Picasso.with(this)
                .load(sandwich.getImage())              //The URL of the Image to download
                .error(R.drawable.detail_error_image)   //The Drawable to use on Error
                .into(mBitmapTarget);                   //The Bitmap Target

        //Populating the AKA Content
        populateAkaText(sandwich.getAlsoKnownAs());

        //Populating the Origin Content
        populateOriginText(sandwich.getPlaceOfOrigin());

        //Populating the Ingredients Content
        populateIngredientsText(sandwich.getIngredients());

        //Populating the Description Content
        populateDescriptionText(sandwich.getDescription());
    }

    /**
     * Method that populates the content for
     * "Also known as" TextView (R.id.also_known_tv)
     *
     * @param alsoKnownAsList is a List of Strings containing the various names for the Sandwich.
     *                        List can be empty.
     */
    private void populateAkaText(List<String> alsoKnownAsList) {
        //Retrieving the List Size
        int contentSize = alsoKnownAsList.size();

        if (contentSize > 0) {
            //When the List has content

            //Starts a StringBuilder with a Quote '"'
            StringBuilder akaTextBuilder = new StringBuilder(QUOTE_MARK);
            //Joins all the Strings in the list with a ';' and appends '"' in the end
            akaTextBuilder.append(TextUtils.join(SEMICOLON_DELIMITER, alsoKnownAsList)).append(QUOTE_MARK);

            //The new String that replaces the delimiters
            String newReplacementStr = QUOTE_MARK + NEW_LINE + QUOTE_MARK;
            //Length of the new String
            int newStringLength = newReplacementStr.length();
            //Length of the delimiter
            int delimiterLength = SEMICOLON_DELIMITER.length();

            //Finding the index of the first occurrence of ';' delimiter
            int lookupIndex = akaTextBuilder.indexOf(SEMICOLON_DELIMITER, 0);
            //Looping till the last delimiter is found
            while (lookupIndex > -1) {
                //Replacing the ';' delimiter with the replacement string
                akaTextBuilder.replace(lookupIndex, lookupIndex + delimiterLength, newReplacementStr);
                //Moving the lookupIndex ahead of the replaced string
                lookupIndex = lookupIndex + newStringLength;
                //Finding the index of the next occurrence of ';' delimiter
                lookupIndex = akaTextBuilder.indexOf(SEMICOLON_DELIMITER, lookupIndex);
            }

            //Setting the final text built
            mAkaTextView.setText(akaTextBuilder.toString());

        } else {
            //When the List is empty

            //Hiding the corresponding views by hiding the Constraint Group
            findViewById(R.id.detail_group_also_known_as).setVisibility(View.GONE);
        }
    }

    /**
     * Method that populates the content for
     * "Place of Origin" TextView (R.id.origin_tv)
     *
     * @param placeOfOrigin is a String containing the "Place of Origin" information
     *                      of the Sandwich. This can be empty.
     */
    private void populateOriginText(String placeOfOrigin) {
        if (!TextUtils.isEmpty(placeOfOrigin)) {
            //When the Place of Origin info is present

            //Setting the Text Content
            mOriginTextView.setText(placeOfOrigin);
        } else {
            //When the Place of Origin info is absent

            //Setting the Text Content to Unknown
            mOriginTextView.setText(R.string.detail_unknown_origin);
        }
    }

    /**
     * Method that populates the content for
     * "Ingredients" TextView (R.id.ingredients_tv)
     *
     * @param ingredientsList is a List of Strings containing the ingredients
     *                        required for preparing the particular Sandwich.
     *                        List can be empty.
     */
    private void populateIngredientsText(List<String> ingredientsList) {
        //Retrieving the List size
        int contentSize = ingredientsList.size();

        if (contentSize > 0) {
            //When the List has content

            //Starts a StringBuilder with a Bullet Mark
            StringBuilder ingredientsTextBuilder = new StringBuilder(BULLET_MARK);
            //Appends a Whitespace character
            ingredientsTextBuilder.append(WHITESPACE);
            //Joins all the Strings in the list with a ';'
            ingredientsTextBuilder.append(TextUtils.join(SEMICOLON_DELIMITER, ingredientsList));

            //The new String that replaces the delimiters
            String newReplacementStr = NEW_LINE + BULLET_MARK + WHITESPACE;
            //Length of the new String
            int newStringLength = newReplacementStr.length();
            //Length of the delimiter
            int delimiterLength = SEMICOLON_DELIMITER.length();

            //Finding the index of the first occurrence of ';' delimiter
            int lookupIndex = ingredientsTextBuilder.indexOf(SEMICOLON_DELIMITER, 0);
            while (lookupIndex > -1) {
                //Replacing the ';' delimiter with the replacement string
                ingredientsTextBuilder.replace(lookupIndex, lookupIndex + delimiterLength, newReplacementStr);
                //Moving the lookupIndex ahead of the replaced string
                lookupIndex = lookupIndex + newStringLength;
                //Finding the index of the next occurrence of ';' delimiter
                lookupIndex = ingredientsTextBuilder.indexOf(SEMICOLON_DELIMITER, lookupIndex);
            }

            //Setting the final text built
            mIngredientsTextView.setText(ingredientsTextBuilder.toString());

        } else {
            //When the List is empty

            //Setting the Text Content to Unknown
            mIngredientsTextView.setText(R.string.detail_unknown_ingredients);
        }
    }

    /**
     * Method that populates the content for
     * "Description" TextView (R.id.description_tv)
     *
     * @param description is the Description content to be set for the Sandwich.
     */
    private void populateDescriptionText(String description) {
        //Setting the Text Content
        mDescriptionTextView.setText(description);
    }

}