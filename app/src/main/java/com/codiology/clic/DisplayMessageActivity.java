package com.codiology.clic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Context context = getActivity();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Integer highScore = sharedPref.getInt(getString(R.string.saved_count), 1);
        SharedPreferences.Editor editor = sharedPref.edit();
        highScore++;
        editor.putInt(getString(R.string.saved_count), highScore);
        editor.commit();

        TextView editText = (TextView) findViewById(R.id.count_here);
        editText.setText(highScore.toString());
        editText.setTextSize(40);

    }

}
