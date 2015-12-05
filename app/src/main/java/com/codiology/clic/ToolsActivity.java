package com.codiology.clic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
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
    }

    /** Called when the user clicks the Send button */
    public void resetData(View view) {
        deleteFile(MainActivity.FILENAME);
        showTimestamps();
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        incrementCount();
    }

    /** Called when the user clicks the Send button */
    public void incrementCount() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd:HHmmss").format(Calendar.getInstance().getTime());
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(MainActivity.FILENAME, MODE_APPEND);
            outputStream.write(timeStamp.getBytes());
            outputStream.write(System.getProperty("line.separator").getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        showTimestamps();
    }


    public void showTimestamps() {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }

    public void showTools() {
        Intent intent = new Intent(this, ToolsActivity.class);
        startActivity(intent);
    }


}
