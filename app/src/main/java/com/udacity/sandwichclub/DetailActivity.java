package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    TextView textViewAlsoKnownAs, tvAlsoKnownAsLabel, textViewPlaceOfOrigin, textViewDescription, textViewIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        textViewAlsoKnownAs = findViewById(R.id.also_known_tv);
        tvAlsoKnownAsLabel = findViewById(R.id.textView1);
        textViewPlaceOfOrigin = findViewById(R.id.origin_tv);
        textViewDescription = findViewById(R.id.description_tv);
        textViewIngredients = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        String prefix = ", ";
        if (sandwich.getAlsoKnownAs().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String details : sandwich.getAlsoKnownAs()) {
                stringBuilder.append(details).append(prefix);
            }
            stringBuilder.setLength(stringBuilder.length() - 2);
            textViewAlsoKnownAs.setText(stringBuilder);
        } else {
            tvAlsoKnownAsLabel.setVisibility(View.GONE);
        }
        textViewPlaceOfOrigin.setText(sandwich.getPlaceOfOrigin());
        textViewDescription.setText(sandwich.getDescription());
        if (sandwich.getIngredients().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String details : sandwich.getIngredients()) {
                stringBuilder.append(details).append(prefix);
            }
            stringBuilder.setLength(stringBuilder.length() - 2);
            textViewIngredients.setText(stringBuilder);
        }
    }
}
