package com.codiology.clic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.flic.lib.FlicButton;
import io.flic.lib.FlicButtonCallback;
import io.flic.lib.FlicButtonCallbackFlags;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String FILENAME = "clic.txt";

    private static final String TAG = "MainActivity";

    private FlicManager manager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setupFlicButton();
        showCount();
        showBarChart();

    }



    private void setupFlicButton() {


        FlicManager.setAppCredentials("290f38cb-ca36-4091-951b-3f34734cfccc", "b2d329cd-b5bb-4259-9fbc-0bc1b1f8aea0", "Codiology Clic");

        FlicManager.getInstance(this, new FlicManagerInitializedCallback() {


            @Override
            public void onInitialized(FlicManager manager) {
                Log.d(TAG, "Ready to use manager");

                MainActivity.this.manager = manager;

                // Restore buttons grabbed in a previous run of the activity
                List<FlicButton> buttons = manager.getKnownButtons();
                for (FlicButton button : buttons) {
                    String status = null;
                    switch (button.getConnectionStatus()) {
                        case FlicButton.BUTTON_DISCONNECTED:
                            status = "disconnected";
                            break;
                        case FlicButton.BUTTON_CONNECTION_STARTED:
                            status = "connection started";
                            break;
                        case FlicButton.BUTTON_CONNECTION_COMPLETED:
                            status = "connection completed";
                            break;
                    }
                    Log.d(TAG, "Found an existing button: " + button + ", status: " + status);
                    setButtonCallback(button);
                }
            }


        });


    }

    private FlicButtonCallback buttonCallback = new FlicButtonCallback() {
        @Override
        public void onButtonSingleOrDoubleClickOrHold(FlicButton button, boolean wasQueued, int timeDiff, boolean isSingleClick, boolean isDoubleClick, boolean isHold) {

            if (isHold) {
                incrementCount();
            }
            if (isDoubleClick) {
                incrementCount();
            }
            if (isSingleClick) {
                incrementCount();
            }
        }
    };


    private void setButtonCallback(FlicButton button) {
        button.removeAllFlicButtonCallbacks();
        button.addFlicButtonCallback(buttonCallback);
        button.setFlicButtonCallbackFlags(FlicButtonCallbackFlags.CLICK_OR_DOUBLE_CLICK_OR_HOLD );
        button.setActiveMode(true);
    }


    @Override
    protected void onDestroy() {
        FlicManager.destroyInstance();
        super.onDestroy();
    }


    public void grabButton(View v) {
        if (manager != null) {
            manager.initiateGrabButton(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
        if (button != null) {
            Log.d(TAG, "Got a button: " + button);
            setButtonCallback(button);
        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            showTimestamps();
        } else if (id == R.id.nav_manage) {
            showTools();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /** Called when the user clicks the Send button */
    public void resetData(View view) {
        deleteFile(FILENAME);
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
            outputStream = openFileOutput(FILENAME, MODE_APPEND);
            outputStream.write(timeStamp.getBytes());
            outputStream.write(System.getProperty("line.separator").getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        showCount();
    }

    public void showTimestamps() {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }

    public void showTools() {
        Intent intent = new Intent(this, ToolsActivity.class);
        startActivity(intent);
    }







    private void showCount() {

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

        if (!timestamps.isEmpty()) {
            clickCount = Integer.toString(timestamps.size());
        }

        setCount(clickCount);
    }

    private void setCount(final String clickCount) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView editText = (TextView) findViewById(R.id.count_here);
                editText.setText(clickCount);
                editText.setTextSize(80);
            }
        });
    }


    private void showBarChart() {

        BarChart chart = (BarChart) findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(1000, 1000);
        chart.invalidate();

    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }

}
