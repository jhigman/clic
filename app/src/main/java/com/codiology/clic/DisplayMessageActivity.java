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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

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

        ArrayList<Date> timestamps = new ArrayList<Date>();
        StringBuilder text = new StringBuilder();

        try {
            FileInputStream in = openFileInput(MainActivity.FILENAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd:HHmmss");

            while ((line = br.readLine()) != null) {
                try {
                    Date date = format.parse(line);
                    timestamps.add(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String clickCount = "0";

        if (timestamps.isEmpty()) {
            text.append("No timestamps");
        } else {
            DateFormat format = new SimpleDateFormat("dd/mmm/yyyy HH:mm:ss");
            Iterator<Date> iterator = timestamps.iterator();
            while (iterator.hasNext()) {
                text.insert(0, format.format(iterator.next()) + System.getProperty("line.separator"));
            }
            clickCount = Integer.toString(timestamps.size());
        }

        TextView editText = (TextView) findViewById(R.id.count_here);
        editText.setText(clickCount);
        editText.setTextSize(40);

        TextView timestampsText = (TextView) findViewById(R.id.timestamps);
        timestampsText.setText(text.toString());
        timestampsText.setTextSize(20);

    }

}
