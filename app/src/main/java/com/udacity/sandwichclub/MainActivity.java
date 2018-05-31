package com.udacity.sandwichclub;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * The Main Activity of the App that inflates the layout 'R.layout.activity_main'
 * and displays a list of Sandwiches loaded from the String resource.
 *
 * @author Kaushik N Sanji
 */
public class MainActivity extends AppCompatActivity {

    //Called when the Activity is to be created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Loading the Interpolator for Animating the list item views in
        final LinearOutSlowInInterpolator interpolator = new LinearOutSlowInInterpolator();

        //Retrieving the initial offset-y translation for the list items
        final float initialOffsetY = getResources().getDimension(R.dimen.main_list_items_offset_y_translation);

        //Reading the data for the ListView items
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_names);
        //Setting up the ListView Adapter with the data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sandwiches) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                //Capturing the item view created
                View view = super.getView(position, convertView, parent);

                //Calculating the start offset-y translation of this list item view
                float currentViewOffset = initialOffsetY * (float) Math.pow(1.5, position);

                //Setting the calculated offset
                view.setTranslationY(currentViewOffset);
                //Setting the initial Alpha
                view.setAlpha(0.75f);

                //Animating back to natural position using ViewPropertyAnimation
                view.animate()
                        .alpha(1f)                      //Full Opaque
                        .translationY(0f)               //Default natural position
                        .setInterpolator(interpolator)
                        .setDuration(1000L)             //1 second duration
                        .setStartDelay(15L)             //Start delay of 15ms
                        .start();

                //Returning the item view
                return view;
            }

        };

        //Finding the ListView
        ListView listView = findViewById(R.id.sandwiches_listview);
        //Setting the adapter on ListView
        listView.setAdapter(adapter);
        //Registering the Click listener on the item to load the DetailActivity on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);
            }
        });

    }

    /**
     * Method invoked when an item in the ListView is clicked.
     * This method prepares an Intent to launch the {@link DetailActivity} passing in the
     * item position being clicked.
     *
     * @param position is the Integer position of the View in the ListView adapter being clicked
     */
    private void launchDetailActivity(int position) {
        //Preparing the Intent to DetailActivity
        Intent intent = new Intent(this, DetailActivity.class);
        //Adding the position information to the Intent
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);

        //Checking if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Apply activity transition

            //Creating ActivityOptions to transition between the Activities
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            //Starting DetailActivity passing in the above Bundle for Animation
            startActivity(intent, bundle);

        } else {
            //Swap without transition
            startActivity(intent);
        }
    }

}
